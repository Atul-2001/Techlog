package com.signature.techlog.controller;

import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

@WebServlet(name = "Update Profile Servlet",
        value = {"/user/update/profile",
                "/user/update/email_status",
                "/settings/reset_avatar",
                "/user/update/avatar"})
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024, maxRequestSize = 1024 * 1024 * 5) // System.getProperty("java.io.tmpdir")
public class UpdateProfileServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(UpdateProfileServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            switch (req.getServletPath()) {
                case "/user/update/profile":
                    out.print(updateUserProfile(req, resp));
                    break;
                case "/user/update/email_status":
                    out.print(updateUserEmailStatus(req, resp));
                    break;
                case "/settings/reset_avatar":
                    out.print(resetUserAvatar(req, resp));
                    break;
                case "/user/update/avatar":
                    out.print(updateUserAvatar(req, resp));
                    break;
                default:
                    out.print(Message.builder().setLevel(Message.Level.ERROR).setContent("Request URI not found : " + req.getRequestURI() + " : " + req.getRequestURL() + " : " + req.getServletPath()).toJSON());
                    break;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String updateUserProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String name = req.getParameter("user[profile_name]");
        String email = req.getParameter("user[profile_email]");
        String emailVisibility = req.getParameter("user[email_visibility]");
        String phone = req.getParameter("user[profile_phone]");
        String gender = req.getParameter("user[profile_gender]");
        String dob = req.getParameter("user[profile_dob]");
        String bio = req.getParameter("user[profile_bio]");
        String country = req.getParameter("user[profile_country]");
        String youtube = req.getParameter("user[profile_contact_youtube]");
        String facebook = req.getParameter("user[profile_contact_facebook]");
        String instagram = req.getParameter("user[profile_contact_instagram]");
        String twitter = req.getParameter("user[profile_contact_twitter]");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return Message.builder()
                    .setLevel(Message.Level.ERROR)
                    .setContent("Invalid Request!")
                    .toJSON();
        } else {
            User user = (User) session.getAttribute("user");
            UserRepository handler = UserRepository.getInstance();

            if (handler.findById(user.getId()) != null) {
                if (name == null || name.isEmpty()) {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Name cannot be empty!")
                            .toJSON();
                } else {
                    user.setName(name);
                }

                if (email == null || email.isEmpty()) {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Email cannot be empty!")
                            .toJSON();
                } else if (!Validator.validateEmail(email)) {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Invalid email address!")
                            .toJSON();
                } else {
                    user.setEmail(email);
                }

                if (emailVisibility != null) {
                    user.setEmailPrivate(Boolean.TRUE);
                } else {
                    user.setEmailPrivate(Boolean.FALSE);
                }

                if (phone != null) {
                    if (!Validator.validatePhone(phone)) {
                        return Message.builder()
                                .setLevel(Message.Level.WARNING)
                                .setContent("Invalid email address!")
                                .toJSON();
                    } else {
                        user.setPhone(phone);
                    }
                } else {
                    user.setPhone(null);
                }

                if (gender == null || gender.isEmpty()) {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Please select your gender!")
                            .toJSON();
                } else {
                    user.setGender(gender);
                }

                if (dob == null) {
                    user.setDateOfBirth(null);
                } else {
                    user.setDateOfBirth(Date.valueOf(dob));
                }

                user.setAbout(bio);

                if (country == null || country.isEmpty()) {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Please select your country!")
                            .toJSON();
                } else {
                    user.setCountry(country);
                }

                if (youtube != null) {
                    user.addContact(User.ContactType.YOUTUBE, youtube);
                } else {
                    user.removeContact(User.ContactType.YOUTUBE);
                }

                if (facebook != null) {
                    user.addContact(User.ContactType.FACEBOOK, facebook);
                } else {
                    user.removeContact(User.ContactType.FACEBOOK);
                }

                if (instagram != null) {
                    user.addContact(User.ContactType.INSTAGRAM, instagram);
                } else {
                    user.removeContact(User.ContactType.INSTAGRAM);
                }

                if (twitter != null) {
                    user.addContact(User.ContactType.TWITTER, facebook);
                } else {
                    user.removeContact(User.ContactType.TWITTER);
                }

                if (handler.update(user)) {
                    session.setAttribute("user", user);
                    return Message.builder()
                            .setLevel(Message.Level.INFO)
                            .setContent("Profile updated successfully — <a onclick='messageModal.hide();' href='/" + user.getUsername() + "'>view your profile.</a>")
                            .toJSON();
                } else {
                    return Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Failed to update profile!")
                            .toJSON();
                }
            } else {
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid user request!")
                        .toJSON();
            }
        }
    }

    private String updateUserEmailStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return Message.builder()
                    .setLevel(Message.Level.ERROR)
                    .setContent("Invalid Request!")
                    .toJSON();
        } else {
            User sessionUser = (User) session.getAttribute("user");
            UserRepository handler = UserRepository.getInstance();

            User existingUser = handler.findById(sessionUser.getId());
            if (existingUser == null) {
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid user request!")
                        .toJSON();
            } else {
                if (!existingUser.isEmailVerified() && !sessionUser.isEmailVerified()) {
                    existingUser.setEmailVerified(Boolean.TRUE);
                    if (handler.update(existingUser)) {
                        sessionUser.setEmailVerified(Boolean.TRUE);
                        session.setAttribute("user", sessionUser);
                        return Message.builder()
                                .setLevel(Message.Level.INFO)
                                .setContent("Email verification status updated successfully.")
                                .toJSON();
                    } else {
                        return Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Failed to update email verification status!")
                                .toJSON();
                    }
                } else {
                    return Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Invalid user request!")
                            .toJSON();
                }
            }
        }
    }

    private String resetUserAvatar(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return Message.builder()
                    .setLevel(Message.Level.ERROR)
                    .setContent("Invalid Request!")
                    .toJSON();
        } else {
            User user = (User) session.getAttribute("user");
            UserRepository handler = UserRepository.getInstance();

            if (handler.findById(user.getId()) == null) {
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid user request!")
                        .toJSON();
            } else {
                File oldProfile = null;
                if (!user.getProfile().startsWith("http")) {
                    oldProfile = new File(user.getProfile());
                }
                switch (user.getGender()) {
                    case "FEMALE":
                        user.setProfile("app.user.profile.female");
                        break;
                    case "MALE":
                        user.setProfile("app.user.profile.male");
                        break;
                    default:
                        user.setProfile("app.user.profile.transgender");
                        break;
                }

                if (handler.update(user)) {
                    if (oldProfile != null) {
                        if (oldProfile.exists()) {
                            if (oldProfile.delete()) {
                                LOGGER.info("File {0} successfully deleted", oldProfile.getAbsolutePath());
                            } else {
                                LOGGER.info("Failed to delete file {0}", oldProfile.getAbsolutePath());
                            }
                        } else {
                            LOGGER.info("File {0} does not exist!", oldProfile.getAbsolutePath());
                        }
                    }
                    session.setAttribute("user", user);
                    return Message.builder()
                            .setLevel(Message.Level.INFO)
                            .setContent("Your profile picture has been reset. It may take a few moments to update across the site.")
                            .toJSON();
                } else {
                    return Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Failed to reset your profile picture!")
                            .toJSON();
                }
            }
        }
    }

    private String updateUserAvatar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part profile = req.getPart("user[profile_avatar]");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return Message.builder()
                    .setLevel(Message.Level.ERROR)
                    .setContent("Invalid Request!")
                    .toJSON();
        } else {
            User user = (User) session.getAttribute("user");
            UserRepository handler = UserRepository.getInstance();

            if (handler.findById(user.getId()) == null) {
                return Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid user request!")
                        .toJSON();
            } else {
                if (profile != null) {
                    try {
                        File avatarDir = new File(user.getHomeDirectory() + File.separator + "avatar");
                        if (avatarDir.exists()) {
                            new File(user.getProfile()).deleteOnExit();
                        } else {
                            avatarDir.mkdirs();
                        }

                        String avatarPath = avatarDir + File.separator + getFileName(profile);
                        profile.write(avatarPath);

                        user.setProfile(avatarPath);
                        if (handler.update(user)) {
                            session.setAttribute("user", user);
                            return Message.builder()
                                    .setLevel(Message.Level.INFO)
                                    .setContent("Your profile picture has been updated. It may take a few moments to update across the site.")
                                    .toJSON();
                        } else {
                            return Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Failed to update your profile picture!")
                                    .toJSON();
                        }
                    } catch (IOException ex) {
                        LOGGER.log(Level.DEBUG, ex.getMessage(), ex);
                        return Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Failed to update your profile picture!")
                                .toJSON();
                    }
                } else {
                    return Message.builder()
                            .setLevel(Message.Level.WARNING)
                            .setContent("Please select your profile picture to update!")
                            .toJSON();
                }
            }
        }
    }

    private String getFileName(Part part) {
        String fileName = part.getSubmittedFileName();
        String[] fileNamePart = fileName.split("\\.");
        String fileExt = fileNamePart[fileNamePart.length - 1];
        return Integer.toUnsignedString(fileName.hashCode()) + "." + fileExt;
    }

}