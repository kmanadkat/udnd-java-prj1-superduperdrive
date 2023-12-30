package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.Credential;
import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.service.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getUserCredentials(Authentication authentication, Model model){
        String username = authentication.getName();
        model.addAttribute("credentials", this.credentialService.getUserCredentials(username));
        return "home";
    }

    @PostMapping
    public String addUserCredential(
            Authentication authentication,
            NoteForm noteForm,
            CredentialForm credentialForm,
            Model model
    ) {
        String username = authentication.getName();
        this.credentialService.addNewCredential(credentialForm, username);
        credentialForm.setCredentialId(null);
        credentialForm.setUrl("");
        credentialForm.setUsername("");
        credentialForm.setPassword("");

        List<Credential> credentials = this.credentialService.getUserCredentials(username);
        model.addAttribute("credentials", credentials);
        return "home";
    }
}
