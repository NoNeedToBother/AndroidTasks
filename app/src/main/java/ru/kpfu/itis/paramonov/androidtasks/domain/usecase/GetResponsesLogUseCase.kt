package ru.kpfu.itis.paramonov.androidtasks.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.ResponseUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ResponseRepository
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import javax.inject.Inject

class GetResponsesLogUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: ResponseRepository,
    private val mapper: ResponseUiModelMapper
) {

    suspend operator fun invoke(): List<ResponseUiModel> {
        return withContext(dispatcher) {
            repository.getResponses().map {
                mapper.mapDomainToUiModel(it)
            }
        }
    }
}