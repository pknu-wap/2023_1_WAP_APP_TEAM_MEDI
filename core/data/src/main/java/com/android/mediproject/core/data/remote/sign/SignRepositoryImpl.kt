package com.android.mediproject.core.data.remote.sign

import android.util.Log
import com.android.mediproject.core.data.remote.user.UserInfoRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.datastore.TokenDataSource
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource,
    private val tokenDataSource: TokenDataSource,
    private val appDataStore: AppDataStore,
    private val userInfoRepository: UserInfoRepository,
) : SignRepository {

    override fun login(loginParameter: LoginParameter): Flow<Result<Unit>> = channelFlow {
        signDataSource.logIn(loginParameter).collect { signInResult ->
            if (signInResult.isFailure) {
                trySend(Result.failure(signInResult.exceptionOrNull() ?: Exception("로그인 실패")))
            } else {
                appDataStore.apply {
                    saveSkipIntro(true)
                    signInResult.onSuccess {
                        // 내 계정 정보 메모리에 저장
                        userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                        saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                    }
                }
                trySend(Result.success(Unit))
            }
        }
    }

    override fun signUp(signUpParameter: SignUpParameter): Flow<Result<Unit>> = channelFlow {
        signDataSource.signUp(signUpParameter).collect { signUpResult ->
            if (signUpResult.isFailure) {
                trySend(Result.failure(signUpResult.exceptionOrNull() ?: Exception("로그인 실패")))
                return@collect
            } else {
                appDataStore.apply {
                    saveSkipIntro(true)
                    signUpResult.onSuccess {
                        // 내 계정 정보 메모리에 저장
                        userInfoRepository.updateMyAccountInfo(AccountState.SignedIn(it._userId!!.toLong(), it._nickName!!, it._email!!))
                        saveMyAccountInfo(it._email!!, it._nickName!!, it._userId!!.toLong())
                    }
                }
                trySend(Result.success(Unit))
            }
        }
    }

    override suspend fun signOut() = tokenDataSource.removeTokens()

    /**
     * 현재 토큰의 상태를 반환한다.
     *
     * 외부에서 인터페이스로 접근할 때, 이 Flow를 collect하면, 토큰의 상태를 받을 수 있다.
     *
     * 만약 호출 했을 때, 만료되었으면 reissueToken()을 자동으로 호출해서 새로운 토큰을 반환한다.
     */
    override fun getCurrentTokens(): Flow<TokenState<CurrentTokenDto>> = channelFlow {
        when (val tokenState = tokenDataSource.currentTokenState) {
            is TokenState.AccessExpiration -> {
                // access token이 만료되었으므로 토큰 재발급 요청
                Log.d("wap", "getCurrentTokens: access token이 만료되었으므로 토큰 재발급 요청")
                signDataSource.reissueToken(tokenState).collectLatest {
                    trySend(tokenDataSource.currentTokenState)
                }
            }

            else -> {
                // 1. 토큰이 없거나 유효한 경우 -> 그대로 반환(Error or Valid)
                // 2.모든 토큰이 만료되었으므로 토큰 재발급 불가 -> 로그인 새로 ㄱㄱ
                send(tokenState)
            }
        }
    }

}
