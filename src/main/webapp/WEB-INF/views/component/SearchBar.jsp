<%--
  Search Modal Component
  Phong cách thiết kế đồng bộ với addTask.jsp
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="searchModal" class="task-modal">
    <div class="task-modal-content">
        <form action="${pageContext.request.contextPath}/app/search" method="get" id="searchForm">

            <div class="task-input-section">
                <div class="search-input-wrapper" style="display: flex; align-items: center; gap: 10px;">
                    <i class="fa-solid fa-magnifying-glass" style="color: #666;"></i>
                    <input type="text"
                           name="q"
                           class="input-title"
                           placeholder="Search tasks, projects..."
                           required
                           value="${param.q}">
                </div>
            </div>

            <div class="task-options-row">
                <div class="option-item">
                    <span style="font-size: 12px; color: #888;">
                        Press <kbd style="background: #eee; padding: 2px 4px; border-radius: 3px;">Enter</kbd> to find results
                    </span>
                </div>
            </div>

            <div class="task-modal-footer">
                <button type="button" class="btn-cancel" onclick="closeSearchModal()">Cancel</button>
                <button type="submit" class="btn-add">Search</button>
            </div>

        </form>
    </div>
</div>