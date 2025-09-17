document.addEventListener('DOMContentLoaded', function() {
    const homeLink = document.getElementById('home-link');
    const aboutLink = document.getElementById('about-link');
    const contactLink = document.getElementById('contact-link');
    const homeSection = document.getElementById('home-section');
    const aboutSection = document.getElementById('about-section');
    const contactSection = document.getElementById('contact-section');
    const form = document.getElementById('contact-form');
    const formMessage = document.getElementById('form-message');

    function showSection(section) {
        homeSection.style.display = 'none';
        aboutSection.style.display = 'none';
        contactSection.style.display = 'none';
        section.style.display = 'block';
    }

    homeLink.addEventListener('click', function(e) {
        e.preventDefault();
        showSection(homeSection);
    });
    aboutLink.addEventListener('click', function(e) {
        e.preventDefault();
        showSection(aboutSection);
    });
    contactLink.addEventListener('click', function(e) {
        e.preventDefault();
        showSection(contactSection);
    });

    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            formMessage.textContent = "Thank you for contacting us!";
            form.reset();
        });
    }

    // Modal logic
    try {
        const admissionModal = document.getElementById('admission-modal');
        const heroAdmissionBtn = document.getElementById('hero-admission-btn');
        const modalCancelBtn = document.getElementById('modal-cancel-btn');
        const enquiryBtn = document.getElementById('enquiry-btn');
        const admissionFormBtn = document.getElementById('admission-form-btn');

        if (!admissionModal) console.error('admissionModal not found');
        if (!heroAdmissionBtn) console.error('heroAdmissionBtn not found');
        if (!modalCancelBtn) console.error('modalCancelBtn not found');

        if (heroAdmissionBtn && admissionModal) {
            heroAdmissionBtn.addEventListener('click', function() {
                admissionModal.style.display = 'flex';
            });
        }

        if (modalCancelBtn && admissionModal) {
            modalCancelBtn.addEventListener('click', function() {
                admissionModal.style.display = 'none';
            });
        }

        if (admissionModal) {
            admissionModal.addEventListener('click', function(e) {
                if (e.target === admissionModal) {
                    admissionModal.style.display = 'none';
                }
            });
        }

        // Redirect logic for modal buttons
        if (enquiryBtn) {
            enquiryBtn.addEventListener('click', function() {
                window.location.href = "enquiry/enquiry-form.html";
            });
        }
        if (admissionFormBtn) {
            admissionFormBtn.addEventListener('click', function() {
                window.location.href = "admission/admission-redirect.html";
            });
        }
    } catch (err) {
        console.error('Modal logic error:', err);
    }

    // Enlarge image on click
    document.querySelectorAll('.enlargeable').forEach(img => {
        img.addEventListener('click', function() {
            const modal = document.getElementById('img-modal');
            const modalImg = document.getElementById('img-modal-img');
            modalImg.src = this.src;
            modal.style.display = 'flex';
        });
    });

    // Close modal on click of close button or overlay
    document.getElementById('img-modal-close').onclick = function() {
        document.getElementById('img-modal').style.display = 'none';
    };
    document.getElementById('img-modal').onclick = function(e) {
        if (e.target === this) {
            this.style.display = 'none';
        }
    };
});
