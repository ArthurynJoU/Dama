const BASE_URL = process.env.REACT_APP_API_URL || '/api';

/**
 * GET požiadavka, vráti rozparsovaný JSON.
 * @param {string} path – cesta vzhľadom na BASE_URL, napr. '/game/new'
 */
export async function get(path) {
    const res = await fetch(`${BASE_URL}${path}`, {
        credentials: 'include', // ak sú potrebné cookies
        headers: {
            'Content-Type': 'application/json',
        },
    });
    if (!res.ok) throw new Error(`GET ${path} zlyhalo: ${res.status}`);
    return await res.json();
}

/**
 * POST požiadavka s JSON telom, vráti rozparsovaný JSON.
 * @param {string} path – cesta vzhľadom na BASE_URL, napr. '/auth/login'
 * @param {object} body – JS objekt, ktorý bude konvertovaný na JSON
 */
export async function post(path, body) {
    const res = await fetch(`${BASE_URL}${path}`, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
    if (!res.ok) throw new Error(`POST ${path} zlyhalo: ${res.status}`);
    return await res.json();
}
