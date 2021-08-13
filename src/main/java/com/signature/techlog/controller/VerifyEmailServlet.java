package com.signature.techlog.controller;

import com.signature.techlog.data.OTPHandler;
import com.signature.techlog.data.UserHandler;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.OTP;
import com.signature.techlog.model.User;
import com.signature.techlog.util.EmailContent;
import com.signature.techlog.util.MailUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EmailVerificationServlet", value = "/auth/verify/email")
public class VerifyEmailServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(VerifyEmailServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            String email = req.getParameter("email_field");
            String os = req.getParameter("os");
            String browser = req.getParameter("browser");
            String timestamp = req.getParameter("timestamp");

            if (os == null || os.isEmpty()) {
                os = "Unknown OS";
            }

            if (browser == null || browser.isEmpty()) {
                browser = "Unknown browser";
            }

            if (timestamp == null || timestamp.isEmpty()) {
                timestamp = Timestamp.from(Instant.now()).toString();
            }

            if (email == null || email.isEmpty()) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("All fields are required!")
                        .toJSON());
            } else {
                User user = UserHandler.getInstance().getUser(email);

                if (user == null) {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("That address is either invalid, not a verified primary email or is not associated with a personal user account.")
                            .toJSON());
                } else {
                    HttpSession session = getSession(req);
                    OTP otp = new OTP(session.getId(), user.getId(), user.getEmail());
                    OTPHandler otpHandler = new OTPHandler();

                    Map<String, String> data = new HashMap<>();
                    data.put("user", user.getName());
                    data.put("code", String.valueOf(otp.getCode()));
                    data.put("os", os);
                    data.put("browser", browser);
                    data.put("timestamp", timestamp);

                    try {
                        final String SUBJECT = "Account verification code";
                        MimeMessage message = MailUtil.createEmail(user.getEmail(), SUBJECT, EmailContent.getPasswordResetTemplate(data));
                        if (MailUtil.sendMessage(message) && otpHandler.saveOrUpdateOTP(otp)) {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.INFO)
                                    .setContent("OTP sent successfully!")
                                    .toJSON());
                        } else {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Failed to send OTP, please try again!")
                                    .toJSON());
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }

    public HttpSession getSession (HttpServletRequest request) {
        HttpSession oldSession = request.getSession();
        if (oldSession != null) {
            if (oldSession.getAttribute("user") != null) {
                return oldSession;
            } else {
                oldSession.invalidate();
                return request.getSession(true);
            }
        } else {
            return request.getSession(true);
        }
    }
}