package com.example.spring_bootstrap.controller;

import com.example.spring_bootstrap.model.Role;
import com.example.spring_bootstrap.model.User;

import com.example.spring_bootstrap.service.RoleService;
import com.example.spring_bootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/user")
    public String userPage(@AuthenticationPrincipal User user, ModelMap modelMap) {
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("role", user.getRoles());
        return "user";
    }

    @GetMapping(value = "/admin")
    public String adminPage(@AuthenticationPrincipal User user, ModelMap modelMap, User user1) {
        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("roles", roleService.getAllRoles());
        modelMap.addAttribute("admin", user);
        modelMap.addAttribute("user1", user1);
        return "admin";
    }

    @PatchMapping(value = "/admin/edit")
    public String editUser(@ModelAttribute User user) {
        userService.editUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, @AuthenticationPrincipal User user, HttpSession httpSession) {
        userService.removeUser(id);
        if(id == user.getId()) {
            httpSession.invalidate();
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin")
    public String addUser(@ModelAttribute @Valid User user1,
                          BindingResult bindingResult,
                          ModelMap modelMap, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("users", userService.getAllUsers());
            modelMap.addAttribute("roles", roleService.getAllRoles());
            modelMap.addAttribute("user1", user1);
            modelMap.addAttribute("admin", user);
            return "admin";
        }
        checkUser(user1);
        return "redirect:/admin";
    }

    @PostConstruct
    void create() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        User user1 = new User("Thomas", "Angelo", 35, "th35@gmail.com", "1234", Set.of(roleAdmin));
        User user2 = new User("Don", "Salieri", 57, "donS@gmail.com", "don", Set.of(roleUser));
        User user3 = new User("Paulie", "Lombardo", 36, "paul@gmail.com", "4321", Set.of(roleUser, roleAdmin));
        checkRole(roleAdmin);
        checkRole(roleUser);
        checkUser(user1);
        checkUser(user2);
        checkUser(user3);
    }

    public void checkUser(User user) {
        if (userService.isEmailAvailable(user.getEmail())) {
            userService.addUser(user);
        } else {
            System.out.println("User with email '" + user.getEmail() + "' already existed");
        }
    }

    public void checkRole(Role role) {
        if (userService.isRoleAvailable(role.getRole())) {
            roleService.save(role);
        } else {
            System.out.println("Role '" + role.getRole() + "' already existed");
        }
    }

}
