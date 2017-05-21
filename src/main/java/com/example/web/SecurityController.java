package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by grisha on 18.05.17.
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    @RequestMapping("loginfail")
    public String loginFail(Model model) {
        model.addAttribute("message", "her");
        return "contacts/list";
    }
}
