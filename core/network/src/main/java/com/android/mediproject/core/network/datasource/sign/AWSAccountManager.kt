package com.android.mediproject.core.network.datasource.sign

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool

open class AWSAccountManager(
    protected val userPool: CognitoUserPool,
)