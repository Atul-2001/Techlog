package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Comment implements Serializable {

    @Transient
    private static final long serialVersionUID = 4L;

    @Transient
    private static long COMMENT_COUNT = 1;

    @Id
    @Column(name = "COMMENT_ID")
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
    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @NotNull
    @Column(name = "COMMENT_TIME", nullable = false)
    private final Timestamp commentTime;

    public Comment() {
        this.commentTime = Timestamp.from(Instant.now());
    }

    public Comment(User user, Blog blog, String comment) {
        this.id = COMMENT_COUNT++;
        this.user = user;
        this.blog = blog;
        this.comment = comment;
        this.commentTime = Timestamp.from(Instant.now());
    }

    public static void setCommentCount(long count) {
        Comment.COMMENT_COUNT = count;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return user.equals(comment.user) && blog.equals(comment.blog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, blog);
    }

}