const form = document.getElementById("adminLoginForm");
const errorMsg = document.getElementById("errorMsg");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();

  try {
    const res = await fetch("http://localhost:8080/api/admin/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (!res.ok) {
      throw new Error("Invalid username or password");
    }

    const data = await res.json();
    // Save JWT to localStorage
    localStorage.setItem("adminToken", data.token);
    // Redirect to dashboard
    window.location.href = "admin_dashboard.html";
  } catch (err) {
    errorMsg.textContent = err.message;
  }
});
form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();

  try {
    const res = await fetch("http://localhost:8080/api/admin/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (!res.ok) {
      throw new Error("Invalid username or password");
    }

    const data = await res.json();
    // Save JWT to localStorage
    localStorage.setItem("adminToken", data.token);
    // Redirect to dashboard
    window.location.href = "admin_dashboard.html";
  } catch (err) {
    errorMsg.textContent = err.message;
  }
});
