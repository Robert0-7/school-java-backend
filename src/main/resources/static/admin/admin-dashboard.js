const token = localStorage.getItem("adminToken");
if (!token) {
  window.location.href = "admin_login.html";
}

document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.removeItem("adminToken");
  window.location.href = "admin_login.html";
});

async function fetchData() {
  try {
    // Fetch Enquiries
    const res1 = await fetch("http://localhost:8080/api/admin/enquiries", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const enquiries = await res1.json();
    const enquiryBody = document.querySelector("#enquiriesTable tbody");
    enquiryBody.innerHTML = "";
    enquiries.forEach(e => {
      enquiryBody.innerHTML += `
        <tr>
          <td>${e.enquiryNumber}</td>
          <td>${e.name}</td>
          <td>${e.admissionClass}</td>
        </tr>`;
    });

    // Fetch Admissions
    const res2 = await fetch("http://localhost:8080/api/admin/admissions", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const admissions = await res2.json();
    const admissionBody = document.querySelector("#admissionsTable tbody");
    admissionBody.innerHTML = "";
    admissions.forEach(a => {
      admissionBody.innerHTML += `
        <tr>
          <td>${a.id}</td>
          <td>${a.enquiryNumber}</td>
          <td>${a.childName}</td>
          <td>${a.admissionClass}</td>
          <td>${a.paymentStatus}</td>
        </tr>`;
    });

  } catch (err) {
    console.error("Error loading dashboard:", err);
  }
}

fetchData();
