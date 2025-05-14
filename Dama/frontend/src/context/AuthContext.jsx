import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

/* hook */
export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    const [player, setPlayer] = useState(
        () => localStorage.getItem('player') || ''
    );

    const login = (name) => {
        setPlayer(name);
        localStorage.setItem('player', name);
    };

    const logout = () => {
        setPlayer('');
        localStorage.removeItem('player');
    };

    return (
        <AuthContext.Provider value={{ player, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}
