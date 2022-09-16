package com.signature.techlog.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigInteger;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(BigInteger id) {
        this.id = id.toString();
    }
}