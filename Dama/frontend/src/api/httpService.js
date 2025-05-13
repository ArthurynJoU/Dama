// src/api/httpService.js
const BASE_URL = process.env.REACT_APP_API_URL || '/api';

export async function get(path) {
    const res = await fetch(`${BASE_URL}${path}`, {
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' }
    });
    if (!res.ok) throw new Error(`GET ${path} failed: ${res.status}`);
    return await res.json();
}

export async function post(path, body) {
    const res = await fetch(`${BASE_URL}${path}`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    if (!res.ok) throw new Error(`POST ${path} failed: ${res.status}`);
    // если тело пустое, res.text() даст '' → просто возвращаем null
    const text = await res.text();
    if (!text) return null;
    try {
        return JSON.parse(text);
    } catch {
        return null;
    }
}
