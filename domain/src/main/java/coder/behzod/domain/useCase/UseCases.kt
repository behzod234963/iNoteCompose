package coder.behzod.domain.useCase

data class UseCases(
    val deleteUseCase: DeleteNoteUseCase,
    val getNotesUseCase: GetNotesUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
)
