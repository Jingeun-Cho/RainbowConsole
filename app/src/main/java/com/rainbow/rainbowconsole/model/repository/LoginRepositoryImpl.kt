package com.rainbow.rainbowconsole.model.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val auth : FirebaseAuth) : LoginRepository {
    override fun loginByEmailAsync(email: String, password: String) : Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

}