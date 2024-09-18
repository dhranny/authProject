package com.project.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.auth.services.LdapService;
import java.util.List;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LdapService ldapServ;

    @PostMapping("login")
    public List<String> login(){
        return ldapServ.getAllUsersFromOu("bolu");
    }
}
