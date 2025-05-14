import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField, Alert } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { post } from '../api/httpService';

const GAME = 'Dama';

export default function VictoryRatingPage() {
    const { state } = useLocation();
    const nav = useNavigate();
    const winner = state?.winner || JSON.parse(localStorage.getItem('winner'));

    const [rating, setRating] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        if (state?.winner) localStorage.setItem('winner', JSON.stringify(state.winner));
    }, [state]);

    if (!winner) {
        return (
            <Box textAlign="center" mt={4} className="dark-glass">
                <Typography variant="h5">No winner found</Typography>
                <Button variant="contained" onClick={() => nav('/')}>Go Home</Button>
            </Box>
        );
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
        <Box textAlign="center" mt={4} className="dark-glass">
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
                    sx={{ mb:2, input: { color: 'white' }, label: { color: 'white' } }}
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
