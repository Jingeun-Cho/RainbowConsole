package com.rainbow.rainbowconsole.model.controller

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LoginControllerImpl(private val auth : FirebaseAuth) : LoginController {
    override fun loginByEmailAsync(email: String, password: String): Deferred<FirebaseUser?>  =
        CoroutineScope(Dispatchers.IO).async {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .await()
                    .user

            }
            catch (e : FirebaseException){
                Log.e("EmailLoginRepository", "loginByEmail: ${e.message}")
                null
            }
        }
}