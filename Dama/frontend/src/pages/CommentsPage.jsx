// src/pages/CommentsPage.jsx
import React, { useState, useEffect } from 'react';
import {
    Box,
    Typography,
    List,
    ListItem,
    ListItemText,
    CircularProgress,
    Alert,
} from '@mui/material';
import { get } from '../api/httpService';

const GAME = 'Dama';

export default function CommentsPage() {
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        get(`/comment/${GAME}`)
            .then(data => setComments(data))
            .catch(() => setError('Failed to load comments.'))
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return (
            <Box textAlign="center" mt={4}>
                <CircularProgress />
            </Box>
        );
    }
    if (error) {
        return (
            <Box textAlign="center" mt={4}>
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Winner Comments
            </Typography>
            <Box
                sx={{
                    maxHeight: 400,
                    overflowY: 'auto',
                    border: '1px solid #ccc',
                    borderRadius: 1,
                    p: 1,
                }}
            >
                {comments.length === 0 ? (
                    <Typography>No comments yet.</Typography>
                ) : (
                    <List>
                        {comments.map(c => (
                            <ListItem key={c.id} divider>
                                <ListItemText
                                    primary={`${c.player} — ${new Date(c.commentedOn).toLocaleString()}`}
                                    secondary={c.comment}
                                />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Box>
        </Box>
    );
}
