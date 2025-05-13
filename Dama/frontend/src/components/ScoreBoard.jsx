import React, { useState, useEffect } from 'react';
import { get } from '../api/httpService';

function ScoreBoard({ game, limit }) {
    const [scores, setScores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);   // добавим сообщение об ошибке

    useEffect(() => {
        setLoading(true);
        setError(null);

        get(`/score/top?limit=${limit}`)
            .then(data => setScores(data))
            .catch(err => {
                console.error(err);
                setError('Failed to load scores');     // покажем юзеру
            })
            .finally(() => setLoading(false));       // ← ОБЯЗАТЕЛЬНО
    }, [game, limit]);

    if (loading) return <p>Loading scores…</p>;
    if (error)   return <p style={{color:'red'}}>{error}</p>;
    if (!scores.length) return <p>No scores yet.</p>;

    return (
        <table>
            <thead>…</thead>
            <tbody>
            {scores.map((s,i)=>(
                <tr key={i}>
                    <td>{i+1}</td><td>{s.player}</td><td>{s.points}</td>
                    <td>{new Date(s.playedOn).toLocaleDateString()}</td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}
export default ScoreBoard;
