// src/pages/CommentsPage.jsx
import React, { useEffect, useState } from 'react';
import {
    Typography,
    Box,
    List,
    ListItem,
    ListItemText,
    Button,
    TextField,
    Stack,
} from '@mui/material';
import { get, post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';

const GAME = 'Dama';

export default function CommentsPage() {
    const { player } = useAuth();
    const [comments, setComments] = useState([]);
    const [replyToId, setReplyToId]   = useState(null);
    const [replyText, setReplyText]   = useState('');

    const load = () => {
        get(`/comment/${GAME}`).then(setComments);
    };
    useEffect(load, []);

    const handleReply = async (parent) => {
        if (!replyText.trim()) return;
        const text = `↳ @${parent.player}: ${replyText.trim()}`;
        await post('/comment', {
            game: GAME,
            player,
            comment: text,
            commentedOn: new Date().toISOString(),
        });
        setReplyText('');
        setReplyToId(null);
        load();
    };

    return (
        <Box sx={{ mt: 4 }} className="dark-glass">
            <Typography variant="h5" gutterBottom>
                Winner Comments
            </Typography>

            <List sx={{ maxHeight: 500, overflowY: 'auto' }}>
                {comments.map((c) => {
                    const isReply = c.comment.startsWith('↳');
                    return (
                        <Box key={c.id ?? `${c.player}-${c.commentedOn}`}>
                            <ListItem
                                sx={{ pl: isReply ? 4 : 1 }}
                                secondaryAction={
                                    player && !isReply && (
                                        <Button
                                            size="small"
                                            onClick={() =>
                                                setReplyToId(replyToId === c.id ? null : c.id)
                                            }
                                        >
                                            Reply
                                        </Button>
                                    )
                                }
                            >
                                <ListItemText
                                    primary={`${c.player} — ${new Date(
                                        c.commentedOn,
                                    ).toLocaleString()}`}
                                    secondary={c.comment}
                                />
                            </ListItem>

                            {replyToId === c.id && (
                                <Stack
                                    direction="row"
                                    spacing={1}
                                    sx={{ pl: 4, pr: 2, pb: 2 }}
                                >
                                    <TextField
                                        fullWidth
                                        size="small"
                                        label="Your reply"
                                        value={replyText}
                                        onChange={(e) => setReplyText(e.target.value)}
                                    />
                                    <Button
                                        variant="contained"
                                        onClick={() => handleReply(c)}
                                    >
                                        Send
                                    </Button>
                                </Stack>
                            )}
                        </Box>
                    );
                })}
            </List>
        </Box>
    );
}
