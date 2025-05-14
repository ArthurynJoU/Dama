import React from 'react';
import { Box, keyframes } from '@mui/material';

const spin = keyframes`
  from { transform: rotate(0deg); }
  to   { transform: rotate(360deg); }
`;

export default function UmbrellaPiece({ king, player }) {
    const borderColor = king ? 'gold' : 'transparent';
    const invert      = player === 1;

    return (
        <Box
            component="img"
            src="/images/umbrella.png"
            alt="piece"
            sx={{
                width: 50,
                height: 50,
                filter: invert ? 'invert(1)' : 'none',
                borderRadius: '50%',
                border: `3px solid ${borderColor}`,
                animation: `${spin} 3s linear infinite`,
            }}
        />
    );
}
