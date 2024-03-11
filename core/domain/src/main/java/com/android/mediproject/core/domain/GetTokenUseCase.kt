package com.android.mediproject.core.domain

import com.android.mediproject.core.data.sign.TokenRepository
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTokenUseCase @Inject constructor(private val tokenRepository: TokenRepository) {

    operator fun invoke(): Flow<TokenState<CurrentTokens>> = channelFlow {
        trySend(TokenState.Empty)
    }
}
