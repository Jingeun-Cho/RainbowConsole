package com.rainbow.rainbowconsole.model.controller

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred

interface LoginController {
    fun loginByEmailAsync(email : String, password : String) : Deferred<FirebaseUser?>
}