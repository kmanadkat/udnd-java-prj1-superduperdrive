package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.Note;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public String addUserNote(
            Authentication authentication,
            NoteForm noteForm,
            CredentialForm credentialForm,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            String username = authentication.getName();
            this.noteService.addNewNote(noteForm, username);
            noteForm.setNoteId(null);
            noteForm.setNoteTitle("");
            noteForm.setNoteDescription("");
            List<Note> notes = this.noteService.getUserNotes(username);
            model.addAttribute("notes", notes);
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @GetMapping(value = "/delete/{noteId}")
    public String deleteUserNote(
            Authentication authentication,
            @PathVariable Integer noteId,
            NoteForm noteForm,
            CredentialForm credentialForm,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String username = authentication.getName();
        try {
            this.noteService.deleteNote(noteId, username);
            model.addAttribute("notes", this.noteService.getUserNotes(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }
}
