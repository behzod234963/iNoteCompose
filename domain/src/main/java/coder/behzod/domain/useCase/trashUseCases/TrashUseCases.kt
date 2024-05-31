package coder.behzod.domain.useCase.trashUseCases

data class TrashUseCases(
    val delete:DeleteUseCase,
    val deleteAll:DeleteAllUseCase,
    val getTrashedNotes: GetTrashedNotesUseCase,
    val saveToTrash: SaveToTrashUseCase,
    val restoreAllUseCase: RestoreAllUseCase
)
