// HighScoresPage.jsx
import React, { useState } from 'react';
import ScoreBoard from '../components/ScoreBoard';

function HighScoresPage() {
    const [reloadFlag, setReloadFlag] = useState(0);

    return (
        <div>
            <h1>High Scores</h1>
            <button onClick={() => setReloadFlag(f => f + 1)}>
                Refresh
            </button>

            {/* reloadFlag пойдёт в ScoreBoard как ключ и заставит useEffect заново сделать GET */}
            <ScoreBoard key={reloadFlag} game="Dama" limit={5} />
        </div>
    );
}

export default HighScoresPage;
