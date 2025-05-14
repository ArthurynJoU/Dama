// src/components/Square.jsx
import React from 'react';
import { Box } from '@mui/material';

export default function Square({ children, black, onClick, style }) {
    const darkGradient  = 'linear-gradient(135deg,#5d3314 0%,#9b6133 100%)';
    const lightGradient = 'linear-gradient(135deg,#f9f8f3 0%,#e5dcc4 100%)';

    return (
        <Box
            onClick={onClick}
            sx={{
                width: 60,
                height: 60,
                background: black ? darkGradient : lightGradient,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                cursor: onClick ? 'pointer' : 'default',
                border: '1px solid rgba(0,0,0,0.4)',
                '&:hover': onClick && { filter: 'brightness(0.9)' },
                ...style,
            }}
        >
            {children}
        </Box>
    );
}
