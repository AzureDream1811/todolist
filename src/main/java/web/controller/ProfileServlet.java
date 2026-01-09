package web.controller;

import web.dao.DAOFactory;
import web.dao.UserDAO;
import web.model.User;
import web.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Servlet for handling user profile operations including avatar upload.
 */
@WebServlet("/app/profile/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 5,         // 5 MB
    maxRequestSize = 1024 * 1024 * 10      // 10 MB
)
public class ProfileServlet extends HttpServlet {
    private static final String PROFILE_PAGE = "/WEB-INF/views/app/Profile.jsp";
    private static final String UPLOAD_DIR = "uploads/avatars";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};

    private final DAOFactory factory = DAOFactory.getInstance();
    private final UserDAO userDAO = factory.getUserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        // Refresh user data from database
        User freshUser = userDAO.getUserById(currentUser.getId());
        request.getSession().setAttribute("currentUser", freshUser);
        
        request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        // For multipart forms, we need to check content type and get action from Part
        String action = null;
        String contentType = request.getContentType();
        
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            // Get action from form Part
            Part actionPart = request.getPart("action");
            if (actionPart != null) {
                action = new String(actionPart.getInputStream().readAllBytes()).trim();
            }
        } else {
            action = request.getParameter("action");
        }
        
        if ("uploadAvatar".equals(action)) {
            handleAvatarUpload(request, response, currentUser);
        } else if ("removeAvatar".equals(action)) {
            handleAvatarRemove(request, response, currentUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/profile");
        }
    }

    /**
     * Handles avatar image upload.
     */
    private void handleAvatarUpload(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            Part filePart = request.getPart("avatar");
            
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Please select an image file to upload.");
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            String fileName = getSubmittedFileName(filePart);
            String extension = getFileExtension(fileName).toLowerCase();

            // Validate file extension
            if (!isAllowedExtension(extension)) {
                request.setAttribute("error", "Invalid file type. Only JPG, PNG, GIF, and WebP are allowed.");
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            // Validate file size (additional check)
            if (filePart.getSize() > 5 * 1024 * 1024) {
                request.setAttribute("error", "File size must be less than 5MB.");
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
                return;
            }

            // Create upload directory if it doesn't exist
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Delete old avatar if exists
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                String oldAvatarPath = getServletContext().getRealPath("") + File.separator + currentUser.getAvatar();
                File oldFile = new File(oldAvatarPath);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // Generate unique filename
            String uniqueFileName = "avatar_" + currentUser.getId() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            Path filePath = Paths.get(uploadPath, uniqueFileName);

            // Save the file
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Update database with avatar path
            String avatarPath = UPLOAD_DIR + "/" + uniqueFileName;
            userDAO.updateAvatar(currentUser.getId(), avatarPath);

            // Refresh user from database and update session
            User updatedUser = userDAO.getUserById(currentUser.getId());
            request.getSession().setAttribute("currentUser", updatedUser);

            response.sendRedirect(request.getContextPath() + "/app/profile?success=avatar_updated");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error uploading file: " + e.getMessage());
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        }
    }

    /**
     * Handles avatar removal.
     */
    private void handleAvatarRemove(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            // Delete file if exists
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                String avatarPath = getServletContext().getRealPath("") + File.separator + currentUser.getAvatar();
                File avatarFile = new File(avatarPath);
                if (avatarFile.exists()) {
                    avatarFile.delete();
                }
            }

            // Update database
            userDAO.updateAvatar(currentUser.getId(), null);

            // Refresh user from database and update session
            User updatedUser = userDAO.getUserById(currentUser.getId());
            request.getSession().setAttribute("currentUser", updatedUser);

            response.sendRedirect(request.getContextPath() + "/app/profile?success=avatar_removed");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error removing avatar: " + e.getMessage());
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        }
    }

    /**
     * Gets the file extension from a filename.
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * Checks if the file extension is allowed.
     */
    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the submitted file name from the Part.
     */
    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
