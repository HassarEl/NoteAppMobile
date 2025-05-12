package ma.noteapp.listeners;

import ma.noteapp.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
