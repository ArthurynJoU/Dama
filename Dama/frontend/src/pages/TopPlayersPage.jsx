import React, { useState } from 'react';
import ScoreBoard from '../components/ScoreBoard';
import { Box, Typography, Button } from '@mui/material';

export default function TopPlayersPage() {
    const [reloadFlag, setReloadFlag] = useState(0);

    return (
        <Box sx={{ mt: 4, textAlign: 'center' }}>
            <Typography variant="h5" gutterBottom>
                Top 5 Players
            </Typography>
            {/* Переброска reloadFlag в key форсит перезагрузку ScoreBoard */}
            <ScoreBoard key={reloadFlag} game="Dama" limit={5} />
        </Box>
    );
}
