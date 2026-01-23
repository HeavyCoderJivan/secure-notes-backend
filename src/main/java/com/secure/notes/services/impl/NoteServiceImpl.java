package com.secure.notes.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.secure.notes.model.Note;
import com.secure.notes.repositories.NoteRepository;
import com.secure.notes.services.NoteService;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService 
{
	private final NoteRepository noteRepository;

	@Override
	public Note createNoteForUser(String userName, String content) 
	{
		Note note=new Note();
		note.setContent(content);
		note.setOwnerUsername(userName);
		return noteRepository.save(note);		
	}

	@Override
	public Note updateNoteForUser(Long noteId, String content, String username)
	{
		Note note =noteRepository.findById(noteId).orElseThrow(
				()->new RuntimeException("Note not found"));
		note.setContent(content);
		note.setOwnerUsername(username);
		return noteRepository.save(note);
	}

	@Override
	public void deleteNoteForUser(Long noteId, String username) 
	{	
		noteRepository.deleteById(noteId);
	}

	@Override
	public List<Note> getNotesForUser(String username) 
	{
	    List<Note> personalNotes= noteRepository.findByOwnerUsername(username);
		return personalNotes;
	}

}
