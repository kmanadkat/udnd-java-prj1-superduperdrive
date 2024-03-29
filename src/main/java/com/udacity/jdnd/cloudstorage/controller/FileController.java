package com.udacity.jdnd.cloudstorage.controller;

import com.udacity.jdnd.cloudstorage.model.CredentialForm;
import com.udacity.jdnd.cloudstorage.model.File;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.service.FileService;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String getUserFiles(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "home";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @PostMapping
    public String uploadUserFile(
            Authentication authentication,
            MultipartFile fileUpload,
            NoteForm noteForm,
            CredentialForm credentialForm,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String username = authentication.getName();
        try {
            fileService.addNewFile(fileUpload, username);
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @GetMapping(value = "/view/{fileId}")
    public ResponseEntity viewUserFile(
            Authentication authentication,
            @PathVariable Integer fileId,
            MultipartFile fileUpload,
            NoteForm noteForm,
            CredentialForm credentialForm,
            Model model
    ){
        String username = authentication.getName();

        try {
            File file = fileService.getFileByNameId(fileId, username);
            String fileName = file.getFileName();
            String contentType = file.getContentType();
            byte[] fileData = file.getFileData();
            InputStream inputStream = new ByteArrayInputStream(fileData);
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (InvalidArgumentException e) {
            return ResponseEntity.internalServerError().body("File not found");
        }
    }

    @GetMapping(value = "/delete/{fileId}")
    public String deleteUserFile(
            Authentication authentication,
            @PathVariable Integer fileId,
            MultipartFile fileUpload,
            NoteForm noteForm,
            CredentialForm credentialForm,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        String username = authentication.getName();
        try{
            fileService.deleteUserFile(fileId, username);
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

}
