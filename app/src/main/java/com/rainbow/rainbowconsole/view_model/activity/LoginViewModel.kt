package com.rainbow.rainbowconsole.view_model.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO

class LoginViewModel : ViewModel(){
    private val proRepository = AppConfig.proRepository
    private val proInformation : MutableLiveData<ManagerDTO?> = MutableLiveData()
    private val loginRepository = AppConfig.loginRepository
    private val loginInformation : MutableLiveData<FirebaseUser?> = MutableLiveData()

    fun observeManagerInformation() : MutableLiveData<ManagerDTO?> = proInformation
    fun getLoginInformation() : MutableLiveData<FirebaseUser?> = loginInformation

    fun loginByEmailPassword(email : String, password : String){
        loginRepository.loginByEmailAsync(email, password)
            .addOnSuccessListener {
                loginInformation.postValue(it.user)
            }
            .addOnFailureListener {
                loginInformation.postValue(null)
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