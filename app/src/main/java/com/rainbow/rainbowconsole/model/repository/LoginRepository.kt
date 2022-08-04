package com.rainbow.rainbowconsole.model.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred

interface LoginRepository {
    fun loginByEmailAsync(email : String, password : String) : Task<AuthResult>
}