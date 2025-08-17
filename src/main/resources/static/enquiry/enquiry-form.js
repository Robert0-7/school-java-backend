const form = document.getElementById('enquiryForm');
const submitBtn = document.getElementById('submitBtn');
const loader = document.querySelector('.loader');

// Helper to get the error span by input element
function getErrorSpan(input) {
  // Use id-based error span if available
  if (input.id && document.getElementById(input.id + 'Error')) {
    return document.getElementById(input.id + 'Error');
  }
  // For checkboxes, get the error span after the checkbox group div
  if (input.type === 'checkbox') {
    return input.closest('td').querySelector('.error');
  }
  return input.nextElementSibling && input.nextElementSibling.classList.contains('error')
    ? input.nextElementSibling
    : null;
}

// Attach input event listeners for validation
form.querySelectorAll('input, select, textarea').forEach(input => {
  input.addEventListener('input', () => {
    const errorSpan = getErrorSpan(input);
    if (!errorSpan) return;
    if (input.validity.valid) {
      errorSpan.textContent = '';
    } else {
      errorSpan.textContent = 'This field is required';
    }
  });
});

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  let isValid = true;
  form.querySelectorAll('input, select, textarea').forEach(input => {
    const errorSpan = getErrorSpan(input);
    if (!errorSpan) return;
    if (!input.validity.valid) {
      errorSpan.textContent = 'This field is required';
      isValid = false;
    } else {
      errorSpan.textContent = '';
    }
  });
  if (!isValid) return;

  submitBtn.disabled = true;
  if (loader) loader.classList.remove('hidden');

  // Handle checkboxes for 'source'
  const formDataObj = Object.fromEntries(new FormData(form));
  const sources = Array.from(form.querySelectorAll('input[name="source"]:checked')).map(cb => cb.value);
  if (sources.length) formDataObj.source = sources;

  try {
    const response = await fetch('http://localhost:8080/api/enquiries', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formDataObj)
    });

    if (!response.ok) throw new Error('Submission failed');

    const result = await response.json();
    document.getElementById('enquiryNumber').textContent = result.enquiryNumber || 'N/A';
    document.getElementById('enquirySuccess').style.display = 'block';
    form.reset();
    form.style.display = 'none';

    // Re-bind copy button after showing success
    bindCopyButton();
  } catch (error) {
    alert(error.message);
  } finally {
    submitBtn.disabled = false;
    if (loader) loader.classList.add('hidden');
  }
});

// Bind copy button event on page load and after success message is shown
function bindCopyButton() {
  const copyBtn = document.getElementById('copyEnquiryNumberBtn');
  const enquiryNumber = document.getElementById('enquiryNumber');
  if (copyBtn && enquiryNumber) {
    copyBtn.onclick = function() {
      const text = enquiryNumber.textContent.trim();
      if (text) {
        // Use execCommand fallback for older browsers
        if (navigator.clipboard && window.isSecureContext) {
          navigator.clipboard.writeText(text);
        } else {
          const tempInput = document.createElement('input');
          tempInput.value = text;
          document.body.appendChild(tempInput);
          tempInput.select();
          document.execCommand('copy');
          document.body.removeChild(tempInput);
        }
        copyBtn.textContent = 'Copied!';
        setTimeout(() => { copyBtn.textContent = 'Copy'; }, 1200);
      }
    };
  }
}

// Initial bind in case success message is visible on load
bindCopyButton();
