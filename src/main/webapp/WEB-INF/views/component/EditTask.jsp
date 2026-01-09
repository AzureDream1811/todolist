<%--
  Edit Task Modal Component
  Similar styling to AddTask.jsp for consistency
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="editTaskModal" class="modal-overlay">
    <div class="modal-box">
        <div class="task-modal-content">
            <div class="modal-header">
                <h3 class="modal-title">Edit Task</h3>
                <button type="button" class="modal-close-btn" onclick="closeEditTaskModal()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            
            <form id="editTaskForm" method="post">
                <input type="hidden" name="id" id="editTaskId">
                
                <div class="task-input-section">
                    <input type="text" name="title" id="editTaskTitle" class="input-title" placeholder="Task name" required>
                    <textarea name="description" id="editTaskDescription" class="input-desc" placeholder="Description"></textarea>
                </div>

                <div class="task-options-row">
                    <%-- Due Date --%>
                    <div class="option-item">
                        <i class="fa-regular fa-calendar"></i>
                        <input type="date" name="dueDate" id="editTaskDueDate" class="inline-date-picker">
                    </div>

                    <%-- Priority --%>
                    <div class="option-item">
                        <i class="fa-solid fa-flag"></i>
                        <select name="priority" id="editTaskPriority" class="inline-select">
                            <option value="1">Priority 1</option>
                            <option value="2">Priority 2</option>
                            <option value="3">Priority 3</option>
                        </select>
                    </div>

                    <%-- Project --%>
                    <div class="option-item">
                        <i class="fa-solid fa-list-ul"></i>
                        <select name="projectId" id="editTaskProject" class="inline-select">
                            <option value="0">Inbox</option>
                            <c:forEach var="project" items="${requestScope.projects}">
                                <option value="${project.id}">${project.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="task-modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeEditTaskModal()">Cancel</button>
                    <button type="submit" class="btn-add">Save changes</button>
                </div>
                
                <input type="hidden" name="redirect" id="editTaskRedirect">
            </form>
        </div>
    </div>
</div>

<script>
    // Open edit modal and populate with task data
    function openEditTaskModal(taskId, title, description, dueDate, priority, projectId) {
        document.getElementById('editTaskId').value = taskId;
        document.getElementById('editTaskTitle').value = title || '';
        document.getElementById('editTaskDescription').value = description || '';
        document.getElementById('editTaskDueDate').value = dueDate || '';
        document.getElementById('editTaskPriority').value = priority || '3';
        document.getElementById('editTaskProject').value = projectId || '0';
        document.getElementById('editTaskRedirect').value = window.location.pathname;
        
        document.getElementById('editTaskModal').style.display = 'flex';
        document.getElementById('editTaskTitle').focus();
    }

    // Close edit modal
    function closeEditTaskModal() {
        document.getElementById('editTaskModal').style.display = 'none';
    }

    // Handle form submission
    document.getElementById('editTaskForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const params = new URLSearchParams();
        formData.forEach((value, key) => params.append(key, value));

        fetch('${pageContext.request.contextPath}/tasks/update', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        })
        .then(response => {
            if (response.ok) {
                closeEditTaskModal();
                location.reload();
            } else {
                alert('Failed to update task. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error updating task.');
        });
    });

    // Close modal when clicking outside
    document.getElementById('editTaskModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeEditTaskModal();
        }
    });
</script>
