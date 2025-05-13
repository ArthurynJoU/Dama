import React, { useState, useEffect } from 'react';
import { get } from '../api/httpService';

export default function ScoreBoard({ game, limit }) {
    const [scores, setScores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        get(`/score/${game}?limit=${limit}`)
            .then(data => setScores(data))
            .catch(err => {
                console.error(err);
                setError('No scores yet.');
            })
            .finally(() => setLoading(false));
    }, [game, limit]);

    if (loading) return <p>Loading scores…</p>;
    if (error)   return <p style={{ color: 'red' }}>{error}</p>;
    if (!scores.length) return <p>No scores yet.</p>;

    return (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
            <tr>
                <th style={{ borderBottom: '1px solid #ccc' }}>#</th>
                <th style={{ borderBottom: '1px solid #ccc' }}>Player</th>
                <th style={{ borderBottom: '1px solid #ccc' }}>Score</th>
                <th style={{ borderBottom: '1px solid #ccc' }}>Date</th>
            </tr>
            </thead>
            <tbody>
            {scores.map((s, i) => (
                <tr key={i}>
                    <td style={{ padding: '8px' }}>{i + 1}</td>
                    <td style={{ padding: '8px' }}>{s.player}</td>
                    <td style={{ padding: '8px' }}>{s.points}</td>
                    <td style={{ padding: '8px' }}>
                        {new Date(s.playedOn).toLocaleDateString()}
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}
