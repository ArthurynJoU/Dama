// src/components/RatingWidget.jsx
import React, { useState, useEffect } from 'react';
import { get, post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';

function RatingWidget({ game }) {
    const { user } = useAuth();
    const [avg, setAvg] = useState(0);
    const [mine, setMine] = useState(0);

    useEffect(() => {
        // stredny raiting
        get(`/rating/average?game=${game}`)
            .then(data => setAvg(data))
            .catch(console.error);

        // moj raiting (ked mam logo)
        if (user) {
            get(`/rating/user?game=${game}&player=${user}`)
                .then(data => setMine(data))
                .catch(() => setMine(0));
        }
    }, [game, user]);

    const handleChange = async e => {
        const val = Number(e.target.value);
        try {
            await post('/rating/set', { game, player: user, rating: val });
            setMine(val);
            const newAvg = await get(`/rating/average?game=${game}`);
            setAvg(newAvg);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div>
            <h2>Rating</h2>
            <p>Average: {avg.toFixed(1)}</p>

            {user ? (
                <div>
                    <p>Your rating: {mine || '—'}</p>
                    <select value={mine} onChange={handleChange}>
                        <option value={0}>Rate…</option>
                        {[1,2,3,4,5].map(n => (
                            <option key={n} value={n}>{n}</option>
                        ))}
                    </select>
                </div>
            ) : (
                <p><em>Log in to rate this game</em></p>
            )}
        </div>
    );
}

export default RatingWidget;
