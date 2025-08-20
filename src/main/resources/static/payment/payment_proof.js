// -----------------------------
// payment_proof.js
// -----------------------------

// --- 1. Get enquiry number from URL ---
const urlParams = new URLSearchParams(window.location.search);
const enquiryNumber = urlParams.get("enquiryNumber");

// --- 2. Display enquiry number or error ---
if (enquiryNumber) {
  document.getElementById("enquiryNumberDisplay").textContent =
    "Enquiry Number: " + enquiryNumber;
} else {
  document.getElementById("enquiryNumberDisplay").textContent =
    "Error: enquiry number missing. Please go back and fill the admission form.";
}

// --- 3. Handle form submission ---
document
  .getElementById("paymentProofForm")
  .addEventListener("submit", function (e) {
    e.preventDefault();

    if (!enquiryNumber) {
      alert("Enquiry number missing. Cannot proceed.");
      return;
    }

    const fileInput = document.getElementById("paymentScreenshot");
    const transactionId = document.getElementById("transactionId").value.trim();

    // --- Validate inputs ---
    if (!fileInput.files.length) {
      alert("Please upload your payment screenshot.");
      return;
    }
    if (!transactionId) {
      alert("Please enter the transaction ID.");
      return;
    }

    // --- Prepare data for upload ---
    const formData = new FormData();
    formData.append("paymentProof", fileInput.files[0]);
    formData.append("transactionId", transactionId);

    // --- API call ---
    const apiUrl = `/api/admissions/by-enquiry/${enquiryNumber}/upload-payment-proof`;

    fetch(apiUrl, {
        method: 'POST',
        body: formData
    });

    fetch(apiUrl, {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Server responded with status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Success:", data);
        alert("✅ Payment proof submitted successfully!");
        window.location.href = "../success.html";
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "❌ An error occurred while submitting your payment proof. Please try again."
        );
      });
  });
