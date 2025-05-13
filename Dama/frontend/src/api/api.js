// Functions for interacting with backend API.
// Adjust endpoint URLs as needed to match your backend.

export async function getAverageRating() {
    try {
        const response = await fetch('/api/averageRating');
        const data = await response.json();
        return data.averageRating;
    } catch (error) {
        console.error('Failed to fetch average rating:', error);
        return null;
    }
}

export async function postGameResult(winner) {
    try {
        await fetch('/api/gameResult', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ winner }),
        });
    } catch (error) {
        console.error('Failed to post game result:', error);
    }
}

export async function postRating(rating, player) {
    try {
        await fetch('/api/rating', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ player, rating }),
        });
    } catch (error) {
        console.error('Failed to post rating:', error);
    }
}

export async function postComment(comment, player) {
    try {
        await fetch('/api/comment', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ player, comment }),
        });
    } catch (error) {
        console.error('Failed to post comment:', error);
    }
}

export async function getTopPlayers() {
    try {
        const response = await fetch('/api/topPlayers');
        const data = await response.json();
        return data.topPlayers;
    } catch (error) {
        console.error('Failed to fetch top players:', error);
        return [];
    }
}

export async function getComments() {
    try {
        const response = await fetch('/api/comments');
        const data = await response.json();
        return data.comments;
    } catch (error) {
        console.error('Failed to fetch comments:', error);
        return [];
    }
}

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