/**
 * Application Review Dashboard Script
 * ------------------------------------
 * This script handles the front-end logic for displaying, filtering,
 * and reviewing student applications. It supports the following workflow:
 *   1. Fetch all applications from the backend.
 *   2. Filter applications by status (NEW, PENDING, EVALUATED, ACCEPTED, REJECTED).
 *   3. Display details and allow reviewers to take actions:
 *        - Send for evaluation
 *        - Approve / Reject applications
 *        - View submitted evaluations
 *        - View accepted/rejected applications
 */

document.addEventListener("DOMContentLoaded", async () => {

    /* ===============================
       DOM ELEMENT REFERENCES
       =============================== */
    const tableBody = document.querySelector("tbody");
    const popup = document.getElementById("reviewPopup");

    // Message modal elements
    const messageModal = document.getElementById("messageModal");
    const messageBox = document.getElementById("messageBox");
    const messageText = document.getElementById("messageText");
    const messageClose = document.getElementById("messageClose");

    // Application filter dropdown
    const statusFilter = document.getElementById("statusFilter");

    // Cached list of applications from the backend
    let applications = [];

    /* ===============================
       MESSAGE HANDLING
       =============================== */

    /**
     * Displays a temporary message modal (success or error).
     * @param {string} text - The message to display.
     * @param {"success"|"error"} [type="success"] - Message type.
     */
    function showMessage(text, type = "success") {
        messageBox.className = type === "error" ? "error" : "success";
        messageText.textContent = text;
        messageModal.style.display = "flex";
    }

    // Closes the message modal and reloads the page
    messageClose.addEventListener("click", () => {
        messageModal.style.display = "none";
        location.reload();
    });

    /* ===============================
       INITIAL DATA FETCH
       =============================== */

    try {
        const res = await fetch("/api/applications");
        applications = await res.json();
    } catch (err) {
        console.error("Error fetching applications:", err);
        showMessage("⚠️ Failed to load applications.", "error");
        return;
    }

    // Display default view (ALL applications)
    renderTable("ALL");

    // Filter change handler
    statusFilter.addEventListener("change", () => {
        renderTable(statusFilter.value);
    });

    /* ===============================
       APPLICATION STATUS LOGIC
       =============================== */

    /**
     * Determines the current status of an application.
     * @param {Object} app - The application object.
     * @returns {"NEW"|"PENDING"|"EVALUATED"}
     */
    function getStatus(app) {
        switch (app.status) {
            case "NEW":
                return "NEW";
            case "PENDING":
                return "PENDING";
            case "NOT_RECOMMENDED":
            case "RECOMMENDED_NO_SUPERVISION":
            case "RECOMMENDED_NO_FUNDING":
            case "RECOMMENDED_WITH_FUNDING":
                return "EVALUATED";
            case "ACCEPTED":
                return "ACCEPTED";
            case "REJECTED":
                return "REJECTED";
            default:
                return "INVALID";
        }
    }

    /* ===============================
       TABLE RENDERING
       =============================== */

    /**
     * Renders the applications table based on a status filter.
     * @param {string} filter - One of "ALL", "NEW", "PENDING", "EVALUATED", "REJECTED", "ACCEPTED".
     */
    function renderTable(filter) {
        const filteredApps = (filter === "ALL") ? applications : applications.filter
            (app => getStatus(app) === filter);
        // Build HTML table rows dynamically
        tableBody.innerHTML = filteredApps.map(app => {
            const status = getStatus(app);
            return `
                <tr>
                    <td>${app.id}</td>
                    <td>${app.personalInfo.firstName} ${app.personalInfo.lastName}</td>
                    <td>${app.personalInfo.email}</td>
                    <td>${app.fieldOfResearch}</td>
                    <td><span class="status-badge ${status.toLowerCase()}">${status}</span></td>
                    <td><button class="review-btn" data-id="${app.id}">Review</button></td>
                </tr>`;
        }).join("");

        filteredApps.forEach(app => {
            if(app.openedByAdmin){
                lockApp(app.id);
            }
            });

        attachReviewListeners();
    }

    /* ===============================
       REVIEW POPUP HANDLING
       =============================== */

    /**
     * Attaches event listeners to dynamically generated "Review" buttons.
     * Opens a popup showing full application details and actions.
     */
    function attachReviewListeners() {
        document.querySelectorAll(".review-btn").forEach(btn => {
            btn.addEventListener("click", async () => {
                const id = btn.getAttribute("data-id");

                // Fetch full application details
                const res = await fetch(`/api/applications/${id}`);
                const data = await res.json();

                // If application is already opened by someone else, don't open.
                if(data.openedByAdmin){
                    return;
                }
                else{
                    // Broadcast that it is being opened so others can't open it.
                    openAppOnServer(id);
                }

                popup.innerHTML = `<button id="closePopup">✖</button>`;

                /* -------------------------------
                   VIEW FOR EVALUATED APPLICATIONS
                   ------------------------------- */
                const appStatus = getStatus(data);
                if (appStatus === "EVALUATED") {
                    popup.innerHTML += `
                        <h2>Application #${data.id}</h2>
                        <h3>Applicant</h3>
                        <p><b>Name:</b> ${data.personalInfo.firstName} ${data.personalInfo.lastName}</p>
                        <p><b>Email:</b> ${data.personalInfo.email}</p>
                        <h3>Evaluations</h3>
                        ${data.evaluations.map(e => `
                            <div class="evaluation-entry">
                                <p><b>Professor:</b> ${e.professor.firstName} ${e.professor.lastName}</p>
                                <p><b>Recommendation:</b> ${e.recommendation}</p>
                                <p><b>Comments:</b> ${e.comments || "No comments"}</p>
                            </div>
                        `).join("")}
                        <div style="margin-top:20px;">
                            <button id="approveBtn" class="popup-btn send">✅ Approve</button>
                            <button id="rejectEvalBtn" class="popup-btn reject">❌ Reject</button>
                        </div>
                    `;

                    // Approve application
                    document.getElementById("approveBtn").addEventListener("click", async () => {
                        const response = await fetch(`/api/applications/${id}/accept`, { method: "POST" });
                        response.ok ? showMessage("✅ Application approved!") : showMessage("❌ Failed to approve.", "error");
                    });

                    // Reject after evaluation
                    document.getElementById("rejectEvalBtn").addEventListener("click", async () => {
                        const response = await fetch(`/api/applications/${id}/reject`, { method: "POST" });
                        response.ok ? showMessage("❌ Application rejected.") : showMessage("⚠️ Failed to reject.", "error");
                    });

                    /* -------------------------------
                       VIEW FOR NEW OR PENDING APPLICATIONS
                       ------------------------------- */
                } else if (appStatus === "NEW" || appStatus === "PENDING") {
                    popup.innerHTML += `
                        <h2>Application #${data.id}</h2>
                        <h3>Personal Info</h3>
                        <p><b>Name:</b> ${data.personalInfo.firstName} ${data.personalInfo.lastName}</p>
                        <p><b>Email:</b> ${data.personalInfo.email}</p>
                        <h3>Field of Research</h3>
                        <p>${data.fieldOfResearch}</p>
                        <h3>Documents</h3>
                        <ul>${data.documents.map(d => `<li><a href="${d.link}" target="_blank">${d.title}</a></li>`).join("")}</ul>
                        <div style="margin-top:20px;">
                            ${appStatus === "NEW" ? `
                                <button id="sendEvalBtn" class="popup-btn send">📤 Send For Evaluation</button>
                                <button id="rejectBtn" class="popup-btn reject">❌ Reject</button>
                            ` : `<p><i>Pending professor evaluations...</i></p>`}
                        </div>
                    `;

                    // "Send for Evaluation" and "Reject" buttons only for NEW apps
                    if (appStatus === "NEW") {
                        document.getElementById("sendEvalBtn").addEventListener("click", async () => {
                            const response = await fetch(`/api/applications/${id}/request-evaluation`, {method: "POST"});
                            response.ok ? showMessage("✅ Application sent for evaluation!") : showMessage("❌ Failed to send.", "error");
                        });

                        document.getElementById("rejectBtn").addEventListener("click", async () => {
                            const response = await fetch(`/api/applications/${id}`, {method: "DELETE"});
                            response.ok ? showMessage("❌ Application Deleted.") : showMessage("⚠️ Failed to reject.", "error");
                        });
                    }
                } else if (appStatus === "ACCEPTED" || appStatus === "REJECTED") {
                    popup.innerHTML += `
                        <h2>Application #${data.id}</h2>
                 
                        <h3>Personal Info</h3>
                        <p><b>Name:</b> ${data.personalInfo.firstName} ${data.personalInfo.lastName}</p>
                        <p><b>Email:</b> ${data.personalInfo.email}</p>
                    
                        <h3>Field of Research</h3>
                        <p>${data.fieldOfResearch}</p>
                    
                        <h3>Documents</h3>
                        <ul>${data.documents.map(d => `<li><a href="${d.link}" target="_blank">${d.title}</a></li>`).join("")}</ul>
                    
                        <h3>Final Recommendation</h3>
                        <p><b>Professor's Recommendation:</b> ${data.finalRecommendation || "No recommendation recorded"}</p>
                    
                        <h3>Final Decision</h3>
                        <p><b>Application Status:</b> 
                           <span class="status-badge ${appStatus.toLowerCase()}">${appStatus}</span>
                        </p>
                    
                        <div style="margin-top:15px;">
                            <p><i>This application has been finalized and cannot be modified.</i></p>
                        </div>
                    `;
                }

                // Display popup
                popup.style.display = "block";
                popup.scrollIntoView({ behavior: "smooth" });

                // Close popup handler
                document.getElementById("closePopup").addEventListener("click", () => {
                    closeAppOnServer(id);
                    popup.style.display = "none";
                    popup.innerHTML = "";
                });
            });
        });
    }
});