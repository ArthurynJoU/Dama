// src/pages/GamePage.jsx
import React, { useState, useEffect } from 'react';
import { Box, Typography } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import Board from '../components/Board';
import {
    initializeBoard,
    initializeDemoBoard,
    getValidMoves,
    applyMove,
    getAllMoves,
} from '../utils/checkers';
import { postScore } from '../api/api';

export default function GamePage() {
    const { state } = useLocation();
    const nav = useNavigate();
    const { player1, player2, demo } = state || {};

    const [board, setBoard] = useState(
        demo ? initializeDemoBoard() : initializeBoard()
    );
    const [turn, setTurn] = useState(1);
    const [possibleMoves, setPossibleMoves] = useState([]);
    const [selectedPiece, setSelectedPiece] = useState(null);
    const [selectedDest, setSelectedDest] = useState(null);
    const [whiteScore, setWhiteScore] = useState(0);
    const [blackScore, setBlackScore] = useState(0);
    const [timeLeft, setTimeLeft] = useState(60);

    useEffect(() => {
        if (!player1 || !player2) nav('/');
    }, [player1, player2, nav]);

    useEffect(() => {
        setTimeLeft(60);
    }, [turn]);

    useEffect(() => {
          if (timeLeft <= 0) {
                handleTimeout();
                return;
              }
          const t = setTimeout(() => setTimeLeft(t => t - 1), 1000);
          return () => clearTimeout(t);
        }, [timeLeft]);

    useEffect(() => {
        const cur = turn === 1 ? player1 : player2;
        if (cur.type === 'Bot') {
            const t = setTimeout(botMove, 500);
            return () => clearTimeout(t);
        }
    }, [turn, board]);

    const onSquareClick = (r, c) => {
        const piece = board[r][c];
        if (selectedPiece) {
            if (piece?.player === turn) {
                setSelectedPiece({ row: r, col: c });
                setPossibleMoves(getValidMoves(board, r, c));
                setSelectedDest(null);
                return;
            }
            const mv = possibleMoves.find(m => m.row === r && m.col === c);
            if (mv) setSelectedDest(mv);
            return;
        }
        if (piece?.player === turn) {
            setSelectedPiece({ row: r, col: c });
            setPossibleMoves(getValidMoves(board, r, c));
        }
    };

    useEffect(() => {
        const handler = e => {
            if (e.key === 'Enter' && selectedPiece && selectedDest) {
                doMove(selectedPiece, selectedDest);
            }
        };
        window.addEventListener('keydown', handler);
        return () => window.removeEventListener('keydown', handler);
    });

    const botMove = () => {
        const moves = getAllMoves(board, turn);
        if (moves.length === 0) return finishGame();
        const mv = moves[Math.floor(Math.random() * moves.length)];
        doMove(mv.from, mv);
    };

    const finishGame = () => {
        const winner = turn === 1 ? player1 : player2;
        const points = turn === 1 ? whiteScore : blackScore;
        postScore('Dama', winner.name, points);
        if (winner.type === 'Human') {
            nav('/victory/rating', { state: { winner } });
        } else {
            nav('/');
        }
    };

    const handleTimeout = () => {
        setSelectedPiece(null);
        setSelectedDest(null);
        setPossibleMoves([]);

        setTurn(t => (t === 1 ? 2 : 1));
    };

    const doMove = (from, dest) => {
        const prevBoard = board;

        const newB = applyMove(
            board,
            from.row,
            from.col,
            dest.row,
            dest.col,
            dest.capture ? dest.captured : null
        );
        setBoard(newB);

        if (dest.capture) {
            const capPiece = prevBoard[dest.captured.row][dest.captured.col];
            const pts = capPiece.king ? 2 : 1;
            turn === 1
                ? setWhiteScore(s => s + pts)
                : setBlackScore(s => s + pts);
        }

        // сброс выделений
        setSelectedPiece(null);
        setSelectedDest(null);
        setPossibleMoves([]);

        // проверяем конец игры
        const opp = turn === 1 ? 2 : 1;
        const stillHas = newB.some(row =>
            row.some(cell => cell && cell.player === opp)
        );
        if (!stillHas) {
            finishGame();
            return;
        }

        setTurn(opp);
    };


    return (
        <Box textAlign="center" mt={2} >
            <Box display="flex" justifyContent="center" alignItems="center" mb={1} className="dark-glass">
                <Typography sx={{ mx: 4 }}>
                    {player1.name}: {whiteScore} 🏅
                </Typography>
                <Typography sx={{ mx: 4 }}>
                    Turn: {turn === 1 ? player1.name : player2.name} ({timeLeft}s)
                </Typography>
                <Typography sx={{ mx: 4 }}>
                    {player2.name}: {blackScore} 🏅
                </Typography>
            </Box>

            <Box display="flex" justifyContent="center">
                <Board
                    board={board}
                    onSquareClick={onSquareClick}
                    possibleMoves={possibleMoves}
                    selectedDest={selectedDest}
                />
            </Box>
        </Box>
    );
}
