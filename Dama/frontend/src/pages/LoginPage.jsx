// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
    const [username, setUsername] = useState(''); // Stav pre meno používateľa
    const [error, setError] = useState(''); // Stav pre chybové hlásenie
    const { login } = useAuth(); // Prístup k funkcii login z kontextu

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            // Odoslanie mena používateľa na backend
            await post('/auth/login', { username });
            // Ak je všetko v poriadku, uložíme používateľa do kontextu
            login(username);
        } catch (err) {
            console.error(err);
            setError('Prihlásenie zlyhalo'); // Nastavenie chybovej správy
        }
    };

    return (
        <div>
            <h1>Login</h1>
            {/* Zobrazenie chyby, ak existuje */}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {/* Formulár na prihlásenie */}
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={e => setUsername(e.target.value)} // Aktualizácia stavu pri zmene
                        placeholder="Your name"
                    />
                </label>
                {/* Tlačidlo na prihlásenie, deaktivované ak je meno prázdne */}
                <button type="submit" disabled={!username.trim()}>
                    Login
                </button>
            </form>
        </div>
    );
}

export default LoginPage;
