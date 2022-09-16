package com.signature.techlog.model;

import com.signature.techlog.model.base.TimestampEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "ARCHIVES")
public class Archive extends TimestampEntity {

    @Transient
    private static final long serialVersionUID = 5L;

    @NotNull
    @Column(name = "NAME", nullable = false, updatable = false)
    private String name;

    @NotNull
    @Column(name = "SIZE", nullable = false, updatable = false)
    private int size;

    @NotNull
    @Column(name = "LOCATION", nullable = false, updatable = false)
    private String location;

    @NotNull
    @Column(name = "IS_EXPIRED", nullable = false)
    private boolean isExpired;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "USER", nullable = false, updatable = false)
    private User user;

    public Archive() { }

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
        return Objects.equals(super.getId(), archive.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    @Override
    public String toString() {
        return "Archive {" +
                "\n\tid: " + super.getId() +
                "\n\tname: " + name +
                "\n\tsize: " + size +
                "\n\tlocation: " + location +
                "\n\tdateCreated: " + super.getTimestamp() +
                "\n\tisExpired: " + isExpired +
                "\n}";
    }
}