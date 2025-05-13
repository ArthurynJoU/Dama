// src/context/AuthContext.jsx
import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

/**
 * Poskytovateľ autentifikácie.
 * Uchováva aktuálne používateľské meno (alebo token) v stave.
 * poskytuje: { user, login, logout }
 */
export function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        // pri prvom vykreslení načítame meno používateľa z localStorage
        return localStorage.getItem('user') || null;
    });

    // voliteľné: pri načítaní overiť platnosť tokenu na serveri
    useEffect(() => {
        if (user) {
            // napríklad zavolať get(`/auth/me`) a pri chybe vykonať logout()
        }
    }, [user]);

    // jednoduchá funkcia na prihlásenie
    async function login(username) {
        // ak backend vyžaduje požiadavku:
        // const data = await post('/auth/login', { username });
        // setUser(data.username);
        setUser(username);
        localStorage.setItem('user', username); // uložíme používateľa aj do localStorage
    }

    // funkcia na odhlásenie používateľa
    function logout() {
        // ak je potrebné, vykonať aj post('/auth/logout')
        setUser(null);
        localStorage.removeItem('user'); // odstránime používateľa z localStorage
    }

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

// hook na prístup ku kontextu autentifikácie
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error('useAuth musí byť použitý v rámci AuthProvider');
    return ctx;
}
