package coder.behzod.domain.useCase.trashUseCases

data class TrashUseCases(

    val saveToTrashUseCase: SaveToTrashUseCase,
    val delete:DeleteUseCase,
    val multipleDelete:MultipleDeleteUseCase,
    val getTrashedNotes: GetTrashedNotesUseCase,
    val restoreAllUseCase: RestoreAllUseCase,
)
