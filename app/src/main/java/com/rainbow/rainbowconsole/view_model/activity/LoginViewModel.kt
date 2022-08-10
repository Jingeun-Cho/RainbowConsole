package com.rainbow.rainbowconsole.view_model.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class LoginViewModel : ViewModel(){
    private val proRepository = AppConfig.proRepository
    private val proInformation : MutableLiveData<ManagerDTO?> = MutableLiveData()
    private val loginRepository = AppConfig.loginRepository
    private val loginInformation : MutableLiveData<Pair<FirebaseUser?, ManagerDTO?>> = MutableLiveData()

    fun observeManagerInformation() : MutableLiveData<ManagerDTO?> = proInformation
    fun getLoginInformation() : MutableLiveData<Pair<FirebaseUser?, ManagerDTO?>> = loginInformation

    fun loginByEmailPassword(email : String, password : String){
        CoroutineScope(Dispatchers.IO).launch {
            val login = loginRepository.loginByEmailAsync(email, password).await().user
            val manager = proRepository.findByEmail(email).get().await().toObjects(ManagerDTO::class.java).first()
            loginInformation.postValue(Pair(login, manager))
//                    .addOnSuccessListener {
//                        loginInformation.postValue(it.user)
//                    }
//                    .addOnFailureListener {
//                        loginInformation.postValue(null)
//                    }
        }

    }

    fun getManagerByEmail(email : String){
        proRepository.findByEmail(email).get()
            .addOnSuccessListener {
                val item = it.toObjects(ManagerDTO::class.java)[0]
                if(item == null){
                    proInformation.postValue(null)
                }
                else{
                    proInformation.postValue(item)
                }
            }
    }

}