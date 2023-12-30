package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.Credential;
import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.Note;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.service.CredentialService;
import com.udacity.jdnd.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(NoteService noteService, CredentialService credentialService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String homeView(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        List<Note> notes = noteService.getUserNotes(username);
        List<Credential> credentials = credentialService.getUserCredentials(username);

        model.addAttribute("notes", notes);
        model.addAttribute("credentials", credentials);
        return "home";
    }
}
