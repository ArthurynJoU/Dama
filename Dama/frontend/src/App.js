import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import { Container, AppBar, Toolbar, Typography, Button } from '@mui/material';
import MainPage from './pages/MainPage';
import SetupPage from './pages/SetupPage';
import GamePage from './pages/GamePage';
import VictoryRatingPage from './pages/VictoryRatingPage';
import VictoryCommentPage from './pages/VictoryCommentPage';
import TopPlayersPage from './pages/TopPlayersPage';
import CommentsPage from './pages/CommentsPage';
import RulesPage from './pages/RulesPage';

function App() {
    return (
        <Container maxWidth="md">
            <AppBar position="static" style={{ marginBottom: '16px' }}>
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Checkers Game
                    </Typography>
                    <Button color="inherit" component={Link} to="/">Home</Button>
                </Toolbar>
            </AppBar>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/setup" element={<SetupPage />} />
                <Route path="/game" element={<GamePage />} />
                <Route path="/victory/rating" element={<VictoryRatingPage />} />
                <Route path="/victory/comment" element={<VictoryCommentPage />} />
                <Route path="/top-players" element={<TopPlayersPage />} />
                <Route path="/comments" element={<CommentsPage />} />
                <Route path="/rules" element={<RulesPage />} />
            </Routes>
        </Container>
    );
}

export default App;
