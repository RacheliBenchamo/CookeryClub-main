package hac.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String error(Model model) {
        model.addAttribute("error", "Some error occurred");
        return "error/error";
    }

    //Handles the "/403" endpoint and displays the "403" view.
    @RequestMapping("/403")
    public String forbidden() {
        return "error/403";
    }
}
