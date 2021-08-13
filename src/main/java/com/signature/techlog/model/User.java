package com.signature.techlog.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Entity
public class User implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "PROFILE")
    private String profile;

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

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE", length = 16)
    private String phone;

    @Column(name = "GENDER", length = 20)
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "COUNTRY", length = 50)
    private String country;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Column(name = "PROFESSION_OR_SKILL")
    @CollectionTable(
            name = "User_ProfessionOrSkills",
            joinColumns = @JoinColumn(name = "USER")
    )
    private List<String> professionOrSkills;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Column(name = "CONTACT")
    @CollectionTable(
            name = "USER_CONTACTS",
            joinColumns = @JoinColumn(name = "USER")
    )
    private Map<ContactType, String> contacts;

    @Column(name = "ABOUT")
    private String about;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_BLOG",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "BLOG")
    )
    private List<Blog> blogs;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_FOLLOWER",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWER")
    )
    private List<User> followers;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_FOLLOWING",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWED")
    )
    private List<User> following;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_REACTION",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "REACTION")
    )
    private List<Reaction> reactions;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_COMMENT",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "COMMENT")
    )
    private List<Comment> comments;

    @NotNull
    @Column(name = "CONTENT_DIRECTORY", nullable = false)
    private String contentDirectory;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "USER_ARCHIVE",
            joinColumns = @JoinColumn(name = "USER"),
            inverseJoinColumns = @JoinColumn(name = "ARCHIVE")
    )
    private List<Archive> requestedDataArchives;

    @NotNull
    @Column(name = "TIMESTAMP", nullable = false)
    private final Timestamp timestamp;

    public enum ContactType {
        YOUTUBE,
        FACEBOOK,
        INSTAGRAM,
        TWITTER
    }

    public User() {
        this.professionOrSkills = new ArrayList<>();
        this.contacts = new HashMap<>();
        this.blogs = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.contentDirectory = System.getProperty("user.home")
                .concat(File.separator).concat("Techlog")
                .concat(File.separator).concat("user")
                .concat(File.separator).concat(String.valueOf(id))
                .concat(File.separator);
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.requestedDataArchives = new ArrayList<>();
        this.timestamp = Timestamp.from(Instant.now());
    }

    public User(BigInteger id, String profile, String name, String email, String password, String phone, String gender, Date dateOfBirth, String country, String about) {
        this.id = id.toString();
        switch (gender) {
            case "FEMALE":
                this.profile = "/assets/images/user_icon/female-user.svg";
                break;
            case "MALE":
                this.profile = "/assets/images/user_icon/male-user.svg";
                break;
            case "TRANSGENDER":
                this.profile = "/assets/images/user_icon/transgender-user.svg";
                break;
            default:
                this.profile = profile;
                break;
        }
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.professionOrSkills = new ArrayList<>();
        this.contacts = new HashMap<>();
        this.about = about;
        this.blogs = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.contentDirectory = System.getProperty("user.home")
                .concat(File.separator).concat("Techlog")
                .concat(File.separator).concat("user")
                .concat(File.separator).concat(String.valueOf(id))
                .concat(File.separator);
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.requestedDataArchives = new ArrayList<>();
        this.timestamp = Timestamp.from(Instant.now());
    }

    public User(BigInteger id, String name, String email, String password, String gender, String country) {
        this(id, null, name, email, password, null, gender, null, country, null);
    }

    public User(BigInteger id, String name, String email, String profile) {
        this(id, profile, name, email, null, null, "", null, null, null);
    }

    public User(BigInteger id, String name, String profile) {
        this(id, name, null, profile);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.contentDirectory = System.getProperty("user.home")
                .concat(File.separator).concat("Techlog")
                .concat(File.separator).concat("user")
                .concat(File.separator).concat(id)
                .concat(File.separator);
    }

    public void setId(BigInteger id) {
        this.id = id.toString();
        this.contentDirectory = System.getProperty("user.home")
                .concat(File.separator).concat("Techlog")
                .concat(File.separator).concat("user")
                .concat(File.separator).concat(String.valueOf(id))
                .concat(File.separator);
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getProfessionOrSkills() {
        return professionOrSkills;
    }

    public void setProfessionOrSkills(List<String> professionOrSkills) {
        this.professionOrSkills = professionOrSkills;
    }

    public void addProfessionOrSkills(String professionOrSkill) {
        this.professionOrSkills.add(professionOrSkill);
    }

    public void removeProfessionOrSkill(String professionOrSkill) {
        this.professionOrSkills.remove(professionOrSkill);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public String getContact(ContactType key) {
        return this.contacts.get(key);
    }

    public void addContact(ContactType key, String contact) {
        this.contacts.put(key, contact);
    }

    public void removeContact(ContactType key) {
        this.contacts.remove(key);
    }

    public void replaceContact(ContactType key, String newContact) {
        this.contacts.replace(key, newContact);
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public void addBlog(Blog blog) {
        this.blogs.add(blog);
    }

    public void removeBlog(Blog blog) {
        this.blogs.remove(blog);
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User follower) {
        this.followers.add(follower);
    }

    public void removeFollower(User follower) {
        this.followers.remove(follower);
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public void addFollowing(User user) {
        this.following.add(user);
    }

    public void removeFollowing(User user) {
        this.following.remove(user);
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

    public String getContentDirectory() {
        return contentDirectory;
    }

    public List<Archive> getRequestedDataArchives() {
        return requestedDataArchives;
    }

    public void setRequestedDataArchives(List<Archive> requestedDataArchives) {
        this.requestedDataArchives = requestedDataArchives;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User {" +
                "\n\tid='" + id + '\'' +
                "\n\tname='" + name + '\'' +
                "\n\tusername='" + username + '\'' +
                "\n\temail='" + email + '\'' +
                "\n\tisEmailVerified=" + isEmailVerified +
                "\n\tisEmailPrivate=" + isEmailPrivate +
                "\n\tphone='" + phone + '\'' +
                "\n\tgender='" + gender + '\'' +
                "\n\tdateOfBirth=" + dateOfBirth +
                "\n\tcountry='" + country + '\'' +
                "\n\tblogs=" + blogs.size() +
                "\n\tfollowers=" + followers.size() +
                "\n\tfollowing=" + following.size() +
                "\n\treactions=" + reactions.size() +
                "\n\tcomments=" + comments.size() +
                "\n\tcontentDirectory='" + contentDirectory + '\'' +
                "\n\ttimestamp=" + timestamp +
                "\n}";
    }
}