import React from 'react';
// Importujeme komponenty na smerovanie
import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import GamePage from './pages/GamePage';
import HighScoresPage from './pages/HighScoresPage';
import LoginPage from './pages/LoginPage';

function App() {
  return (
      <BrowserRouter>
        {/* Hlavné menu na navigáciu medzi jednotlivými stránkami */}
        <nav style={{ display: 'flex', gap: '1rem', margin: '1rem 0' }}>
          <NavLink to="/game">Game</NavLink> {/* Odkaz na stránku s hrou */}
          <NavLink to="/scores">High Scores</NavLink> {/* Odkaz na stránku s najlepšími skóre */}
          <NavLink to="/login">Login</NavLink> {/* Odkaz na prihlasovaciu stránku */}
        </nav>

        <Routes>
          {/* Trasa na stránku s hrou */}
          <Route path="/game" element={<GamePage />} />
          {/* Trasa na stránku s top 5 výsledkami */}
          <Route path="/scores" element={<HighScoresPage />} />
          {/* Trasa na prihlasovaciu stránku */}
          <Route path="/login" element={<LoginPage />} />
          {/* Akákoľvek iná trasa presmeruje na stránku s hrou */}
          <Route path="*" element={<GamePage />} />
        </Routes>
      </BrowserRouter>
  );
}

export default App;
