document.addEventListener("DOMContentLoaded", async () => {
    const tableBody = document.querySelector("tbody");
    const popup = document.getElementById("reviewPopup");

    // Fetch all applications
    const res = await fetch("/api/applications");
    const applications = await res.json();

    // Filter out applications that are available to professors
    const filteredApps = applications.filter(app => !app.availableToProfs);

    // Build the table rows
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

    // Attach event listeners to review buttons
    const reviewButtons = document.querySelectorAll(".review-btn");

    reviewButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const id = btn.getAttribute("data-id");

            const res = await fetch(`/api/applications/${id}`);
            const data = await res.json();

            // Build popup HTML
            popup.innerHTML = `
                <button id="closePopup" style="float:right;">✖ Close</button>
                <h2>Application #${data.id}</h2>
                
                <h3>Personal Info</h3>
                <p><b>Name:</b> ${data.personalInfo.firstName} ${data.personalInfo.lastName}</p>
                <p><b>Email:</b> ${data.personalInfo.email}</p>
                <p><b>Phone:</b> ${data.personalInfo.phoneNumber ?? "n/a"}</p>
                <p><b>Address:</b> ${data.personalInfo.address ?? "n/a"}</p>

                <h3>Desired Field of Research</h3>
                <p>${data.fieldOfResearch}</p>

                <h3>Professors</h3>
                <ul>
                    ${data.professors.map(p => `<li>${p.firstName} ${p.lastName} (${p.email})</li>`).join('')}
                </ul>

                <h3>Supporting Documents</h3>
                <ul>
                    ${data.documents.map(d => `<li><a href="${d.link}" target="_blank">${d.title}</a></li>`).join('')}
                </ul>

                <div style="margin-top:20px;">
                    <button id="sendEvalBtn">Send For Evaluation</button>
                    <button id="rejectBtn" style="margin-left:10px; background-color:#d9534f; color:white;">Reject Application</button>
                </div>
            `;

            popup.style.display = "block";
            popup.scrollIntoView({ behavior: "smooth" });

            // Close button
            document.getElementById("closePopup").addEventListener("click", () => {
                popup.style.display = "none";
                popup.innerHTML = "";
            });

            // Send for evaluation button
            document.getElementById("sendEvalBtn").addEventListener("click", async () => {
                if (confirm("Send this application for evaluation?")) {
                    const response = await fetch(`/api/applications/${id}/request-evaluation`, {
                        method: "POST"
                    });
                    if (response.ok) {
                        alert("Application sent for evaluation!");
                        location.reload();
                    } else {
                        alert("Failed to send for evaluation.");
                    }
                }
            });

            // Reject button
            document.getElementById("rejectBtn").addEventListener("click", async () => {
                if (confirm("Are you sure you want to reject this application?")) {
                    const response = await fetch(`/api/applications/${id}/reject`, {
                        method: "POST"
                    });
                    if (response.ok) {
                        alert("Application rejected.");
                        location.reload();
                    } else {
                        alert("Failed to reject application.");
                    }
                }
            });
        });
    });
});
