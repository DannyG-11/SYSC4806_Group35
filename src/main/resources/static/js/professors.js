document.getElementById("addProfessorBtn").addEventListener("click", async () => {
    const firstName = document.getElementById('profFirstName').value;
    const lastName = document.getElementById('profLastName').value;
    const email = document.getElementById('profEmail').value;

    if (!firstName || !lastName || !email) {
        alert('Please fill in all professor fields');
        return;
    }

    const professor = { firstName, lastName, email };

    // Add professor
    try {
        const response = await fetch('professors', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(professor)
        });

        if (!response.ok) {
            throw new Error('Failed to add professor');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to add professor. Please try again.');
    }

    // Clear inputs
    document.getElementById('profFirstName').value = '';
    document.getElementById('profLastName').value = '';
    document.getElementById('profEmail').value = '';

    renderProfessors();
});

// Render professors list
async function renderProfessors() {
    const container = document.getElementById('professorsList');
    let professors = [];

     // Get professors
     try {
        const response = await fetch('/professors');
        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const data = await response.json();
        professors = data._embedded?.professors;


        if (professors.length === 0) {
            container.innerHTML = '<p class="empty-state">No professors added yet. Add at least one professor below.</p>';
            return;
        }

        container.innerHTML = '';
        professors.forEach((prof, index) => {
            const div = document.createElement('div');
            div.className = 'professor-item';
            div.innerHTML = `
                    <div class="professor-info">
                        <h4>${prof.firstName} ${prof.lastName}</h4>
                        <p style="font-size: 13px;">${prof.email}</p>
                    </div>
                    <button class="remove-btn" onclick="removeProfessor('${prof._links.self.href}')">Remove</button>
                `;
            container.appendChild(div);
        });
     } catch (error) {
        console.error('Error:', error);
    }
}

// Remove professor
async function removeProfessor(link) {
    try {
        const response = await fetch(link, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Failed to delete professor');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to delete professor. Please try again.');
    }
}

renderProfessors();