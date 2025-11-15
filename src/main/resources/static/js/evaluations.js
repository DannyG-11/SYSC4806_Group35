(function ($) {
    'use strict';

    $(document).ready(function () {
        let allApplications = [];
        let currentApplication = null;

        // Load all applications on page load
        loadApplications();
        loadProfessors();

        // Event Handlers
        $('#statusFilter').on('change', filterApplications);
        $('#professorFilter').on('change', filterApplications);
        $('#closeModalBtn').on('click', closeViewModal);
        $('#closeStatusModalBtn').on('click', closeStatusModal);

        // Close modals when clicking overlay
        $('#viewModal, #statusModal').on('click', function (e) {
            if (e.target === this) {
                $(this).removeClass('active');
            }
        });

        // Modal action buttons
        $('#modalNotRecommendedBtn').on('click', function () {
            if (currentApplication) {
                updateApplicationStatus(currentApplication.id, 'NOT_RECOMMENDED');
            }
        });

        $('#modalNoSupervisionBtn').on('click', function () {
            if (currentApplication) {
                updateApplicationStatus(currentApplication.id, 'RECOMMENDED_NO_SUPERVISION');
            }
        });

        $('#modalNoFundingBtn').on('click', function () {
            if (currentApplication) {
                updateApplicationStatus(currentApplication.id, 'RECOMMENDED_NO_FUNDING');
            }
        });

        $('#modalWithFundingBtn').on('click', function () {
            if (currentApplication) {
                updateApplicationStatus(currentApplication.id, 'RECOMMENDED_WITH_FUNDING');
            }
        });

        $('#confirmStatusChangeBtn').on('click', function () {
            if (currentApplication) {
                const newStatus = $('#newStatusSelect').val();
                updateApplicationStatus(currentApplication.id, newStatus);
            }
        });

        // Load applications from API
        function loadApplications() {
            $.ajax({
                url: '/api/applications',
                method: 'GET',
                dataType: 'json',
                success: function (data) {
                    allApplications = data;
                    renderApplications(allApplications);
                },
                error: function (xhr, status, error) {
                    console.error('Error loading applications:', error);
                    $('#applicationsTableBody').html(`
                        <tr>
                            <td colspan="9">
                                <div class="empty-state">
                                    <h2>Error Loading Applications</h2>
                                    <p>Please try refreshing the page</p>
                                </div>
                            </td>
                        </tr>
                    `);
                }
            });
        }

        // Load professors from API and populate dropdown
        function loadProfessors() {
            $.ajax({
                url: '/professors',
                method: 'GET',
                dataType: 'json',
                success: function (data) {
                    console.log(data._embedded.professors);
                    populateProfessorFilter(data._embedded.professors);
                },
                error: function (xhr, status, error) {
                    console.error('Error loading professors:', error);
                }
            });
        }

        // Populate professor filter dropdown
        function populateProfessorFilter(professors) {
            const select = $('#professorFilter');

            // Clear existing options except the first one
            select.find('option:gt(0)').remove();

            // Sort professors by last name, then first name
            const sortedProfessors = professors.sort((a, b) => {
                const lastNameCompare = a.lastName.localeCompare(b.lastName);
                if (lastNameCompare !== 0) return lastNameCompare;
                return a.firstName.localeCompare(b.firstName);
            });

            // Add professor options
            $.each(sortedProfessors, function (index, prof) {
                const displayName = `${prof.lastName}, ${prof.firstName}`;
                const option = $('<option>')
                    .val(prof.id) // Store ID as value
                    .text(displayName); // Display name to user
                select.append(option);
            });
        }

        // Render applications in table
        function renderApplications(applications) {
            const tbody = $('#applicationsTableBody');
            tbody.empty();

            if (applications.length === 0) {
                tbody.html(`
                    <tr>
                        <td colspan="9">
                            <div class="empty-state">
                                <h2>No Applications Found</h2>
                                <p>There are no applications matching your criteria</p>
                            </div>
                        </td>
                    </tr>
                `);
                return;
            }

            $.each(applications, function (index, app) {
                const row = createApplicationRow(app);
                tbody.append(row);
            });
        }

        // Create table row for application
        function createApplicationRow(app) {
            const fullName = app.personalInfo.firstName + ' ' + app.personalInfo.lastName;
            const professorNames = app.professors && app.professors.length > 0
                ? app.professors.map(p => p.firstName + ' ' + p.lastName).join(', ')
                : 'None';
            const documentCount = app.documents ? app.documents.length : 0;
            const evaluationsCounts = app.evaluations ? app.evaluations.length : 0;

            const row = $('<tr>');

            row.append($('<td>').text(app.id));
            row.append($('<td>').text(fullName));
            row.append($('<td>').text(app.personalInfo.email));
            row.append($('<td>').text(app.fieldOfResearch || 'N/A'));
            row.append($('<td>').text(professorNames));
            row.append($('<td>').text(documentCount + ' doc(s)'));
            row.append($('<td>').text(evaluationsCounts + ' evaluation(s)'));
            row.append($('<td>').html(`<span class="status-badge status-${app.status}">${formatStatus(app.status)}</span>`));

            // Action buttons
            const actionsCell = $('<td>');
            const actionsDiv = $('<div>').addClass('action-buttons');

            const viewBtn = $('<button>')
                .addClass('btn btn-view')
                .text('View')
                .on('click', function () {
                    viewApplication(app);
                });

            const statusBtn = $('<button>')
                .addClass('btn btn-status')
                .text('Change Status')
                .on('click', function () {
                    openStatusModal(app);
                });

            actionsDiv.append(viewBtn, statusBtn);
            actionsCell.append(actionsDiv);
            row.append(actionsCell);

            return row;
        }

        // Format status for display
        function formatStatus(status) {
            const statusMap = {
                'PENDING': 'Pending',
                'NOT_RECOMMENDED': 'Not Recommended',
                'RECOMMENDED_NO_SUPERVISION': 'Rec. No Supervision',
                'RECOMMENDED_NO_FUNDING': 'Rec. No Funding',
                'RECOMMENDED_WITH_FUNDING': 'Rec. With Funding'
            };
            return statusMap[status] || status;
        }

        // View application details in modal
        function viewApplication(app) {
            currentApplication = app;

            const fullName = app.personalInfo.firstName + ' ' + app.personalInfo.lastName;

            $('#modalApplicantName').text(fullName);
            $('#modalAppId').text('Application #' + app.id);

            // Build modal body content
            let modalContent = `
                <div class="detail-section">
                    <div class="detail-grid">
                        <div class="detail-label">Current Status:</div>
                        <div class="detail-value">
                            <span class="status-badge status-${app.status}">${formatStatus(app.status)}</span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3>Personal Information</h3>
                    <div class="detail-grid">
                        <div class="detail-label">First Name:</div>
                        <div class="detail-value">${app.personalInfo.firstName}</div>

                        <div class="detail-label">Last Name:</div>
                        <div class="detail-value">${app.personalInfo.lastName}</div>

                        <div class="detail-label">Email:</div>
                        <div class="detail-value">${app.personalInfo.email}</div>

                        <div class="detail-label">Phone:</div>
                        <div class="detail-value">${app.personalInfo.phoneNumber}</div>

                        <div class="detail-label">Address:</div>
                        <div class="detail-value">${app.personalInfo.address}</div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3>Academic Information</h3>
                    <div class="detail-grid">
                        <div class="detail-label">Research Field:</div>
                        <div class="detail-value">${app.fieldOfResearch || 'N/A'}</div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3>Professors (${app.professors ? app.professors.length : 0})</h3>
            `;

            if (app.professors && app.professors.length > 0) {
                $.each(app.professors, function (index, prof) {
                    modalContent += `
                        <div class="professor-card">
                            <h4>${prof.firstName} ${prof.lastName}</h4>
                            <p><strong>Email:</strong> ${prof.email}</p>
                        </div>
                    `;
                });
            } else {
                modalContent += '<p style="color: #999;">No professors listed</p>';
            }

            modalContent += `
                </div>
                <div class="detail-section">
                    <h3>Documents (${app.documents ? app.documents.length : 0})</h3>
            `;

            if (app.documents && app.documents.length > 0) {
                $.each(app.documents, function (index, doc) {
                    modalContent += `
                        <div class="document-card">
                            <h4>${doc.title}</h4>
                            <p><strong>URL:</strong> <a href="${doc.link}" target="_blank">${doc.link}</a></p>
                        </div>
                    `;
                });
            } else {
                modalContent += '<p style="color: #999;">No documents submitted</p>';
            }

            if (app.evaluations && app.evaluations.length > 0) {
                $.each(app.evaluations, function (index, evalObj) {
                    modalContent += `
                        <div class="evaluation-card">
                            <h4>${evalObj.professor}</h4>
                            <p>Comments: ${evalObj.comments}</p>
                            <p>Recommendation: ${evalObj.recommendation} </p>
                        </div>
                    `;
                });
            } else {
                modalContent += '<p style="color: #999;">No evaluations submitted</p>';
            }

            modalContent += '</div>';

            $('#modalBody').html(modalContent);
            $('#viewModal').addClass('active');
        }

        // Open status change modal
        function openStatusModal(app) {
            currentApplication = app;
            $('#newStatusSelect').val(app.status);
            $('#statusModal').addClass('active');
        }

        // Close view modal
        function closeViewModal() {
            $('#viewModal').removeClass('active');
            currentApplication = null;
        }

        // Close status modal
        function closeStatusModal() {
            $('#statusModal').removeClass('active');
            currentApplication = null;
        }

        // Update application status
        function updateApplicationStatus(appId, newStatus) {
            const statusNames = {
                'NOT_RECOMMENDED': 'not recommend',
                'RECOMMENDED_NO_SUPERVISION': 'recommend without supervision',
                'RECOMMENDED_NO_FUNDING': 'recommend without funding',
                'RECOMMENDED_WITH_FUNDING': 'recommend with funding',
                'PENDING': 'set as pending'
            };

            const confirmMessage = `Are you sure you want to ${statusNames[newStatus]} this application?`;

            if (!confirm(confirmMessage)) {
                return;
            }

            $.ajax({
                url: `/api/applications/${appId}/status?status=${encodeURIComponent(newStatus)}`,
                method: 'PUT',
                success: function () {
                    // Reload applications to reflect changes
                    loadApplications();

                    // Close modals
                    closeViewModal();
                    closeStatusModal();

                    // Show success message
                    alert(`Application status updated to: ${formatStatus(newStatus)}`);
                },
                error: function (xhr, status, error) {
                    console.error('Error updating status:', error);
                    alert('Failed to update application status. Please try again.');
                }
            });
        }

        // Filter applications
        function filterApplications() {
            const selectedStatus = $('#statusFilter').val();
            const selectedProfessorId = $('#professorFilter').val();

            let filtered = allApplications;

            // Filter by status
            if (selectedStatus !== '') {
                filtered = filtered.filter(app => app.status === selectedStatus);
            }

            // Filter by professor ID (check if professor is assigned to the application)
            if (selectedProfessorId !== '') {
                const profId = parseInt(selectedProfessorId, 10);
                filtered = filtered.filter(app => {
                    return app.professors && app.professors.some(prof => prof.id === profId);
                });
            }

            renderApplications(filtered);
        }
    });
})(jQuery);
