package com.udacity.jdnd.cloudstorage.model;

public class NoteModel {
    private Integer notedId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;

    public NoteModel(Integer notedId, String noteTitle, String noteDescription, Integer userId) {
        this.notedId = notedId;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.userId = userId;
    }

    public Integer getNotedId() {
        return notedId;
    }

    public void setNotedId(Integer notedId) {
        this.notedId = notedId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
