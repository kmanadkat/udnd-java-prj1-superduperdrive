package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.*;
import com.udacity.jdnd.cloudstorage.service.CredentialService;
import com.udacity.jdnd.cloudstorage.service.FileService;
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
    private final FileService fileService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String homeView(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        List<Note> notes = noteService.getUserNotes(username);
        List<Credential> credentials = credentialService.getUserCredentials(username);
        List<File> files = fileService.getUserFiles(username);

        model.addAttribute("notes", notes);
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", files);
        return "home";
    }
}
