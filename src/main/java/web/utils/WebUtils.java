package web.utils;


import web.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WebUtils {

    /**
     * Validates if the user is logged in and retrieves the current user session.
     * If the user is not logged in, it redirects them to the login page.
     *
     * @param request  the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @return the current user session if the user is logged in, null otherwise
     * @throws IOException if an exception occurs during the input/output operations
     */
    public static User validateAndGetUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        return currentUser;
    }

    /**
     * Validates that the current user is logged in and has admin role.
     * Returns the user if admin; otherwise sends 403 and returns null.
     */
    public static User validateAdminAndGetUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = validateAndGetUser(request, response);
        if (currentUser == null) return null;
        if (!currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return currentUser;
    }

    /**
     * Retrieves the action form path from the request.
     * If the path is null or empty, it redirects the request to the inbox page.
     * Otherwise, it returns the path substring from index 1 (to exclude the leading slash).
     *
     * @param request  the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @return the action form path if the path is not null or empty, null otherwise
     * @throws IOException if an exception occurs during the input/output operations
     */
    public static String getActionFormPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/app/inbox");
            return null;
        }

        return path.substring(1);
    }

    /**
     * Sends an error message to the client by setting the "error" attribute to the request and
     * redirecting the request to the specified redirect path.
     *
     * @param request  the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @param message the error message to send to the client
     * @param redirectPath the path to redirect the request to after sending the error message
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException if an exception occurs during the input/output operations
     */
    public static void sendError(HttpServletRequest request, HttpServletResponse response,
                          String message, String redirectPath)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher(redirectPath).forward(request, response);
    }
}