package com.secure.notes.services;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.secure.notes.model.Note;
@Repository
public interface NoteService 
{
	Note createNoteForUser(String userName,String content);
	Note updateNoteForUser(Long noteId,String content,String username);
	void deleteNoteForUser(Long noteId,String username);
	List<Note> getNotesForUser(String username);
	
	

}
