package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class Reaction implements Serializable {

    @Transient
    private static final long serialVersionUID = 3L;

    @Transient
    private static long REACTION_COUNT = 1;

    @Id
    @Column(name = "REACTION_ID")
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "BLOG", nullable = false)
    private Blog blog;

    @NotNull
    @Column(name = "REACTION", nullable = false)
    private React reaction;

    @NotNull
    @Column(name = "REACTION_TIME", nullable = false)
    private final Timestamp reactionTime;

    public enum React {
        UNDEFINED,
        LIKE,
        DISLIKE
    }

    public Reaction() {
        this.reaction = React.UNDEFINED;
        this.reactionTime = Timestamp.from(Instant.now());
    }

    public Reaction(User user, Blog blog, React reaction) {
        this.id = REACTION_COUNT++;
        this.user = user;
        this.blog = blog;
        this.reaction = reaction;
        this.reactionTime = Timestamp.from(Instant.now());
    }

    public static void setReactionCount(long count) {
        Reaction.REACTION_COUNT = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public React getReaction() {
        return reaction;
    }

    public void setReaction(React reaction) {
        this.reaction = reaction;
    }

    public Timestamp getReactionTime() {
        return reactionTime;
    }
}