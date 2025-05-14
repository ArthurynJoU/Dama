// src/pages/MainPage.jsx
import React, { useEffect, useState } from 'react';
import { Button, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { get } from '../api/httpService';

const GAME = 'Dama';

export default function MainPage() {
    const nav = useNavigate();
    const [avgRating, setAvgRating] = useState(null);

    useEffect(() => {
        get(`/rating/${GAME}`)
            .then(data => {
                if (Array.isArray(data)) {
                    if (data.length) {
                        const sum = data.reduce(
                            (acc, r) =>
                                acc + (typeof r.rating === 'number' ? r.rating : 0),
                            0
                        );
                        setAvgRating(sum / data.length);
                    }
                    return;
                }
                if (typeof data === 'number') {
                    setAvgRating(data);
                    return;
                }
                if (data && typeof data.average === 'number') {
                    setAvgRating(data.average);
                }
            })
            .catch(() =>
                get(`/rating/average/${GAME}`)
                    .then(val => setAvgRating(
                        typeof val === 'number' ? val : val?.average ?? null
                    ))
                    .catch(() => setAvgRating(null))
            );
    }, []);

    return (
        <Box textAlign="center" mt={4} >
            <Box className="dark-glass">
            <Typography variant="h3" gutterBottom >Dama</Typography>

            {typeof avgRating === 'number' && (
                <Typography variant="h6" gutterBottom>
                    Average Rating: {avgRating.toFixed(2)} ⭐ (max 5)
                </Typography>
            )}
            </Box>

            <Box mt={3} >
                <Button variant="contained" sx={{ m:1 }} onClick={() => nav('/setup')}>
                    Play
                </Button>
                <Button variant="contained" sx={{ m:1 }} onClick={() => nav('/setup?demo=true')}>
                    Demo Game
                </Button>
                <Button variant="contained" sx={{ m:1 }} onClick={() => nav('/comments')}>
                    Comments
                </Button>
                <Button variant="contained" sx={{ m:1 }} onClick={() => nav('/top-players')}>
                    Top Players
                </Button>
                <Button variant="contained" sx={{ m:1 }} onClick={() => nav('/rules')}>
                    Rules
                </Button>
            </Box>
        </Box>
    );
}
