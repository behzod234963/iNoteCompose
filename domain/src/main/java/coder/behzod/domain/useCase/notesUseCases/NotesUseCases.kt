package coder.behzod.domain.useCase.notesUseCases

data class NotesUseCases(
    val deleteUseCase: DeleteNoteUseCase,
    val getNotesUseCase: GetNotesUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
    val deleteAllUseCase:DeleteAllUseCase
)
