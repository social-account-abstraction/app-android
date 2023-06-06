package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow

abstract class AbstractUseCaseWithoutParams<R>(): UseCase {
    abstract fun invoke(): Flow<R>
}