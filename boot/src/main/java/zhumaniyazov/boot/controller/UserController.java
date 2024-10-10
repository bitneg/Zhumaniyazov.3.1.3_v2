package zhumaniyazov.boot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zhumaniyazov.boot.model.User;
import zhumaniyazov.boot.service.RoleService;
import zhumaniyazov.boot.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping()
    public String show(@AuthenticationPrincipal User authUser, Model model) {
        model.addAttribute("authUser",authUser);
        return "user";
    }

}







































