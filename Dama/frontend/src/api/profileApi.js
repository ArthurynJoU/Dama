import { get } from './httpService';

export const fetchScores = (game) =>
    get(`/score/${game}?limit=1000`);          // limit побольше

export const fetchRatings = (game) =>
    get(`/rating/${game}`);                    // уже использовали раньше

export const fetchComments = (game) =>
    get(`/comment/${game}`);
