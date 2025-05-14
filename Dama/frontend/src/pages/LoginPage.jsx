import React, { useState } from 'react';
import { Box, TextField, Button, Typography, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
    const [name, setName] = useState('');
    const [error, setError] = useState(null);
    const nav = useNavigate();
    const { login } = useAuth();

    const handleSubmit = (e) => {
        e.preventDefault();
        const trimmed = name.trim();
        if (!trimmed) {
            setError('Name is required');
            return;
        }
        login(trimmed);
        nav('/profile');
    };

    return (
        <Box textAlign="center" mt={4} className="dark-glass">
            <Typography variant="h4" gutterBottom>Login</Typography>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <Box component="form" onSubmit={handleSubmit} >
                <TextField
                    label="Username"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                    sx={{ mb: 2 }}
                />
                <Button type="submit" variant="contained">Login</Button>
            </Box>
        </Box>
    );
}
