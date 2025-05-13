import React from 'react';
import { Typography, Box, List, ListItem } from '@mui/material';

export default function RulesPage() {
    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Checkers Rules (English)
            </Typography>
            <List>
                <ListItem>
                    Each player begins with 12 pieces on the dark squares of the three rows nearest them.
                </ListItem>
                <ListItem>
                    Players alternate turns, moving one piece at a time. Regular pieces move diagonally forward to an adjacent unoccupied dark square.
                </ListItem>
                <ListItem>
                    If an opponent's piece occupies an adjacent square and the square immediately beyond it is vacant, a capture jump is made by jumping over the opponent's piece.
                </ListItem>
                <ListItem>
                    Multiple jumps are allowed if, after a capture, another capture is immediately possible.
                </ListItem>
                <ListItem>
                    When a piece reaches the farthest row forward (the "king row"), it becomes a king and can move both forward and backward diagonally.
                </ListItem>
                <ListItem>
                    Regular pieces can only move/capture forward. Kings can move/capture in all four diagonal directions.
                </ListItem>
                <ListItem>
                    Captures are mandatory if available (according to official rules), but in this game, players may choose any legal move.
                </ListItem>
                <ListItem>
                    The game is won when the opponent has no pieces left or cannot make any legal moves.
                </ListItem>
            </List>
        </Box>
    );
}
