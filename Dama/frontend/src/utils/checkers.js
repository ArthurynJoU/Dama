// src/utils/checkers.js

// Проверка границ
function isInside(r, c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
}

/* ---------- ИНИЦИАЛИЗАЦИЯ ДОСОК ---------- */

export function initializeBoard() {
    const board = Array(8).fill(null).map(() => Array(8).fill(null));
    for (let r = 0; r < 3; r++)
        for (let c = 0; c < 8; c++)
            if ((r + c) % 2 === 1) board[r][c] = { player: 2, king: false };
    for (let r = 5; r < 8; r++)
        for (let c = 0; c < 8; c++)
            if ((r + c) % 2 === 1) board[r][c] = { player: 1, king: false };
    return board;
}

export function initializeDemoBoard() {
    const b = Array(8).fill(null).map(() => Array(8).fill(null));
    b[5][2] = { player: 1, king: true };
    b[5][4] = { player: 1, king: false };
    b[2][3] = { player: 2, king: true };
    b[2][5] = { player: 2, king: false };
    return b;
}

/* ---------- ХОДЫ MAN ---------- */

function getManMoves(board, r, c) {
    const p = board[r][c];
    if (!p) return [];
    const dir = p.player === 1 ? -1 : +1;
    const out = [];
    for (let dc of [-1, +1]) {
        const nr = r + dir, nc = c + dc;
        if (isInside(nr, nc) && !board[nr][nc])
            out.push({ row: nr, col: nc, capture: false });
        const r2 = r + 2 * dir, c2 = c + 2 * dc;
        if (
            isInside(nr, nc) &&
            board[nr][nc] &&
            board[nr][nc].player !== p.player &&
            isInside(r2, c2) &&
            !board[r2][c2]
        ) {
            out.push({
                row: r2,
                col: c2,
                capture: true,
                captured: { row: nr, col: nc }
            });
        }
    }
    return out;
}

/* ---------- ХОДЫ KING (летящая дамка) ---------- */

function getKingMoves(board, r, c) {
    const p = board[r][c];
    if (!p) return [];
    const moves = [];

    for (let [dr, dc] of [
        [-1, -1],
        [-1, +1],
        [1, -1],
        [1, +1]
    ]) {
        let step = 1;
        let seenEnemy = false;
        let capPos = null;

        while (true) {
            const nr = r + dr * step;
            const nc = c + dc * step;
            if (!isInside(nr, nc)) break;

            const cell = board[nr][nc];
            if (!cell) {
                // пустая
                moves.push({
                    row: nr,
                    col: nc,
                    capture: seenEnemy,
                    captured: capPos
                });
                step++;
                continue;
            }

            if (!seenEnemy && cell.player !== p.player) {
                // первая вражеская — помним и идём дальше
                seenEnemy = true;
                capPos = { row: nr, col: nc };
                step++;
                continue;
            }

            // своя фигура или вторая вражеская — стоп
            break;
        }
    }

    return moves;
}

/* ---------- ПУБЛИЧНЫЕ АПИ ---------- */

export function getValidMoves(board, r, c) {
    const p = board[r][c];
    if (!p) return [];
    return p.king ? getKingMoves(board, r, c) : getManMoves(board, r, c);
}

export function applyMove(board, fr, fc, tr, tc, captured = null) {
    const newB = board.map(row => row.map(cell => (cell ? { ...cell } : null)));

    const piece = newB[fr][fc];
    newB[fr][fc] = null;
    newB[tr][tc] = piece;

    // обычное короткое взятие
    if (Math.abs(fr - tr) === 2 && !captured) {
        const cr = (fr + tr) / 2;
        const cc = (fc + tc) / 2;
        newB[cr][cc] = null;
    }

    // «летающая» дамка — удаляем явно переданную жертву
    if (captured) {
        newB[captured.row][captured.col] = null;
    }

    // повышение
    if (
        !piece.king &&
        ((piece.player === 1 && tr === 0) || (piece.player === 2 && tr === 7))
    ) {
        piece.king = true;
    }

    return newB;
}

export function getAllMoves(board, player) {
    const out = [];
    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {
            if (board[r][c]?.player === player) {
                getValidMoves(board, r, c).forEach(m =>
                    out.push({ from: { row: r, col: c }, ...m })
                );
            }
        }
    }
    return out;
}
