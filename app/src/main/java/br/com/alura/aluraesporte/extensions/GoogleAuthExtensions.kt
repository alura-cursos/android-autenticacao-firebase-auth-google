package br.com.alura.aluraesporte.extensions

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun Context.googleSignInClient(): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(br.com.alura.aluraesporte.R.string.default_web_client_id))
            .requestEmail()
            .build()
    return GoogleSignIn.getClient(this, gso)
}