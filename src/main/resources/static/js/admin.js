document.addEventListener("DOMContentLoaded", async () => {
    const tableBody = document.querySelector("tbody");
    const popup = document.getElementById("reviewPopup");

    const messageModal = document.getElementById("messageModal");
    const messageBox = document.getElementById("messageBox");
    const messageText = document.getElementById("messageText");
    const messageClose = document.getElementById("messageClose");
    const statusFilter = document.getElementById("statusFilter");

    let applications = [];

    function showMessage(text, type = "success") {
        messageBox.className = type === "error" ? "error" : "success";
        messageText.textContent = text;
        messageModal.style.display = "flex";
    }

    messageClose.addEventListener("click", () => {
        messageModal.style.display = "none";
        location.reload();
    });

    const res = await fetch("/api/applications");
    applications = await res.json();

    renderTable("NEW"); // default view

    statusFilter.addEventListener("change", () => {
        renderTable(statusFilter.value);
    });

    function getStatus(app) {
        if (!app.availableToProfs) return "NEW";
        if (app.availableToProfs && (!app.evaluations || app.evaluations.length === 0)) return "PENDING";
        if (app.evaluations && app.evaluations.length > 0) return "EVALUATED";
        return "UNKNOWN";
    }

    function renderTable(filter) {
        const filteredApps = applications.filter(app => getStatus(app) === filter);

        tableBody.innerHTML = filteredApps.map(app => `
            <tr>
                <td>${app.id}</td>
                <td>${app.personalInfo.firstName} ${app.personalInfo.lastName}</td>
                <td>${app.personalInfo.email}</td>
                <td>${app.fieldOfResearch}</td>
                <td><button class="review-btn" data-id="${app.id}">Review</button></td>
            </tr>
        `).join("");

        attachReviewListeners(filter);
    }

    function attachReviewListeners(filter) {
        document.querySelectorAll(".review-btn").forEach(btn => {
            btn.addEventListener("click", async () => {
                const id = btn.getAttribute("data-id");
                const res = await fetch(`/api/applications/${id}`);
                const data = await res.json();

                popup.innerHTML = `<button id="closePopup">✖</button>`;

                if (filter === "EVALUATED") {
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

                    document.getElementById("approveBtn").addEventListener("click", async () => {
                        const response = await fetch(`/api/applications/${id}/approve`, { method: "POST" });
                        response.ok ? showMessage("✅ Application approved!") : showMessage("❌ Failed to approve.", "error");
                    });

                    document.getElementById("rejectEvalBtn").addEventListener("click", async () => {
                        const response = await fetch(`/api/applications/${id}/reject`, { method: "POST" });
                        response.ok ? showMessage("❌ Application rejected.") : showMessage("⚠️ Failed to reject.", "error");
                    });

                } else {
                    // NEW or PENDING
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
                            ${filter === "NEW" ? `
                                <button id="sendEvalBtn" class="popup-btn send">📤 Send For Evaluation</button>
                                <button id="rejectBtn" class="popup-btn reject">❌ Reject</button>
                            ` : `<p><i>Pending professor evaluations...</i></p>`}
                        </div>
                    `;

                    if (filter === "NEW") {
                        document.getElementById("sendEvalBtn").addEventListener("click", async () => {
                            const response = await fetch(`/api/applications/${id}/request-evaluation`, { method: "POST" });
                            response.ok ? showMessage("✅ Application sent for evaluation!") : showMessage("❌ Failed to send.", "error");
                        });

                        document.getElementById("rejectBtn").addEventListener("click", async () => {
                            const response = await fetch(`/api/applications/${id}/reject`, { method: "POST" });
                            response.ok ? showMessage("❌ Application rejected.") : showMessage("⚠️ Failed to reject.", "error");
                        });
                    }
                }

                popup.style.display = "block";
                popup.scrollIntoView({ behavior: "smooth" });

                document.getElementById("closePopup").addEventListener("click", () => {
                    popup.style.display = "none";
                    popup.innerHTML = "";
                });
            });
        });
    }
});
