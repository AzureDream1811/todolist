<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="addProjectModal" class="task-modal">
    <div class="task-modal-content">
        <form action="${pageContext.request.contextPath}/projects/add" method="post" id="projectForm">
            <div class="task-input-section">
                <input type="text"
                       id="projectName"
                       name="name"
                       class="input-title"
                       placeholder="Project name"
                       required
                       autofocus>
                <textarea name="description" class="input-desc" placeholder="Description"></textarea>
            </div>

            <div class="task-options-row">
                <div class="option-item">
                    <i class="fa-solid fa-palette"></i>
                    <span class="date-text">Color: Default</span>
                </div>
            </div>

            <div class="task-modal-footer">
                <button type="button" class="btn-cancel" onclick="closeAddProjectModal()">Cancel</button>
                <button type="submit" class="btn-add">Add project</button>
            </div>
        </form>
    </div>
</div>