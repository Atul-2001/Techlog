package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Archive implements Serializable {

    @Transient
    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "SIZE", nullable = false)
    private int size;

    @NotNull
    @Column(name = "LOCATION", nullable = false)
    private String location;

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    private Timestamp dateCreated;

    @NotNull
    @Column(name = "IS_EXPIRED", nullable = false)
    private boolean isExpired;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER", nullable = false)
    private User user;

    public Archive() {}

    public Archive(String name, int size, String location) {
        this.name = name;
        this.size = size;
        this.location = location;
        this.dateCreated = Timestamp.from(Instant.now());
        this.isExpired = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archive archive = (Archive) o;
        return id == archive.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Archive {" +
                "\n\tid=" + id +
                "\n\tname='" + name + '\'' +
                "\n\tsize=" + size +
                "\n\tlocation='" + location + '\'' +
                "\n\tdateCreated=" + dateCreated +
                "\n\tisExpired=" + isExpired +
                "\n}";
    }
}