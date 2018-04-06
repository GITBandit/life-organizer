package pl.java.lifeorganizer.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    //add login controller

    @GetMapping("/logout")
    @ResponseBody
    public String logout(){

        SecurityContextHolder.clearContext();

        return "<script>alert(\"Zostałeś wylogowany\"); window.location = \"/\"</script>";
    }

}
