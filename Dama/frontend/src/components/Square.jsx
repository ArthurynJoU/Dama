// src/components/Square.jsx
import React from 'react';
import { Box } from '@mui/material';

export default function Square({ children, black, onClick, style }) {
    return (
        <Box
            onClick={onClick}
            sx={{
                width: 60,
                height: 60,
                backgroundColor: black ? '#8B4513' : '#F5F5DC', // brown / beige
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                cursor: onClick ? 'pointer' : 'default',
                border: '1px solid #444',
                ...style,
            }}
        >
            {children}
        </Box>
    );
}
