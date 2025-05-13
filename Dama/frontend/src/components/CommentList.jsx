// src/components/CommentList.jsx
import React, { useState, useEffect } from 'react';
import { get, post } from '../api/httpService';
import { useAuth } from '../context/AuthContext';

function CommentList({ game }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const { user } = useAuth();

    useEffect(() => {
        get(`/comment/list?game=${game}`)
            .then(data => setComments(data))
            .catch(console.error);
    }, [game]);

    const handleSubmit = async e => {
        e.preventDefault();
        if (!newComment.trim()) return;
        try {
            await post('/comment', { game, player: user, comment: newComment });
            setNewComment('');
            const updated = await get(`/comment/list?game=${game}`);
            setComments(updated);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div>
            <h2>Comments</h2>
            <ul>
                {comments.map((c, i) => (
                    <li key={i}>
                        <strong>{c.player}</strong>: {c.comment}
                    </li>
                ))}
            </ul>

            {user ? (
                <form onSubmit={handleSubmit}>
          <textarea
              value={newComment}
              onChange={e => setNewComment(e.target.value)}
              placeholder="Write your comment"
          />
                    <button type="submit" disabled={!newComment.trim()}>
                        Send
                    </button>
                </form>
            ) : (
                <p><em>Log in to leave a comment</em></p>
            )}
        </div>
    );
}

export default CommentList;
