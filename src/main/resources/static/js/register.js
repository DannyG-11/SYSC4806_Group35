$(document).ready(function() {

    // Form validation on submit
    $('#registerForm').on('submit', function(e) {
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const username = $('#username').val();

        // Check username length
        if (username.length < 3) {
            e.preventDefault();
            alert('Username must be at least 3 characters long');
            return false;
        }

        // Check password length
        if (password.length < 8) {
            e.preventDefault();
            alert('Password must be at least 8 characters long');
            return false;
        }

        // Check password match
        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Passwords do not match');
            return false;
        }

        // All validations passed
        return true;
    });

    // Real-time password match indicator (optional enhancement)
    $('#confirmPassword').on('input', function() {
        const password = $('#password').val();
        const confirmPassword = $(this).val();

        if (confirmPassword.length > 0) {
            if (password === confirmPassword) {
                $(this).css('border-color', '#27ae60');
            } else {
                $(this).css('border-color', '#e74c3c');
            }
        } else {
            $(this).css('border-color', '#e0e0e0');
        }
    });

    // Username validation indicator
    $('#username').on('input', function() {
        const username = $(this).val();

        if (username.length >= 3) {
            $(this).css('border-color', '#27ae60');
        } else if (username.length > 0) {
            $(this).css('border-color', '#e74c3c');
        } else {
            $(this).css('border-color', '#e0e0e0');
        }
    });

    // Password strength indicator
    $('#password').on('input', function() {
        const password = $(this).val();

        if (password.length >= 8) {
            $(this).css('border-color', '#27ae60');
        } else if (password.length > 0) {
            $(this).css('border-color', '#e74c3c');
        } else {
            $(this).css('border-color', '#e0e0e0');
        }
    });
});