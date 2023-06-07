package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow

abstract class AbstractUseCaseWithParams<P, R>: UseCase {
    abstract fun invoke(p:P): Flow<R>
}