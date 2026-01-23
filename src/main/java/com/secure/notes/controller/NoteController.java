package com.secure.notes.controller;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.secure.notes.model.Note;
import com.secure.notes.services.NoteService;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public Note createNote(
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.createNoteForUser(
                userDetails.getUsername(), content);
    }
    @GetMapping
    public List<Note> getUserNotes(
            @AuthenticationPrincipal UserDetails userDetails) {

        return noteService.getNotesForUser(
                userDetails.getUsername());
    }
    @PutMapping("/{noteId}")
    public Note updateNote(
            @PathVariable Long noteId,
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails) {

        return noteService.updateNoteForUser(
                noteId, content, userDetails.getUsername());
    }
    @DeleteMapping("/{noteId}")
    public void deleteNote(
            @PathVariable Long noteId,
            @AuthenticationPrincipal UserDetails userDetails) {

        noteService.deleteNoteForUser(
                noteId, userDetails.getUsername());
    }
}
