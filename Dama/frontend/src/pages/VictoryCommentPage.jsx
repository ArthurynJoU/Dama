// src/pages/VictoryCommentPage.jsx
import React, { useState } from 'react';
import { Box, Typography, Button, TextField } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { post } from '../api/httpService';

const GAME = 'Dama';

export default function VictoryCommentPage() {
    const { state } = useLocation();
    const nav = useNavigate();
    const { winner } = state || {};
    const [comment, setComment] = useState('');

    if (!winner) {
        nav('/');
        return null;
    }

    const handleSubmit = async e => {
        e.preventDefault();
        if (comment.trim()) {
            await post('/comment', {
                game: GAME,
                player: winner.name,
                comment: comment.trim(),
                commentedOn: new Date().toISOString()
            });
        }
        nav('/');
    };

    const handleSkip = () => nav('/');

    return (
        <Box textAlign="center" mt={4} className="dark-glass">
            <Typography variant="h6" gutterBottom>
                Leave a comment (optional)
            </Typography>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mt:2 }}
            >
                <TextField
                    label="Comment"
                    value={comment}
                    onChange={e => setComment(e.target.value)}
                    multiline
                    rows={4}
                    sx={{ width: '100%', maxWidth: 400, mb:2 }}
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
