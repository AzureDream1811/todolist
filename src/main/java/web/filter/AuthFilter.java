package web.filter;

import web.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication Filter - Kiểm tra đăng nhập tập trung cho toàn bộ ứng dụng.
 * Thay vì check trong mỗi Servlet, Filter sẽ chặn request trước khi đến Servlet.
 */
@WebFilter(urlPatterns = {"/app/*", "/tasks/*", "/projects/*", "/admin/*"})
public class AuthFilter implements Filter {

    // Các URL công khai không cần đăng nhập
    private static final String[] PUBLIC_PATHS = {
        "/auth/",
        "/static/",
        "/index.jsp"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        // Bỏ qua các URL công khai
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Kiểm tra session và user
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        
        if (currentUser == null) {
            // Chưa đăng nhập → redirect về trang login
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Kiểm tra quyền admin cho /admin/*
        if (path.startsWith("/admin") && !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied - Admin only");
            return;
        }
        
        // Đã đăng nhập → cho phép tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Dọn dẹp resources (nếu cần)
    }

    /**
     * Kiểm tra path có phải là public không.
     */
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
}
