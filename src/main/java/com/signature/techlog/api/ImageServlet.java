package com.signature.techlog.api;

import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.repository.impl.UserRepositoryImpl;
import com.signature.techlog.util.ApplicationProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@WebServlet(name = "User Profile Servlet", value = "/user/profile/*")
public class ImageServlet extends HttpServlet {

    private final Logger LOGGER = LogManager.getLogger(ImageServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            User user = (User) session.getAttribute("user");
            UserRepository handler = null/*UserRepositoryImpl.getInstance()*/;

            if (handler.findById(user.getId()) == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                try {
//                    System.out.println(URLDecoder.decode(request.getPathInfo().substring(1), StandardCharsets.UTF_8));
                    String filepath = user.getProfile();
                    if (filepath.startsWith("http")) {
                        new URL(filepath).openConnection().getInputStream().transferTo(response.getOutputStream());
                    } else {
                        if (filepath.startsWith("app.user.profile")) {
                            filepath = ApplicationProperties.getProperty("app.dir").concat(ApplicationProperties.getProperty(filepath));
                        }
                        File file = new File(filepath);
                        response.setHeader("Content-Type", getServletContext().getMimeType(filepath));
                        response.setHeader("Content-Length", String.valueOf(file.length()));
                        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                        Files.copy(file.toPath(), response.getOutputStream());
                    }
                } catch (NullPointerException | IOException ex) {
                    LOGGER.log(Level.DEBUG, ex.getMessage(), ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        }
    }
}