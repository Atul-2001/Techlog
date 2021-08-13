package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Blog implements Serializable {

    @Transient
    private static final long serialVersionUID = 2L;

    @Transient
    private static int BLOG_COUNT = 1;

    @Id
    @Column(name = "BLOG_ID")
    private int id;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @NotNull
    @Column(name = "CONTENT_URL", nullable = false)
    private final String contentURL;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Column(name = "TAG")
    @CollectionTable(
            name = "BLOG_TAGS",
            joinColumns = @JoinColumn(name = "BLOG")
    )
    private List<String> tags;

    @NotNull
    @Column(name = "CREATED_DATE", nullable = false)
    private final Timestamp createdDate;

    @NotNull
    @Column(name = "READ_TIME", nullable = false)
    private long readTime;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "BLOG_REACTION",
            joinColumns = @JoinColumn(name = "BLOG"),
            inverseJoinColumns = @JoinColumn(name = "REACTION")
    )
    private List<Reaction> reactions;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "BLOG_COMMENT",
            joinColumns = @JoinColumn(name = "BLOG"),
            inverseJoinColumns = @JoinColumn(name = "COMMENT")
    )
    private List<Comment> comments;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER", nullable = false)
    private User user;

    public Blog() {
        this.contentURL = user.getContentDirectory().concat(String.valueOf(id).concat(".txt"));
        this.tags = new ArrayList<>();
        this.createdDate = Timestamp.from(Instant.now());
        this.readTime = 0L;
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Blog(String title, String thumbnail, List<String> tags, User user) {
        this.id = BLOG_COUNT++;
        this.title = title;
        this.thumbnail = thumbnail;
        this.contentURL = user.getContentDirectory().concat(String.valueOf(id).concat(".txt"));
        this.tags = tags;
        this.createdDate = Timestamp.from(Instant.now());
        this.readTime = 0L;
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.user = user;
    }

    public static void setBlogCount(int count) {
        Blog.BLOG_COUNT = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContentURL() {
        return contentURL;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
    }

    public void removeReaction(Reaction reaction) {
        this.reactions.remove(reaction);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}