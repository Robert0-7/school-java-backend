(function() {
    const token = localStorage.getItem('adminToken');
    if (!token) {
        window.location.href = 'admin-login.html';
        return;
    }
    // Decode JWT to check role (simple base64 decode, not secure for secrets)
    function parseJwt (token) {
        try {
            return JSON.parse(atob(token.split('.')[1]));
        } catch (e) {
            return {};
        }
    }
        const payload = parseJwt(token);
        if (payload.role !== 'ADMIN') {
        localStorage.removeItem('adminToken');
        window.location.href = 'admin-login.html';
    }
})();