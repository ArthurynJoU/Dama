import axios from 'axios';

export const postScore = data => axios.post('/api/score', data);
export const getTopScores = game => axios.get(`/api/score/top/${game}`);
