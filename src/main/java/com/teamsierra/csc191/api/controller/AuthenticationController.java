package com.teamsierra.csc191.api.controller;

import com.teamsierra.csc191.api.form.LoginForm;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * User: scott
 * Date: 9/9/13
 * Time: 4:49 PM
 */
@Controller
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loginView() {
        return new ModelAndView("login", "command", new LoginForm());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String authenticate(HttpServletResponse response, @ModelAttribute("login")
                               LoginForm login,
                               BindingResult result) {
        User user = userRepository.findByEmail(login.getUsername());

        if (user.getPassword().equals(login.getPassword())) {
//            SecureRandom random = new SecureRandom();
//            user.setToken(new BigInteger(256, random).toString(64));
            user.setToken("12345678910");
            response.addCookie(new Cookie("authToken", user.getToken()));
        }
        userRepository.save(user);

        return "redirect:appointments";
    }

}
