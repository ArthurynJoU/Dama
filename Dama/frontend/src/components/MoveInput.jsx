import React, { useState } from 'react';

// Komponent pre vstup ťahu hráča (textové pole a tlačidlo)
function MoveInput({ onMove }) {
    const [move, setMove] = useState('');

    // Odošle zadaný ťah do GamePage cez callback
    const handleSubmit = (event) => {
        event.preventDefault();
        if (move) {
            onMove(move);
            setMove('');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Move:
                <input
                    type="text"
                    value={move}
                    onChange={e => setMove(e.target.value)}
                    placeholder="e.g. D2-C3"
                />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
}

export default MoveInput;
