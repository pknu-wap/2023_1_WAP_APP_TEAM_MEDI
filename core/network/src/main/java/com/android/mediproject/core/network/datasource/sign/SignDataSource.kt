package com.android.mediproject.core.network.datasource.sign

import com.android.mediproject.core.model.sign.SignInResponse
import com.android.mediproject.core.model.sign.SignUpResponse
import com.android.mediproject.core.model.requestparameters.LoginParameter
import com.android.mediproject.core.model.requestparameters.SignUpParameter
import kotlinx.coroutines.flow.Flow

interface SignDataSource {
    fun logIn(loginParameter: LoginParameter): Flow<Result<SignInResponse>>
    fun signUp(signUpParameter: SignUpParameter): Flow<Result<SignUpResponse>>
    fun signOut()
}
