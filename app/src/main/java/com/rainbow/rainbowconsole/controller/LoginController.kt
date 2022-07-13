package com.rainbow.rainbowconsole.controller

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred

interface LoginController {
    fun loginByEmailAsync(email : String, password : String) : Deferred<FirebaseUser?>
}