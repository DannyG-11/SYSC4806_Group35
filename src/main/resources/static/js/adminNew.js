document.addEventListener("DOMContentLoaded", async () => {
    const tableBody = document.querySelector("tbody");
    const popup = document.getElementById("reviewPopup");

    const messageModal = document.getElementById("messageModal");
    const messageBox = document.getElementById("messageBox");
    const messageText = document.getElementById("messageText");
    const messageClose = document.getElementById("messageClose");

    function showMessage(text, type = "success") {
        messageBox.className = type === "error" ? "error" : "success";
        messageText.textContent = text;
        messageModal.style.display = "flex";
    }

    messageClose.addEventListener("click", () => {
        messageModal.style.display = "none";
        location.reload();
    });

    // Fetch all applications
    const res = await fetch("/api/applications");
    const applications = await res.json();

    // Filter applications not yet available to professors
    const filteredApps = applications.filter(app => !app.availableToProfs);

    // Build the table
    tableBody.innerHTML = filteredApps.map(app => `
        <tr>
            <td>${app.id}</td>
            <td>${app.personalInfo.firstName} ${app.personalInfo.lastName}</td>
            <td>${app.personalInfo.email}</td>
            <td>${app.fieldOfResearch}</td>
            <td>
                <button class="review-btn" data-id="${app.id}">Review</button>
            </td>
        </tr>
    `).join("");

    // Event listeners for review buttons
    const reviewButtons = document.querySelectorAll(".review-btn");

    reviewButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const id = btn.getAttribute("data-id");
            const res = await fetch(`/api/applications/${id}`);
            const data = await res.json();

            popup.innerHTML = `
                <button id="closePopup">✖</button>
                <h2>Application #${data.id}</h2>
                <h3>Personal Info</h3>
                <p><b>Name:</b> ${data.personalInfo.firstName} ${data.personalInfo.lastName}</p>
                <p><b>Email:</b> ${data.personalInfo.email}</p>

                <h3>Field of Research</h3>
                <p>${data.fieldOfResearch}</p>

                <h3>Supporting Documents</h3>
                <ul>
                    ${data.documents.map(d => `<li><a href="${d.link}" target="_blank">${d.title}</a></li>`).join('')}
                </ul>

                <div style="margin-top:20px;">
                    <button id="sendEvalBtn" class="popup-btn send">📤 Send For Evaluation</button>
                    <button id="rejectBtn" class="popup-btn reject">❌ Reject Application</button>
                </div>
            `;

            popup.style.display = "block";
            popup.scrollIntoView({ behavior: "smooth" });

            document.getElementById("closePopup").addEventListener("click", () => {
                popup.style.display = "none";
                popup.innerHTML = "";
            });

            document.getElementById("sendEvalBtn").addEventListener("click", async () => {
                const response = await fetch(`/api/applications/${id}/request-evaluation`, { method: "POST" });
                if (response.ok) {
                    showMessage("✅ Application sent for evaluation!", "success");
                } else {
                    showMessage("❌ Failed to send for evaluation.", "error");
                }
            });

            document.getElementById("rejectBtn").addEventListener("click", async () => {
                const response = await fetch(`/api/applications/${id}/reject`, { method: "POST" });
                if (response.ok) {
                    showMessage("❌ Application rejected.", "error");
                } else {
                    showMessage("⚠️ Failed to reject application.", "error");
                }
            });
        });
    });
});
