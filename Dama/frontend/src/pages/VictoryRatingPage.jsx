// src/pages/VictoryRatingPage.jsx
import React, { useState } from 'react';
import { Box, Typography, Button, TextField, Alert } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { post } from '../api/httpService';

const GAME = 'Dama';

export default function VictoryRatingPage() {
    const { state } = useLocation();
    const nav = useNavigate();
    const { winner } = state || {};
    const [rating, setRating] = useState('');
    const [error, setError] = useState(null);

    if (!winner) {
        nav('/');
        return null;
    }

    const handleSubmit = async e => {
        e.preventDefault();
        const val = parseFloat(rating);
        if (isNaN(val) || val < 0 || val > 5) {
            setError('Please enter a number between 0 and 5');
            return;
        }
        setError(null);
        await post('/rating', {
            game: GAME,
            player: winner.name,
            rating: val,
            ratedOn: new Date().toISOString()
        });
        nav('/victory/comment', { state: { winner } });
    };

    const handleSkip = () => nav('/victory/comment', { state: { winner } });

    return (
        <Box textAlign="center" mt={4}>
            <Typography variant="h5" gutterBottom>
                Congrats, {winner.name}!
            </Typography>
            <Typography gutterBottom>
                Rate the game (0–5), decimals allowed:
            </Typography>
            {error && <Alert severity="error">{error}</Alert>}
            <Box component="form" onSubmit={handleSubmit} sx={{ mt:2 }}>
                <TextField
                    label="Rating"
                    value={rating}
                    onChange={e => setRating(e.target.value)}
                    type="number"
                    inputProps={{ step: 0.1, min: 0, max: 5 }}
                    sx={{ mb:2 }}
                />
                <Box>
                    <Button type="submit" variant="contained" sx={{ m:1 }}>
                        Submit
                    </Button>
                    <Button variant="outlined" sx={{ m:1 }} onClick={handleSkip}>
                        Skip
                    </Button>
                </Box>
            </Box>
        </Box>
    );
}
