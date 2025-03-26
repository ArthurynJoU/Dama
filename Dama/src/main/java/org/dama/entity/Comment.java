package org.dama.entity;


import java.util.Date;

public class Comment {
    private String game;

    private String player;

    private String comment;

    private Date commentedOn;

    public Comment(String game, String player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOndOn() {
        return commentedOn;
    }

    public void setCommentedOnOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return  player +
                " comment: " + comment +
                " " + commentedOn;
    }
}

