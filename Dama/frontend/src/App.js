// src/App.js
import React from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import { Container, AppBar, Toolbar, Typography, Button } from '@mui/material';
import { AuthProvider, useAuth } from './context/AuthContext';

import MainPage        from './pages/MainPage';
import SetupPage       from './pages/SetupPage';
import GamePage        from './pages/GamePage';
import CommentsPage    from './pages/CommentsPage';
import TopPlayersPage  from './pages/TopPlayersPage';
import RulesPage       from './pages/RulesPage';
import LoginPage       from './pages/LoginPage';
import ProfilePage     from './pages/ProfilePage';
import VictoryCommentPage from "./pages/VictoryCommentPage";
import VictoryRatingPage from "./pages/VictoryRatingPage";

function Header() {
    const { player, logout } = useAuth();
    const nav = useNavigate();

    return (
        <AppBar position="static" sx={{ mb: 2 }}>
            <Toolbar>
                <Typography variant="h6" sx={{ flexGrow: 1 }}>
                    Dama Game
                </Typography>

                {/* Кнопки навигации */}
                <Button color="inherit" component={Link} to="/">
                    Main
                </Button>

                {player ? (
                    <>
                        <Button color="inherit" component={Link} to="/profile">
                            {player}
                        </Button>
                        <Button color="inherit" onClick={() => { logout(); nav('/'); }}>
                            Logout
                        </Button>
                    </>
                ) : (
                    <Button color="inherit" component={Link} to="/login">
                        Login
                    </Button>
                )}
            </Toolbar>
        </AppBar>
    );
}

export default function App() {
    return (
        <AuthProvider>
            <Container maxWidth="md">
                <Header />

                <Routes>
                    <Route path="/"           element={<MainPage />} />
                    <Route path="/setup"      element={<SetupPage />} />
                    <Route path="/game"       element={<GamePage />} />
                    <Route path="/comments"   element={<CommentsPage />} />
                    <Route path="/top-players"element={<TopPlayersPage />} />
                    <Route path="/rules"      element={<RulesPage />} />
                    <Route path="/login"      element={<LoginPage />} />
                    <Route path="/profile"    element={<ProfilePage />} />
                    <Route path="/victory/rating"   element={<VictoryRatingPage />} />
                    <Route path="/victory/comment"  element={<VictoryCommentPage />} />
                </Routes>
            </Container>
        </AuthProvider>
    );
}
