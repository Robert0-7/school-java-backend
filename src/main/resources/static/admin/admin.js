const API_BASE = "http://localhost:8080/api/admin";

// Handle login
document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("adminLoginForm");
  const errorMsg = document.getElementById("error");

  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;

      try {
        // Correct
        const res = await fetch(`${API_BASE}/auth/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password })
        });

        if (res.ok) {
          const data = await res.json();
          localStorage.setItem("adminToken", data.token);
          window.location.href = "admin-dashboard.html";
        } else {
          errorMsg.textContent = "Invalid credentials!";
        }
      } catch (err) {
        errorMsg.textContent = "Server error!";
      }
    });
  }

  // If on dashboard page, fetch data
  if (window.location.pathname.includes("admin-dashboard.html")) {
    const token = localStorage.getItem("adminToken");
    if (!token) {
      window.location.href = "admin-login.html";
    }

    // Fetch enquiries
    fetch(`${API_BASE}/enquiries`, {
      headers: { "Authorization": `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
      const tbody = document.querySelector("#enquiryTable tbody");
      tbody.innerHTML = "";
      data.forEach(enq => {
        tbody.innerHTML += `
          <tr>
            <td>${enq.id}</td>
            <td>${enq.studentName}</td>
            <td>${enq.enquiryClass}</td>
            <td>${enq.mobile}</td>
            <td>${enq.status || "Pending"}</td>
          </tr>`;
      });
    });

    // Fetch admissions
    fetch(`${API_BASE}/admissions`, {
      headers: { "Authorization": `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
      const tbody = document.querySelector("#admissionTable tbody");
      tbody.innerHTML = "";
      data.forEach(adm => {
        tbody.innerHTML += `
          <tr>
            <td>${adm.id}</td>
            <td>${adm.name}</td>
            <td>${adm.admissionClass}</td>
            <td>${adm.fatherName}</td>
            <td>${adm.motherTongue}</td>
          </tr>`;
      });
    });

    // Logout
    document.getElementById("logoutBtn").addEventListener("click", () => {
      localStorage.removeItem("adminToken");
      window.location.href = "admin-login.html";
    });
  }
});
