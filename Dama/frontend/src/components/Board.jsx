// src/components/Board.jsx
import React from 'react';
import { Box } from '@mui/material';
import Square from './Square';

export default function Board({ board, onSquareClick, possibleMoves, selectedDest }) {
    return (
        <Box
            sx={{
                display: 'grid',
                gridTemplateColumns: 'repeat(8, 60px)',
                border: '2px solid #333',
                boxSizing: 'content-box',
            }}
        >
            {board.map((row, r) =>
                row.map((cell, c) => {
                    const black = (r + c) % 2 === 1;
                    const isDest = selectedDest?.row === r && selectedDest?.col === c;
                    const isMove = possibleMoves.some(m => m.row === r && m.col === c);
                    const style = isDest
                        ? { boxShadow: '0 0 0 4px #8FBC8F inset' } // light green
                        : isMove
                            ? { boxShadow: '0 0 0 3px #f00 inset' } // red
                            : {};
                    return (
                        <Square
                            key={`${r}-${c}`}
                            black={black}
                            onClick={() => onSquareClick(r, c)}
                            style={style}
                        >
                            {cell && (
                                <Box
                                    sx={{
                                        width: 40,
                                        height: 40,
                                        borderRadius: '50%',
                                        backgroundColor: cell.player === 1 ? '#fff' : '#000',
                                        border: cell.king ? '3px solid gold' : 'none',
                                    }}
                                />
                            )}
                        </Square>
                    );
                })
            )}
        </Box>
    );
}
