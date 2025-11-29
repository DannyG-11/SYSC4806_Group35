let professors = [];
let documents = [];

async function renderProfessors() {
    const container = document.getElementById('professorsList');
    let allProfessors = [];

     // Get professors
     try {
        const response = await fetch('/professors');
        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const data = await response.json();
        allProfessors = data._embedded?.professors;


        if (allProfessors.length === 0) {
            container.innerHTML = '<p class="empty-state">No professors added yet. Add at least one professor below.</p>';
            return;
        }

        container.innerHTML = '';
        allProfessors.forEach((prof, index) => {
            const div = document.createElement('div');
            div.className = 'professor-item';
            div.innerHTML = `
                    <div class="professor-info">
                        <h4>${prof.firstName} ${prof.lastName}</h4>
                        <p style="font-size: 13px;">${prof.email}</p>
                    </div>
                    <input type="checkbox" onclick="selectProfessor('${prof._links.self.href}')"/>
                `;
            container.appendChild(div);
        });
     } catch (error) {
        console.error('Error:', error);
    }
}

function selectProfessor(prof){
    const index = professors.indexOf(prof);

    if(index == -1){
        professors.push(prof);
    } else {
        professors.splice(index, 1);
    }
}

// Add document to list
async function addDocument() {
    const fileInput = document.getElementById('docFile');
    const file = fileInput.files[0];

    if (!file) {
        return;
    }

    // Convert file -> base64
    const base64Data = await fileToBase64(file);

    const newDoc = {
        fileName: file.name,
        contentType: file.type || 'application/octet-stream',
        data: base64Data          // will map to byte[] on the backend
    };

    documents.push(newDoc);

    fileInput.value = '';
    renderDocuments();
}

// Helper: file -> base64 (without the "data:...;base64," prefix)
function fileToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = e => {
            const result = e.target.result;         // "data:...;base64,XXXX"
            const base64 = result.split(',')[1];    // keep only the actual base64
            resolve(base64);
        };

        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}

// Render documents list
function renderDocuments() {
    const container = document.getElementById('documentsList');

    if (documents.length === 0) {
        container.innerHTML = '<p class="empty-state">No documents added yet. Add your supporting documents below.</p>';
        return;
    }

    container.innerHTML = '';
    documents.forEach((doc, index) => {
        const div = document.createElement('div');
        div.className = 'document-item';
        div.innerHTML = `
                <div class="document-info">
                    <h4>${doc.fileName}</h4>
                </div>
                <button class="remove-btn" onclick="removeDocument(${index})">Remove</button>
            `;
        container.appendChild(div);
    });
}

// Remove document
function removeDocument(index) {
    documents.splice(index, 1);
    renderDocuments();
}

// Submit application
document.getElementById('applicationForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Validate professors
    if (professors.length === 0) {
        alert('Please add at least one professor');
        return;
    }

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<span class="loading"></span> Submitting...';

    // Gather form data
    const applicationData = {
        personalInfo: {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            phoneNumber: document.getElementById('phoneNumber').value,
            address: document.getElementById('address').value
        },
        fieldOfResearch: document.getElementById('fieldOfResearch').value,
        professors: professors,
        documents: documents
    };

    console.log(applicationData);

    try {
        const response = await fetch('applicationFiles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(applicationData)
        });

        if (response.ok) {
            document.getElementById('successModal').classList.add('active');
        } else {
            new Error('Failed to submit application');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to submit application. Please try again.');
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Submit Application';
    }
});

renderProfessors();