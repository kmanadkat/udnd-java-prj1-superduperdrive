package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.Credential;
import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.service.CredentialService;
import com.udacity.jdnd.cloudstorage.service.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getUserCredentials(Authentication authentication, Model model){
        String username = authentication.getName();
        model.addAttribute("credentials", this.credentialService.getUserCredentials(username));
        return "home";
    }

    @GetMapping(value = "/decrypt-password/{credentialId}")
    @ResponseBody
    public Map<String, String> decryptCredential(Authentication authentication, @PathVariable Integer credentialId) {
        String username = authentication.getName();
        String rawPassword = credentialService.getUserCredentialDecoded(credentialId, username);
        Map<String, String> response = new HashMap<>();
        response.put("decryptedPassword", rawPassword);
        return response;
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

    @GetMapping(value = "/delete/{credentialId}")
    public String deleteUserCredential(
            Authentication authentication,
            @PathVariable Integer credentialId,
            NoteForm noteForm,
            CredentialForm credentialForm,
            Model model
    ) {
        String username = authentication.getName();

        this.credentialService.deleteCredential(credentialId, username);
        model.addAttribute("credentials", this.credentialService.getUserCredentials(username));
        return "home";
    }
}
