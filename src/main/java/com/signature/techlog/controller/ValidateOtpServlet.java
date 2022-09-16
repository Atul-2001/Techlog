package com.signature.techlog.controller;

import com.signature.techlog.model.Message;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
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

@WebServlet(name = "OTPValidationServlet", value = "/auth/validate/otp")
public class ValidateOtpServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(ValidateOtpServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            String email = req.getParameter("email");
            String key = req.getParameter("otp_field");

            if (email == null || email.isEmpty()) {
                out.print(Message.builder()
                        .setLevel(Message.Level.ERROR)
                        .setContent("Invalid Request!")
                        .toJSON());
            }
            if ((key == null || key.isEmpty())) {
                out.print(Message.builder()
                        .setLevel(Message.Level.WARNING)
                        .setContent("All fields are required!")
                        .toJSON());
            } else {
                User user = UserRepository.getInstance().findByEmail(email);

                if (user == null) {
                    out.print(Message.builder()
                            .setLevel(Message.Level.ERROR)
                            .setContent("That address is either invalid, not a verified primary email or is not associated with a personal user account.")
                            .toJSON());
                } else {
                    HttpSession session = req.getSession(false);
                    if (session == null) {
                        out.print(Message.builder()
                                .setLevel(Message.Level.ERROR)
                                .setContent("Invalid request!")
                                .toJSON());
                    } else {
                        OTPRepository otpRepository = new OTPRepository();
                        OTP otp = otpRepository.getOTP(session.getId(), user);

                        if (otp.validate(Integer.parseInt(key))) {
                            otp.setValidated(true);
                            otpRepository.saveOrUpdateOTP(otp);
                            out.print(Message.builder()
                                    .setLevel(Message.Level.INFO)
                                    .setContent("Verification Successful!")
                                    .toJSON());
                        } else {
                            out.print(Message.builder()
                                    .setLevel(Message.Level.ERROR)
                                    .setContent("Verification failed! Try again...")
                                    .toJSON());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
        }
    }

}