document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const enquiryNumber = urlParams.get("enquiryNumber");
  const admissionClass = urlParams.get("admissionClass") || "11th";

  if (!enquiryNumber) {
    alert("No enquiry number provided.");
    return;
  }

  document.querySelector("[name='enquiryNumber']").value = enquiryNumber;
  document.querySelector("[name='admissionClass']").value = admissionClass;

  try {
    const res = await fetch(`/api/enquiries/${enquiryNumber}`);
    if (!res.ok) throw new Error("Enquiry not found");
    const data = await res.json();

    // Prefill readonly from enquiry
    document.querySelector("[name='childName']").value = data.name;
    document.querySelector("[name='gender']").value = data.gender;
    document.querySelector("[name='fatherName']").value = data.fatherName;
    document.querySelector("[name='motherName']").value = data.motherName;
    document.querySelector("[name='fatherOcc']").value = data.fatherProfession;
    document.querySelector("[name='fatherIncome']").value = data.fatherIncome;
    document.querySelector("[name='motherOcc']").value = data.motherProfession;
    document.querySelector("[name='motherIncome']").value = data.motherIncome;
    document.querySelector("[name='address']").value = data.address;
    document.querySelector("[name='mobile']").value = data.contact1;
    document.querySelector("[name='fatherMobile']").value = data.contact1;
    document.querySelector("[name='motherMobile']").value = data.contact2 || data.contact1;

    // DOB conversion & splitting
    if (data.dob) {
      let dobStr = data.dob;
      let yyyy, mm, dd;
      if (/^\d{4}-\d{2}-\d{2}$/.test(dobStr)) {
        [yyyy, mm, dd] = dobStr.split("-");
      } else if (/^\d{2}\/\d{2}\/\d{4}$/.test(dobStr)) {
        [dd, mm, yyyy] = dobStr.split("/");
      }
      document.querySelector("[name='dob']").value = `${dd}-${mm}-${yyyy}`;
      document.querySelector("[name='dob_dd']").value = dd;
      document.querySelector("[name='dob_mm']").value = mm;
      document.querySelector("[name='dob_yyyy']").value = yyyy;
    }
  } catch (err) {
    alert(err.message);
  }

  // --- Toggle nationality
  window.toggleNationalityOther = function () {
    const sel = document.getElementById("nationality");
    const other = document.getElementById("nationalityOther");
    if (sel.value === "Other") {
      other.style.display = "";
    } else {
      other.style.display = "none";
      other.value = "";
    }
  };

  // --- Toggle mother tongue "Other"
  window.toggleMotherTongueOther = function () {
    var sel = document.getElementById('motherTongue');
    var other = document.getElementById('motherTongueOther');
    if (sel && other) {
      other.style.display = sel.value === 'Other' ? '' : 'none';
      if (sel.value !== 'Other') other.value = '';
    }
  };

  // --- Group & subjects logic
  const groups = {
    A: [
      { name: "English", type: "fixed" },
      { name: ["History", "Geography"], type: "single" },
      { name: "Political Science", type: "fixed" },
      { name: ["Economics", "Mass Media", "Computer Science", "Informative Practice"], type: "single" },
      { name: ["Mathematics", "Applied Mathematics", "Fashion Studies", "Psychology"], type: "single" },
      { name: ["Physical Education", "Hindi", "Bengali", "Painting"], type: "single" }
    ],
    B: [
      { name: "English", type: "fixed" },
      { name: "Physics", type: "fixed" },
      { name: "Chemistry", type: "fixed" },
      { name: ["Biology", "Economics", "Computer Science", "Informative Practice"], type: "single" },
      { name: ["Mathematics", "Applied Mathematics", "Bio Technology", "Psychology"], type: "single" },
      { name: ["Physical Education", "Hindi", "Bengali", "Painting"], type: "single" }
    ],
    C: [
      { name: "English", type: "fixed" },
      { name: "Business Studies", type: "fixed" },
      { name: "Accountancy", type: "fixed" },
      { name: ["Economics", "Mass Media", "Computer Science", "Informative Practice"], type: "single" },
      { name: ["Mathematics", "Applied Mathematics", "Fashion Studies", "Psychology", "Entrepreneurship"], type: "single" },
      { name: ["Physical Education", "Hindi", "Bengali", "Painting"], type: "single" }
    ]
  };

  const appliedGroupSelect = document.getElementById("appliedGroup");
  appliedGroupSelect.innerHTML = `
    <option value="">Select</option>
    <option value="A">Humanities</option>
    <option value="B">Science</option>
    <option value="C">Commerce</option>
  `;

  appliedGroupSelect.addEventListener("change", () => {
    const val = appliedGroupSelect.value;
    const section = document.getElementById("subjectsSection");
    section.innerHTML = "";
    if (!groups[val]) return;

    groups[val].forEach((subj, idx) => {
      const div = document.createElement("div");
      div.classList.add("subject-choice");
      if (subj.type === "fixed") {
        div.innerHTML = `<label><input type="checkbox" name="chosenSubjects" value="${subj.name}" checked readonly> ${subj.name}</label>`;
      } else if (subj.type === "single") {
        div.innerHTML = `
          <label>Choose Subject ${idx + 1}:</label>
          <select name="chosenSubjects" required>
            <option value="">Select</option>
            ${subj.name.map(opt => `<option value="${opt}">${opt}</option>`).join("")}
          </select>
        `;
      }
      section.appendChild(div);
    });
  });

  // --- Form submit
  document.getElementById("admissionFormClass11").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.target;

    // Validate all Mark/Grade fields are between 0 and 100
    const perfFields = [
      "ixEng", "ixLang", "ixMath", "ixScience", "ixSst",
      "xEng", "xLang", "xMath", "xScience", "xSst"
    ];
    for (const name of perfFields) {
      const val = form[name].value;
      if (val === "" || isNaN(val) || Number(val) < 0 || Number(val) > 100) {
        alert("All Mark/Grade fields must be numbers between 0 and 100.");
        form[name].focus();
        return;
      }
    }

    const formData = new FormData(form);

    try {
      const res = await fetch("/api/admissions", {
        method: "POST",
        body: formData
      });
      if (!res.ok) {
        let msg = "Submission failed";
        try {
          const errData = await res.json();
          msg = JSON.stringify(errData);
        } catch {}
        throw new Error(msg);
      }
      const result = await res.json();
      alert("Admission submitted successfully! ");
      window.location.href = `../payment/payment_proof.html?enquiryNumber=${enquiryNumber}`;    } catch (err) {
      alert(err.message);
    }
  });
});
