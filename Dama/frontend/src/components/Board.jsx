import React from 'react';
import styles from './Board.module.css';

/**
 * grid – 8×8 pole, kde každý prvok je buď null, alebo znak predstavujúci figúrku.
 * Príklad: '●' a '○' alebo 'B'/'W'
 */
function Board({ grid }) {
    return (
        <div className={styles.board}>
            {grid.map((row, i) =>
                row.map((cell, j) => (
                    <div
                        key={`${i}-${j}`}
                        className={`${styles.cell} ${
                            (i + j) % 2 === 0 ? styles.white : styles.black
                        }`}
                    >
                        {cell && <span className={styles.piece}>{cell}</span>}
                    </div>
                ))
            )}
        </div>
    );
}

export default Board;
