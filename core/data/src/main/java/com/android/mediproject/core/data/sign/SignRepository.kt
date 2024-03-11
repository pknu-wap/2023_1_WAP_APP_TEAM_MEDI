package com.android.mediproject.core.data.sign


import com.android.mediproject.core.model.sign.LoginParameter
import com.android.mediproject.core.model.sign.SignUpParameter

interface SignRepository {
    suspend fun login(loginParameter: LoginParameter): LoginState
    suspend fun signUp(signUpParameter: SignUpParameter): Result<Boolean>
    suspend fun signOut()
}
