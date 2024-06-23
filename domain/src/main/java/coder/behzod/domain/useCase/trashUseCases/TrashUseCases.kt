package coder.behzod.domain.useCase.trashUseCases

data class TrashUseCases(

    val saveToTrashUseCase: SaveToTrashUseCase,
    val updateDayUseCase: UpdateDayUseCase,
    val delete:DeleteUseCase,
    val multipleDelete:MultipleDeleteUseCase,
    val getTrashedNotes: GetTrashedNotesUseCase,
    val getListOfNotes: GetListOfNotes,
    val restoreAllUseCase: RestoreAllUseCase,
)
