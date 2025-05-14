export async function postScore(game, player, points) {
    try {
        await fetch('/api/score', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                game,
                player,
                points,
                playedOn: new Date().toISOString()
            })
        });
    } catch (e) {
        console.error('Failed to post score', e);
    }
}