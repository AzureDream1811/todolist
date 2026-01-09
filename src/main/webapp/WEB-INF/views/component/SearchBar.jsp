<%--
  Global Search Bar Component
  Created: 09/01/2026
  Description: A search bar that can search for tasks and projects across the app
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="search-container">
    <form action="${pageContext.request.contextPath}/app/search" method="get" class="search-form" id="globalSearchForm">
        <div class="search-input-wrapper">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" 
                   name="q" 
                   id="globalSearchInput"
                   class="search-input" 
                   placeholder="Search tasks, projects..." 
                   value="${param.q}"
                   autocomplete="off">
            <button type="button" class="search-clear-btn" id="searchClearBtn" style="display: none;">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>
        <button type="submit" class="search-submit-btn">
            <span>Search</span>
        </button>
    </form>
    
    <!-- Quick search dropdown for instant results -->
    <div class="search-dropdown" id="searchDropdown">
        <div class="search-dropdown-content">
            <div class="search-section" id="taskResults">
                <div class="search-section-header">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Tasks</span>
                </div>
                <div class="search-section-items" id="taskResultsList">
                    <!-- Task results will be populated here -->
                </div>
            </div>
            <div class="search-section" id="projectResults">
                <div class="search-section-header">
                    <i class="fa-solid fa-folder"></i>
                    <span>Projects</span>
                </div>
                <div class="search-section-items" id="projectResultsList">
                    <!-- Project results will be populated here -->
                </div>
            </div>
            <div class="search-no-results" id="noResults" style="display: none;">
                <i class="fa-solid fa-face-frown"></i>
                <span>No results found</span>
            </div>
        </div>
        <div class="search-dropdown-footer">
            <span>Press Enter to see all results</span>
        </div>
    </div>
</div>

<script>
    (function() {
        const searchInput = document.getElementById('globalSearchInput');
        const searchDropdown = document.getElementById('searchDropdown');
        const clearBtn = document.getElementById('searchClearBtn');
        const taskResultsList = document.getElementById('taskResultsList');
        const projectResultsList = document.getElementById('projectResultsList');
        const noResults = document.getElementById('noResults');
        const taskResults = document.getElementById('taskResults');
        const projectResults = document.getElementById('projectResults');
        
        let searchTimeout;
        
        // Show/hide clear button based on input
        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            clearBtn.style.display = query.length > 0 ? 'flex' : 'none';
            
            // Debounce search
            clearTimeout(searchTimeout);
            if (query.length >= 2) {
                searchTimeout = setTimeout(() => performQuickSearch(query), 300);
            } else {
                hideDropdown();
            }
        });
        
        // Clear button functionality
        clearBtn.addEventListener('click', function() {
            searchInput.value = '';
            clearBtn.style.display = 'none';
            hideDropdown();
            searchInput.focus();
        });
        
        // Focus handling
        searchInput.addEventListener('focus', function() {
            if (this.value.trim().length >= 2) {
                searchDropdown.classList.add('show');
            }
        });
        
        // Click outside to close
        document.addEventListener('click', function(e) {
            if (!e.target.closest('.search-container')) {
                hideDropdown();
            }
        });
        
        // Keyboard navigation
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                hideDropdown();
                this.blur();
            }
        });
        
        function hideDropdown() {
            searchDropdown.classList.remove('show');
        }
        
        function performQuickSearch(query) {
            fetch('${pageContext.request.contextPath}/app/search?q=' + encodeURIComponent(query) + '&ajax=true')
                .then(response => response.json())
                .then(data => {
                    renderResults(data);
                    searchDropdown.classList.add('show');
                })
                .catch(error => {
                    console.error('Search error:', error);
                });
        }
        
        function renderResults(data) {
            const tasks = data.tasks || [];
            const projects = data.projects || [];
            
            // Clear previous results
            taskResultsList.innerHTML = '';
            projectResultsList.innerHTML = '';
            
            // Render tasks
            if (tasks.length > 0) {
                taskResults.style.display = 'block';
                tasks.slice(0, 5).forEach(task => {
                    const item = document.createElement('a');
                    item.href = '${pageContext.request.contextPath}/app/inbox?taskId=' + task.id;
                    item.className = 'search-result-item';
                    item.innerHTML = '<i class="fa-regular fa-circle"></i><span>' + escapeHtml(task.title) + '</span>';
                    taskResultsList.appendChild(item);
                });
            } else {
                taskResults.style.display = 'none';
            }
            
            // Render projects
            if (projects.length > 0) {
                projectResults.style.display = 'block';
                projects.slice(0, 5).forEach(project => {
                    const item = document.createElement('a');
                    item.href = '${pageContext.request.contextPath}/app/projects?id=' + project.id;
                    item.className = 'search-result-item';
                    item.innerHTML = '<i class="fa-solid fa-hashtag"></i><span>' + escapeHtml(project.name) + '</span>';
                    projectResultsList.appendChild(item);
                });
            } else {
                projectResults.style.display = 'none';
            }
            
            // Show no results message
            if (tasks.length === 0 && projects.length === 0) {
                noResults.style.display = 'flex';
                taskResults.style.display = 'none';
                projectResults.style.display = 'none';
            } else {
                noResults.style.display = 'none';
            }
        }
        
        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }
    })();
</script>
