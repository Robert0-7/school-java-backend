document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("adminLoginForm");
  const errorMsg = document.getElementById("errorMsg");

  // If already logged in, redirect to dashboard
  const token = localStorage.getItem("adminToken");
  if (token) {
    window.location.href = "admin-dashboard.html";
    return;
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    errorMsg.textContent = ""; // Clear previous error

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (!username || !password) {
      errorMsg.textContent = "Please fill in both fields.";
      return;
    }

    try {
      const res = await fetch("/api/admin/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });

      if (!res.ok) {
        if (res.status === 401) {
          errorMsg.textContent = "Invalid username or password.";
        } else {
          errorMsg.textContent = "Server error. Please try again.";
        }
        return;
      }

      const data = await res.json();
      if (data.token) {
        localStorage.setItem("adminToken", data.token);
        window.location.href = "admin-dashboard.html";
      } else {
        errorMsg.textContent = "Unexpected server response.";
      }
    } catch (err) {
      errorMsg.textContent = "Network error. Please try again.";
    }
  });
});