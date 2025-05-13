// src/components/Board.jsx
import React from 'react';
import styles from './Board.module.css';

/**
 * state – 8×8 pole, kde každý prvok je buď null, alebo znak predstavujúci figúrku.
 * Príklad: '●' a '○' alebo 'B'/'W'
 */
function Board({ state }) {
    const grid = state;

    return (
        <div className={styles.board}>
            {/* Vykreslenie jednotlivých buniek šachovnice */}
            {grid.map((row, i) =>
                row.map((cell, j) => (
                    <div
                        key={`${i}-${j}`}
                        className={`${styles.cell} ${
                            (i + j) % 2 === 0 ? styles.white : styles.black // Striedanie farieb políčok
                        }`}
                    >
                        {/* Ak je v bunke figúrka, vykresliť ju */}
                        {cell && <span className={styles.piece}>{cell}</span>}
                    </div>
                ))
            )}
        </div>
    );
}

export default Board;
