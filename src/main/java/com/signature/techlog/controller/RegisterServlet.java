package com.signature.techlog.controller;

import com.signature.techlog.data.UserHandler;
import com.signature.techlog.generator.SequenceGenerator;
import com.signature.techlog.generator.UsernameGenerator;
import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.util.PasswordAuthentication;
import com.signature.techlog.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

@WebServlet(name = "RegisterServlet", value = "/ajax/register")
public class RegisterServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String gender = req.getParameter("gender");
            String country = req.getParameter("country");
            String password = req.getParameter("password");

            if ((name == null || name.isEmpty())
                    || (email == null || email.isEmpty())
                    || (gender == null || gender.isEmpty())
                    || (country == null || country.isEmpty())
                    || (password == null || password.isEmpty())) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("All fields are required!")
                        .toJSON());
            } else if (!Validator.validateEmail(email)) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("Invalid email address!")
                        .toJSON());
            } else if (Validator.validateCountry(country)) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("Please select your country!")
                        .toJSON());
            } else if (Validator.validatePassword(password)) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("Password length should be between 8 to 20 characters. " +
                                "It should contain at least one uppercase letter(A-Z), " +
                                "one lowercase letter(a-z), one digit(0-9) and " +
                                "one special character.")
                        .toJSON());
            } else {
                UserHandler handler = UserHandler.getInstance();
                PasswordAuthentication authenticator = new PasswordAuthentication();

                String key = authenticator.hash(password.toCharArray());

                if (handler.getUser(email) == null) {

                    User user = new User(new BigInteger(String.valueOf(SequenceGenerator.getInstance().nextId())), name, email, key, gender, country);
                    try {
                        user.setUsername(UsernameGenerator.generate(email));
                        if (handler.saveUser(user)) {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.INFO)
                                    .setContent("Registration Successful! Please login...")
                                    .toJSON());
                        } else {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Something went wrong! Please try again!")
                                    .toJSON());
                        }
                    } catch (UsernameGenerator.InvalidEmailException ex) {
                        LOGGER.log(Level.ERROR, ex.getMessage());
                        out.print(Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Something went wrong! Please try again!")
                                .toJSON());
                    }
                } else {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("Email already exist, try another one!")
                            .toJSON());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }
}