document.addEventListener("DOMContentLoaded", async () => {
  const enquiryNumber = new URLSearchParams(window.location.search).get("enquiryNumber");
  if (!enquiryNumber) {
    alert("No enquiry number provided.");
    return;
  }

  try {
    const res = await fetch(`/api/enquiries/${enquiryNumber}`);
    if (!res.ok) throw new Error("Enquiry not found");
    const data = await res.json();

    // Prefill and make readonly
    document.querySelector("[name='enquiryNumber']").value = enquiryNumber;
    document.querySelector("[name='childName']").value = data.name;
    document.querySelector("[name='gender']").value = data.gender;
    document.querySelector("[name='fatherName']").value = data.fatherName;
    document.querySelector("[name='fatherMobile']").value = data.contact1;
    document.querySelector("[name='motherName']").value = data.motherName;
    document.querySelector("[name='motherMobile']").value = data.contact2 || data.contact1;
    document.querySelector("[name='address']").value = data.address;
    document.querySelector("[name='admissionClass']").value = data.admissionClass;
    document.querySelector("[name='mobile']").value = data.contact1;

    // DOB handling
    if (data.dob) {
      const [yyyy, mm, dd] = data.dob.split("-");
      document.querySelector("#dob").value = `${yyyy}-${mm}-${dd}`;
      document.querySelector("[name='dob_dd']").value = dd;
      document.querySelector("[name='dob_mm']").value = mm;
      document.querySelector("[name='dob_yyyy']").value = yyyy;
    }
  } catch (err) {
    alert(err.message);
  }

  // Toggle "Other Nationality"
  const nationalitySelect = document.getElementById('nationality-nursery');
  const nationalityOther = document.getElementById('nationalityOther-nursery');

  if (nationalitySelect && nationalityOther) {
    nationalitySelect.addEventListener('change', () => {
      if (nationalitySelect.value === 'Other') {
        nationalityOther.style.display = '';
      } else {
        nationalityOther.style.display = 'none';
        nationalityOther.value = '';
      }
    });
  }

  // Toggle "Other Mother Tongue"
  window.toggleMotherTongueOtherNursery = function() {
    var sel = document.getElementById('motherTongue-nursery');
    var other = document.getElementById('motherTongueOther-nursery');
    if (sel && other) {
      other.style.display = sel.value === 'Other' ? '' : 'none';
      if (sel.value !== 'Other') other.value = '';
    }
  };

  // Form submission
  document.getElementById("admissionFormNursery9").addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);

    try {
      const res = await fetch("/api/admissions", {
        method: "POST",
        body: formData
      });
      if (!res.ok) {
        let errorMsg = "Submission failed";
        try {
          const errorData = await res.json();
          if (errorData && errorData.message) errorMsg = errorData.message;
        } catch {
          // ignore
        }
        throw new Error(errorMsg);
      }
      alert("Admission submitted successfully!");
      window.location.href = `../payment/payment_proof.html?enquiryNumber=${enquiryNumber}`;
    } catch (err) {
      alert(err.message);
    }
  });
});
