import React from 'react';
import { Box } from '@mui/material';
import Square from './Square';
import UmbrellaPiece from './UmbrellaPiece';

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
                    const black   = (r + c) % 2 === 1;
                    const isDest  = selectedDest?.row === r && selectedDest?.col === c;
                    const isMove  = possibleMoves.some(m => m.row === r && m.col === c);

                    const style = isDest
                        ? { boxShadow: '0 0 0 4px #8FBC8F inset' }
                        : isMove
                            ? { boxShadow: '0 0 0 3px #f00 inset' }
                            : {};

                    return (
                        <Square
                            key={`${r}-${c}`}
                            black={black}
                            onClick={() => onSquareClick(r, c)}
                            style={style}
                        >
                            {cell && (
                                <UmbrellaPiece
                                    king   = {cell.king}
                                    player = {cell.player}
                                />
                            )}
                        </Square>
                    );
                })
            )}
        </Box>
    );
}
