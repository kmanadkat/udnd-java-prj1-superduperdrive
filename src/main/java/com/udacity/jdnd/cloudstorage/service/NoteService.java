package com.udacity.jdnd.cloudstorage.service;

import com.udacity.jdnd.cloudstorage.mapper.NoteMapper;
import com.udacity.jdnd.cloudstorage.mapper.UserMapper;
import com.udacity.jdnd.cloudstorage.model.Note;
import com.udacity.jdnd.cloudstorage.model.NoteForm;
import com.udacity.jdnd.cloudstorage.model.User;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public List<Note> getUserNotes(String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        return noteMapper.getUserNotes(user.getUserId());
    }

    public void addNewNote(NoteForm noteForm, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        // New Note
        if(noteForm.getNoteId() == null) {
            Note newNote = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), user.getUserId());
            noteMapper.insert(newNote);
        }
        // Update Note
        else {
            // Verify Note belongs to user
            Note userNote = noteMapper.getNote(noteForm.getNoteId());
            if(!Objects.equals(userNote.getUserId(), user.getUserId())){
                throw new InvalidArgumentException("User action not allowed!");
            }
            // Update Title & Description
            userNote.setNoteTitle(noteForm.getNoteTitle());
            userNote.setNoteDescription(noteForm.getNoteDescription());
            noteMapper.update(userNote);
        }
    }

    public void deleteNote(Integer noteId, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        // Validate Note
        Note note = noteMapper.getNote(noteId);
        if(note == null) {
            throw new InvalidArgumentException("Note not found!");
        }
        // Validate Ownership
        if(!Objects.equals(note.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Note does not below to user");
        }
        noteMapper.delete(noteId);
    }
}
