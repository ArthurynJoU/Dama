-- Vytvorenie databázy (spustiť pod superpoužívateľom)
-- CREATE DATABASE gamestudio;

-- Prepnúť sa na databázu
-- \c gamestudio

-- Tabuľka pre výsledky hier
CREATE TABLE IF NOT EXISTS public.score (
                                            id         SERIAL      PRIMARY KEY,
                                            player     VARCHAR(255) NOT NULL,
    game       VARCHAR(255) NOT NULL,
    points     INTEGER      NOT NULL,
    played_on  DATE         NOT NULL
    );

-- Tabuľka pre komentáre ku hrám
CREATE TABLE IF NOT EXISTS public.comment (
                                              id           SERIAL      PRIMARY KEY,
                                              player       VARCHAR(255) NOT NULL,
    game         VARCHAR(255) NOT NULL,
    comment_text TEXT         NOT NULL,
    commented_on DATE         NOT NULL
    );

-- Tabuľka pre hodnotenia hier
CREATE TABLE IF NOT EXISTS public.rating (
                                             id        SERIAL      PRIMARY KEY,
                                             player    VARCHAR(255) NOT NULL,
    game      VARCHAR(255) NOT NULL,
    rating    INTEGER      NOT NULL CHECK (rating BETWEEN 1 AND 5),
    rated_on  DATE         NOT NULL,
    UNIQUE (player, game)  -- Každý hráč môže dať hodnotenie len raz na danú hru
    );

-- Index pre rýchle získanie top výsledkov
CREATE INDEX IF NOT EXISTS idx_score_game_points
    ON public.score (game, points DESC);

-- Index pre rýchle načítanie najnovších komentárov
CREATE INDEX IF NOT EXISTS idx_comment_game_date
    ON public.comment (game, commented_on DESC);

-- Index pre filtrovanie hodnotení podľa hry
CREATE INDEX IF NOT EXISTS idx_rating_game
    ON public.rating (game);

SELECT * FROM score;
SELECT * FROM comment;
SELECT * FROM rating;
