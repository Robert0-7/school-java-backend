package com.hmec.admission_portal.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {

    @GetMapping("/success")
    public String successPage(HttpSession session) {
        Boolean justSubmitted = (Boolean) session.getAttribute("justSubmitted");

        if (justSubmitted != null && justSubmitted) {
            // ✅ allow once
            session.removeAttribute("justSubmitted");
            return "success"; // renders templates/success.html
        }

        // ❌ block direct access
        return "redirect:/index.html";
    }
}
