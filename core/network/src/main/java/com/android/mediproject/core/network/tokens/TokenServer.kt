package com.android.mediproject.core.network.tokens

import com.android.mediproject.core.model.remote.token.CurrentTokens
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.network.datasource.tokens.NewTokensFromServer
import java.time.LocalDateTime


interface TokenServer {
    data class Tokens(
        val accessToken: CharArray,
        val refreshToken: CharArray,
        val accessTokenExpiresIn: LocalDateTime,
        val refreshTokenExpiresIn: LocalDateTime,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Tokens

            if (!accessToken.contentEquals(other.accessToken)) return false
            if (!refreshToken.contentEquals(other.refreshToken)) return false
            if (accessTokenExpiresIn != other.accessTokenExpiresIn) return false
            if (refreshTokenExpiresIn != other.refreshTokenExpiresIn) return false

            return true
        }

        override fun hashCode(): Int {
            var result = accessToken.contentHashCode()
            result = 31 * result + refreshToken.contentHashCode()
            result = 31 * result + accessTokenExpiresIn.hashCode()
            result = 31 * result + refreshTokenExpiresIn.hashCode()
            return result
        }
    }

    val tokenState: TokenState<CurrentTokens>

    /**
     * 토큰을 저장한다.
     */
    suspend fun saveTokens(newTokensFromServer: NewTokensFromServer)

    suspend fun removeTokens()
}


internal fun NewTokensFromServer.toServerTokens(): TokenServer.Tokens {
    return TokenServer.Tokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresIn = accessTokenExpireDateTime,
        refreshTokenExpiresIn = refreshTokenExpireDateTime,
    )
}