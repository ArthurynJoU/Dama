import React, { useState, useEffect } from 'react';
import { get } from '../api/httpService';

function ScoreBoard({ game, limit }) {
    const [scores, setScores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);   // добавим сообщение об ошибке

        useEffect(() => {
                setLoading(true);
                setError(null);

                    // запрашиваем именно для нужной игры!
                      get(`/score/${game}?limit=${limit}`)
                   .then(data => setScores(data))
            .catch(err => {
                           console.error(err);
                           setError('No scores yet.');
                       })
                    .finally(() => setLoading(false));
           }, [game, limit]);

    if (loading) return <p>Loading scores…</p>;
    if (error)   return <p style={{color:'red'}}>{error}</p>;
    if (!scores.length) return <p>No scores yet.</p>;

    return (
        <table>
            <thead>
                         <tr>
                              <th>#</th>
                              <th>Player</th>
                             <th>Score</th>
                             <th>Data</th>
                             </tr>
                      </thead>
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
