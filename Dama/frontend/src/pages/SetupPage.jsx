// src/pages/SetupPage.jsx
import React, { useState, useEffect } from 'react';
import {
    TextField,
    Button,
    Typography,
    Box,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';

export default function SetupPage() {
    const nav = useNavigate();
    const { search } = useLocation();
    const demoMode = new URLSearchParams(search).get('demo') === 'true';

    const [player1, setPlayer1] = useState({ name: '', type: 'Human' });
    const [player2, setPlayer2] = useState({ name: '', type: 'Human' });

    useEffect(() => {
        if (demoMode) {
            setPlayer1({ name: 'DemoWhite', type: 'Human' });
            setPlayer2({ name: 'DemoBlack', type: 'Bot' });
        }
    }, [demoMode]);

    const handleSubmit = e => {
        e.preventDefault();

        const n1 = player1.name.trim() || 'Player1';
        const n2 = player2.name.trim() || 'Player2';

        const name1 = player1.type === 'Bot' ? `${n1} (Bot)` : n1;
        const name2 = player2.type === 'Bot' ? `${n2} (Bot)` : n2;

        nav('/game', {
            state: {
                player1: { ...player1, name: name1 },
                player2: { ...player2, name: name2 },
                demo: demoMode,
            },
        });
    };


    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 4, textAlign: 'center' }} className="dark-glass">
            <Typography variant="h5" gutterBottom>
                {demoMode ? 'Demo Game Setup' : 'Setup Players'}
            </Typography>
            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 6, mb: 3 }}>
                {['player1', 'player2'].map((key, idx) => {
                    const state = key === 'player1' ? player1 : player2;
                    const setState = key === 'player1' ? setPlayer1 : setPlayer2;
                    return (
                        <Box key={key}>
                            <Typography variant="h6">{`Player ${idx + 1}`}</Typography>
                            <TextField
                                label="Name"
                                value={state.name}
                                onChange={e => setState({ ...state, name: e.target.value })}
                                required
                                sx={{ mb: 2 }}
                            />
                            <FormControl fullWidth>
                                <InputLabel>Type</InputLabel>
                                <Select
                                    value={state.type}
                                    label="Type"
                                    onChange={e => setState({ ...state, type: e.target.value })}
                                >
                                    <MenuItem value="Human">Human</MenuItem>
                                    <MenuItem value="Bot">Bot</MenuItem>
                                </Select>
                            </FormControl>
                        </Box>
                    );
                })}
            </Box>
            <Button type="submit" variant="contained" color="primary">
                Start Game
            </Button>
        </Box>
    );
}
