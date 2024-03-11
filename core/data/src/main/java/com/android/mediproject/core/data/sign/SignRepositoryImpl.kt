package com.android.mediproject.core.data.sign

import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter
import com.android.mediproject.core.network.datasource.sign.SignDataSource

internal class SignRepositoryImpl(
    private val signDataSource: SignDataSource,
    private val accountSessionRepository: AccountSessionRepository,
    private val appDataStore: AppDataStore,
) : SignRepository {


    override suspend fun login(loginParameter: LoginParameter) = signDataSource.logIn(loginParameter).fold(
        onSuccess = {
            accountSessionRepository.updateSession(it.userSession)
            accountSessionRepository.updateAccount(loginParameter.email, it.userSession.username)
            appDataStore.saveSkipIntro(true)
            LoginState.Success
        },
        onFailure = {
            if (it is UserNotConfirmedException) {
                LoginState.NotVerified
            } else {
                LoginState.Failed(it)
            }
        },
    )


    override suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean> {
        signDataSource.signUp(signUpParameter).onSuccess {
            appDataStore.saveSkipIntro(true)
        }
        return Result.success(true)
    }

    override suspend fun signOut() {
        accountSessionRepository.updateSession(null)
        signDataSource.signOut()
    }
}


sealed interface LoginState {
    data object Success : LoginState
    data object NotVerified : LoginState
    data class Failed(val exception: Throwable) : LoginState
}