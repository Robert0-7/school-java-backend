     (function() {
       // Redirect if not logged in as admin
       const token = localStorage.getItem("adminToken");
       if (!token) {
         window.location.href = "admin-login.html";
         return;
       }

       // Decode JWT to check role and expiration
       function parseJwt(token) {
         try {
           return JSON.parse(atob(token.split('.')[1]));
         } catch (e) {
           return {};
         }
       }
       const payload = parseJwt(token);

       // Check for role and expiry
       if (payload.role !== "ADMIN" || (payload.exp && Date.now() / 1000 > payload.exp)) {
         localStorage.removeItem("adminToken");
         window.location.href = "admin-login.html";
         return;
       }

       // Logout button
       document.getElementById("logoutBtn").addEventListener("click", () => {
         localStorage.removeItem("adminToken");
         window.location.href = "admin-login.html";
       });

       // Fetch and fill data
       async function fetchData() {
         try {
           // Fetch Enquiries
           const res1 = await fetch("/api/admin/enquiries", {
             headers: { Authorization: `Bearer ${token}` },
           });
           if (res1.status === 401) {
             localStorage.removeItem("adminToken");
             window.location.href = "admin-login.html";
             return;
           }
           const enquiries = await res1.json();
           const enquiryBody = document.querySelector("#enquiriesTable tbody");
           enquiryBody.innerHTML = "";
           if (enquiries.length === 0) {
             enquiryBody.innerHTML = `<tr><td colspan="3" style="text-align:center;">No enquiries found.</td></tr>`;
           } else {
             enquiries.forEach(e => {
               enquiryBody.innerHTML += `
               <tr>
                <td>${e.enquiryNumber || ""}</td>
                <td>${e.name || ""}</td>
                <td>${e.gender || ""}</td>
                <td>${e.fatherName || ""}</td>
                <td>${e.fatherProfession || ""}</td>
                <td>${e.fatherIncome || ""}</td>
                <td>${e.motherName || ""}</td>
                <td>${e.motherProfession || ""}</td>
                <td>${e.motherIncome || ""}</td>
                <td>${e.admissionClass || ""}</td>
                <td>${e.dob || ""}</td>
                <td>${e.contact1 || ""}</td>
                <td>${e.email || ""}</td>
                <td>${e.address || ""}</td>
                <td>${e.query || ""}</td>
                <td>${Array.isArray(e.source) ? e.source.join(", ") : ""}</td>
               </tr>`;
             });
           }

           // Fetch Admissions
           const res2 = await fetch("/api/admin/admissions", {
             headers: { Authorization: `Bearer ${token}` },
           });
           if (res2.status === 401) {
             localStorage.removeItem("adminToken");
             window.location.href = "admin-login.html";
             return;
           }
           const admissions = await res2.json();
           const admissionBody = document.querySelector("#admissionsTable tbody");
           admissionBody.innerHTML = "";
           if (admissions.length === 0) {
             admissionBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No admissions found.</td></tr>`;
           } else {
             admissions.forEach(a => {
               admissionBody.innerHTML += `
               <tr>
                <td>${a.id || ""}</td>
                <td>${a.enquiryNumber || ""}</td>
                <td>${a.admissionClass || ""}</td>
                <td>${a.childName || ""}</td>
                <td>${a.gender || ""}</td>
                <td>${a.dob || ""}</td>
                <td>${a.bloodGroup || ""}</td>
                <td>${a.nationality || ""}</td>
                <td>${a.state || ""}</td>
                <td>${a.caste || ""}</td>
                <td>${a.motherTongue || ""}</td>
                <td>${a.secondLanguage || ""}</td>
                <td>${a.address || ""}</td>
                <td>${a.mobile || ""}</td>
                <td>${a.aadhaar || ""}</td>
                <td>${a.fatherName || ""}</td>
                <td>${a.fatherMobile || ""}</td>
                <td>${a.fatherEmail || ""}</td>
                <td>${a.motherName || ""}</td>
                <td>${a.motherMobile || ""}</td>
                <td>${a.motherEmail || ""}</td>
                <td>${a.lastSchool || ""}</td>
                <td>${Array.isArray(a.lastSchoolAffiliation) ? a.lastSchoolAffiliation.join(", ") : ""}</td>
                <td><a href="/uploads/${a.childImagePath}" target="_blank">View Child</a></td>
                <td><a href="/uploads/${a.fatherImagePath}" target="_blank">View Father</a></td>
                <td><a href="/uploads/${a.motherImagePath}" target="_blank">View Mother</a></td>
                <td>${a.paymentStatus || ""}</td>
                <td>${a.transactionId || ""}</td>
                <td><a href="/uploads/${a.paymentProofPath}" target="_blank">View Proof</a></td>
              </tr>`;
             });
           }

         } catch (err) {
           console.error("Error loading dashboard:", err);
         }
       }

       fetchData();
     })();