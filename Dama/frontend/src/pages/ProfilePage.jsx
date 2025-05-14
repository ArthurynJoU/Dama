// src/pages/ProfilePage.jsx
import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    Alert,
    List,
    ListItem,
    CircularProgress,
    TextField,
    Button,
    Stack,
} from '@mui/material';
import { useAuth } from '../context/AuthContext';
import {
    fetchScores,
    fetchRatings,
    fetchComments,
} from '../api/profileApi';
import { post } from '../api/httpService';

const GAME = 'Dama';

export default function ProfilePage() {
    const { player } = useAuth();

    const [loading, setLoading]   = useState(true);
    const [maxScore, setMaxScore] = useState(0);
    const [rating, setRating]     = useState(0);
    const [comments, setComments] = useState([]);

    const [ratingInput, setRatingInput] = useState('');
    const [commentInput, setCommentInput] = useState('');
    const [msg, setMsg] = useState(null);

    const loadData = () => {
        setLoading(true);
        Promise.allSettled([
            fetchScores(GAME),
            fetchRatings(GAME),
            fetchComments(GAME),
        ]).then(results => {
            if (results[0].status === 'fulfilled') {
                const val = results[0].value;
                if (Array.isArray(val)) {
                    const my = val.filter(s => s.player === player);
                    if (my.length) setMaxScore(Math.max(...my.map(s => s.points)));
                }
            }
            if (results[1].status === 'fulfilled') {
                const val = results[1].value;
                let r = 0;
                if (Array.isArray(val)) {
                    const rec = val.find(v => v.player === player);
                    if (rec) r = rec.rating;
                } else if (typeof val === 'number') r = val;
                else if (val && typeof val === 'object')
                    r = val.rating ?? val.average ?? 0;
                setRating(r);
            }
            if (results[2].status === 'fulfilled') {
                const val = results[2].value;
                if (Array.isArray(val)) {
                    const my = val
                        .filter(c => c.player === player)
                        .map(c => c.comment);
                    setComments(my);
                }
            }
        }).finally(() => {
            setLoading(false);
            setMsg(null);
        });
    };

    useEffect(() => {
        if (player) loadData();
    }, [player]);

    const handleAddRating = async () => {
        const val = parseFloat(ratingInput);
        if (isNaN(val) || val < 0 || val > 5) {
            setMsg('Enter number 0-5');
            return;
        }
        await post('/rating', {
            game: GAME,
            player,
            rating: val,
            ratedOn: new Date().toISOString(),
        });
        setRating(val);
        setRatingInput('');
        setMsg('Rating saved!');
    };

    const handleAddComment = async () => {
        if (!commentInput.trim()) return;
        await post('/comment', {
            game: GAME,
            player,
            comment: commentInput.trim(),
            commentedOn: new Date().toISOString(),
        });
        setCommentInput('');
        setMsg('Comment added!');
        loadData();
    };

    if (!player) return <Alert severity="warning">Please login first.</Alert>;
    if (loading)   return <CircularProgress sx={{ mt: 4, display: 'block', mx: 'auto' }} />;

    return (
        <Box mt={4} textAlign="center" className="dark-glass">
            <Typography variant="h4" gutterBottom>{player}</Typography>

            <Typography>Max score: {maxScore}</Typography>
            <Typography>Your rating: {rating}</Typography>

            <Box mt={3}>
                <Stack
                    direction="row"
                    spacing={2}
                    justifyContent="center"
                    alignItems="center"
                >
                    <TextField
                        type="number"
                        inputProps={{ step: 0.1, min: 0, max: 5 }}
                        label="New rating (0-5)"
                        value={ratingInput}
                        onChange={e => setRatingInput(e.target.value)}
                        size="small"
                    />
                    <Button variant="contained" onClick={handleAddRating}>
                        Save rating
                    </Button>
                </Stack>
            </Box>

            <Box mt={3}>
                <Stack spacing={2} justifyContent="center" alignItems="center">
                    <TextField
                        label="Add comment"
                        multiline
                        rows={3}
                        value={commentInput}
                        onChange={e => setCommentInput(e.target.value)}
                        sx={{ width: 400, maxWidth: '100%' }}
                    />
                    <Button variant="contained" onClick={handleAddComment}>
                        Add comment
                    </Button>
                </Stack>
            </Box>

            {msg && <Alert sx={{ mt: 2 }}>{msg}</Alert>}

            <Typography variant="h6" sx={{ mt: 4 }}>Your comments</Typography>
            {comments.length === 0 ? (
                <Typography>No comments yet.</Typography>
            ) : (
                <List sx={{ maxWidth: 500, mx: 'auto' }}>
                    {comments.map((c, i) => (
                        <ListItem key={i}>{c}</ListItem>
                    ))}
                </List>
            )}
        </Box>
    );
}
