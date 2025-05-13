import React, { useState, useEffect } from 'react';
import { get, post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

import Board from '../components/Board';
import MoveInput from '../components/MoveInput';
import CommentList from '../components/CommentList';
import RatingWidget from '../components/RatingWidget';

export default function GamePage() {
    const { user } = useAuth();
    const navigate = useNavigate();

    // 8×8—пустая доска
    const [grid, setGrid] = useState(
        Array.from({ length: 8 }, () => Array(8).fill(null))
    );
    const [time, setTime] = useState(0);
    const [status, setStatus] = useState('PLAYING');

    // при загрузке страницы подгружаем новую игру
    useEffect(() => {
        get('/game/new')
            .then(data => {
                // некоторые бекенды возвращают { grid: [...], status: 'PLAYING' }
                setGrid(data.grid ?? data);
                setStatus(data.status ?? 'PLAYING');
            })
            .catch(console.error);
    }, []);

    // таймер, пока играем
    useEffect(() => {
        if (status !== 'PLAYING') return;
        const timer = setInterval(() => setTime(t => t + 1), 1000);
        return () => clearInterval(timer);
    }, [status]);

    const handleMove = async move => {
        try {
            const result = await post('/game/move', { move, player: user });
            setGrid(result.grid);
            setStatus(result.status);

            if (result.status === 'FINISHED') {
                // сохраним очки
                await post('/score', {
                    game: 'Dama',
                    player: user,
                    points: result.points,
                });
                navigate('/scores');
            }
        } catch {
            alert('Неверный ход');
        }
    };

    const handleRestart = () => {
        setTime(0);
        setStatus('PLAYING');
        get('/game/new')
            .then(data => setGrid(data.grid ?? data))
            .catch(console.error);
    };

    return (
        <div>
            <h1>Dama</h1>

            {/* доска */}
            <Board grid={grid} />

            {/* ввод хода */}
            <MoveInput onMove={handleMove} />

            <div>Time: {time}s</div>
            <button onClick={handleRestart}>Restart</button>

            {/* комментарии и рейтинг в своих компонентах */}
            <CommentList game="Dama" />
            <RatingWidget game="Dama" />
        </div>
    );
}
