package com.signature.techlog.model;

import com.signature.techlog.bootstrap.ApplicationContext;
import com.signature.techlog.catalog.AccountType;
import com.signature.techlog.catalog.ContactType;
import com.signature.techlog.catalog.Gender;
import com.signature.techlog.model.base.TimestampEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable
@Table(name = "USERS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends TimestampEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "HOME_DIRECTORY", nullable = false, updatable = false)
    private String homeDirectory;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "USERNAME", unique = true)
    private String username;

    @NotNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @NotNull
    @Column(name = "IS_EMAIL_VERIFIED", nullable = false)
    private boolean isEmailVerified;

    @NotNull
    @Column(name = "IS_EMAIL_PRIVATE", nullable = false)
    private boolean isEmailPrivate;

    @Column(name = "PASSWORD", length = 64)
    private String password;

    @Column(name = "PHONE", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 15)
    private Gender gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "COUNTRY", length = 100)
    private String country;

    @Column(name = "PROFILE")
    private String profile;

    @Column(name = "ABOUT")
    private String about;

    @ElementCollection
    @MapKeyColumn(name = "TYPE")
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(name = "CONTACTS", joinColumns = @JoinColumn(name = "USER", referencedColumnName = "ID"))
    @AttributeOverride(name = "value.String", column = @Column(name = "VALUE", nullable = false))
    private Map<ContactType, String> contacts;

    @ManyToMany
    @JoinTable(
            name = "RELATIONS",
            joinColumns = @JoinColumn(name = "FOLLOWER", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWING", referencedColumnName = "ID")
    )
    @OrderBy(value = " NAME ASC ")
    private Set<User> following;

    @ManyToMany(mappedBy = "FOLLOWING")
    @OrderBy(value = " NAME ASC ")
    private Set<User> followers;

    @OneToMany(mappedBy = "USER")
    @OrderBy(value = " TIMESTAMP DESC ")
    private Set<Blog> blogs;

    @OneToMany(mappedBy = "USER")
    @OrderBy(value = " TIMESTAMP DESC ")
    private Set<Reaction> reactions;

    @OneToMany(mappedBy = "USER")
    @OrderBy(value = " TIMESTAMP DESC ")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "USER")
    @OrderBy(value = " TIMESTAMP ASC ")
    private Set<Archive> archives;

    public User() {
        this.contacts = new HashMap<>();
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.blogs = new HashSet<>();
        this.reactions = new HashSet<>();
        this.comments = new HashSet<>();
        this.archives = new HashSet<>();
    }

    @PrePersist
    private void updateHomeDirectory() {
        this.homeDirectory = ApplicationContext.getProperties().getProperty("app.user.dir").concat(File.separator).concat(super.getId());
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isEmailPrivate() {
        return isEmailPrivate;
    }

    public void setEmailPrivate(boolean emailPrivate) {
        isEmailPrivate = emailPrivate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(Set<Blog> blogs) {
        this.blogs = blogs;
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

    public Set<Archive> getArchives() {
        return archives;
    }

    public void setArchives(Set<Archive> archives) {
        this.archives = archives;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(super.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    @Override
    public String toString() {
        return "User {" +
                "\n\tid: " + super.getId() +
                "\n\ttype: " + accountType.name() +
                "\n\tname: " + name +
                "\n\tusername: " + username +
                "\n\temail: " + email +
                "\n\tisEmailVerified: " + isEmailVerified +
                "\n\tisEmailPrivate: " + isEmailPrivate +
                "\n\tphone: " + phone +
                "\n\tgender: " + gender +
                "\n\tdateOfBirth: " + dateOfBirth +
                "\n\tcountry: " + country +
                "\n\tfollowers: " + followers.size() +
                "\n\tfollowing: " + following.size() +
                "\n\tblogs: " + blogs.size() +
                "\n\treactions: " + reactions.size() +
                "\n\tcomments: " + comments.size() +
                "\n\tcontentDirectory: " + homeDirectory +
                "\n\ttimestamp: " + super.getTimestamp() +
                "\n}";
    }
}