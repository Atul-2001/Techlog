package com.signature.techlog.model;

import com.signature.techlog.catalog.Tag;
import com.signature.techlog.model.base.TimestampEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable
@Table(name = "BLOGS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Blog extends TimestampEntity {

    @Transient
    private static final long serialVersionUID = 2L;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "SUBTITLE")
    private String subtitle;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "DOCUMENT_URL", nullable = false, updatable = false)
    private String documentURL;

    @NotNull
    @Column(name = "READ_TIME", nullable = false)
    private long readTime;

    @ElementCollection
    @Column(name = "TAG")
    @Enumerated(EnumType.STRING)
    @OrderBy(value = " TAG ASC ")
    @CollectionTable(name = "TAGS", joinColumns = @JoinColumn(name = "BLOG", referencedColumnName = "ID"))
    private Set<Tag> tags;

    @OneToMany(mappedBy = "BLOG")
    @OrderBy(value = " TIMESTAMP DESC ")
    private Set<Reaction> reactions;

    @OneToMany(mappedBy = "BLOG")
    @OrderBy(value = " TIMESTAMP DESC ")
    private Set<Comment> comments;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "USER", nullable = false, updatable = false)
    private User user;

    public Blog() {
        this.tags = new HashSet<>();
        this.reactions = new HashSet<>();
        this.comments = new HashSet<>();
    }

    @PrePersist
    private void updateDocumentURL() {
        this.documentURL = user.getHomeDirectory() +
                File.separator + "blogs" +
                File.separator + super.getId() + ".txt";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Blog blog = (Blog) obj;
        return Objects.equals(super.getId(), blog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }
}