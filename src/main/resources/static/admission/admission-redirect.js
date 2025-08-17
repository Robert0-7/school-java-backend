document.getElementById("redirectForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const enquiryNumber = document.getElementById("enquiryNumber").value.trim();
  const errorMsg = document.getElementById("errorMsg");

  if (!enquiryNumber) {
    errorMsg.textContent = "Please enter your enquiry number.";
    return;
  }

  try {
    const res = await fetch(`http://localhost:8080/api/enquiries/${enquiryNumber}`);
    if (!res.ok) {
      errorMsg.textContent = "Invalid enquiry number.";
      return;
    }

    const data = await res.json();

    // Redirect based on admissionClass from enquiry
    if (data.admissionClass && data.admissionClass.toLowerCase() === "11th") {
      window.location.href = `admission-form-class11.html?enquiryNumber=${encodeURIComponent(enquiryNumber)}`;
    } else {
      window.location.href = `admission-form-nursery-9.html?enquiryNumber=${encodeURIComponent(enquiryNumber)}`;
    }
  } catch (err) {
    errorMsg.textContent = "Error verifying enquiry number.";
  }
});
