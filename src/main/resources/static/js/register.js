$(document).ready(function() {
    // Role selection handling
    $('.role-card').on('click', function() {
        const role = $(this).data('role');
        const radio = $(this).find('input[type="radio"]');

        // Deselect all cards
        $('.role-card').removeClass('selected');
        $('input[name="roleSelection"]').prop('checked', false);

        // Select this card
        $(this).addClass('selected');
        radio.prop('checked', true);
        $('#roleInput').val(role);

        // Show/hide additional info sections
        $('#applicantInfo, #professorInfo').addClass('hidden');

        if (role === 'APPLICANT') {
            $('#applicantInfo').removeClass('hidden');
            // Make applicant fields required
            $('#applicantFirstName, #applicantLastName, #applicantPhone, #applicantAddress').prop('required', true);
            // Make professor fields not required
            $('#professorFirstName, #professorLastName, #professorEmail').prop('required', false);
        } else if (role === 'PROFESSOR') {
            $('#professorInfo').removeClass('hidden');
            // Make professor fields required
            $('#professorFirstName, #professorLastName, #professorEmail').prop('required', true);
            // Make applicant fields not required
            $('#applicantFirstName, #applicantLastName, #applicantPhone, #applicantAddress').prop('required', false);
        }
    });

    // Form validation
    $('#registerForm').on('submit', function(e) {
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const role = $('#roleInput').val();

        // Check if role is selected
        if (!role) {
            e.preventDefault();
            alert('Please select a role');
            return false;
        }

        // Check password match
        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Passwords do not match');
            return false;
        }

        return true;
    });

    // Autofill professor email with main email if same
    $('#email').on('blur', function() {
        const role = $('#roleInput').val();
        if (role === 'PROFESSOR' && !$('#professorEmail').val()) {
            $('#professorEmail').val($(this).val());
        }
    });
});