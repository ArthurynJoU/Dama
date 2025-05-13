// src/pages/GamePage.jsx
import React, { useState, useEffect } from 'react';
import { get, post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Board from '../components/Board';
import MoveInput from '../components/MoveInput';
import CommentList from '../components/CommentList';
import RatingWidget from '../components/RatingWidget';

function GamePage() {
    const { user } = useAuth(); // Získanie aktuálneho používateľa z kontextu
    const navigate = useNavigate(); // Hook na navigáciu medzi stránkami
    const [grid, setGrid] = useState(() => Array(8).fill(Array(8).fill(null))); // Stav hernej plochy
    const [time, setTime] = useState(0); // Časovač hry
    const [status, setStatus] = useState('PLAYING'); // Stav hry

    // Načítanie novej hry pri načítaní stránky
    useEffect(() => {
        get('/game/new')
            .then(data => {
                setGrid(data.grid); // Nastavenie hernej plochy
                setStatus(data.status); // Nastavenie stavu hry
            })
            .catch(console.error);
    }, []);

    // Spustenie časovača, ak hra prebieha
    useEffect(() => {
        if (status !== 'PLAYING') return;
        const timer = setInterval(() => setTime(t => t + 1), 1000);
        return () => clearInterval(timer); // Vyčistenie časovača pri zmene stavu
    }, [status]);

    // Spracovanie pohybu hráča
    const handleMove = async move => {
        try {
            const result = await post('/game/move', { move, player: user });
            setGrid(result.grid); // Aktualizácia hernej plochy
            setStatus(result.status); // Aktualizácia stavu hry
            if (result.status === 'FINISHED') {
                // Ak je hra ukončená, uložiť skóre a presmerovať na tabuľku výsledkov
                await post('/score', {
                    game: 'Dama',
                    player: user,
                    points: result.points,
                });
                navigate('/scores');
            }
        } catch (err) {
            console.error(err);
            alert('Neplatný ťah'); // Zobrazenie chyby pri neplatnom ťahu
        }
    };

    // Reštart hry
    const handleRestart = () => {
        setTime(0); // Vynulovanie časovača
        setStatus('PLAYING'); // Nastavenie hry na aktívnu
        get('/game/new')
            .then(data => setGrid(data.grid)) // Načítanie novej hernej plochy
            .catch(console.error);
    };

    return (
        <div>
            <h1>Dama</h1>
            <Board state={grid} /> {/* Komponent na vykreslenie hernej plochy */}
            <MoveInput onMove={handleMove} /> {/* Komponent na zadávanie ťahov */}
            <div>Time: {time}s</div> {/* Zobrazenie uplynutého času */}
            <button onClick={handleRestart}>Restart</button> {/* Tlačidlo na reštart hry */}

            <CommentList game="Dama" /> {/* Komponent na komentáre ku hre */}
            <RatingWidget game="Dama" /> {/* Komponent na hodnotenie hry */}
        </div>
    );
}

export default GamePage;
