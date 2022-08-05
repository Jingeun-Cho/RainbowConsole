package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO

class ProViewModel : ViewModel(){
    private val proRepository = AppConfig.proRepository
    private val proItems : MutableLiveData<ArrayList<ManagerDTO>> = MutableLiveData()

    fun observeProItems() : MutableLiveData<ArrayList<ManagerDTO>> = proItems

    fun getProItems(branch : String){
        if (branch == "전체"){
            proRepository.findAllByBranch().get().addOnSuccessListener {
                if(it == null) return@addOnSuccessListener
                proItems.postValue(it.toObjects(ManagerDTO::class.java) as ArrayList<ManagerDTO>)
            }
        }
        else{
            proRepository.findByBranch(branch).get().addOnSuccessListener {
                if(it == null) return@addOnSuccessListener
                proItems.postValue(it.toObjects(ManagerDTO::class.java) as ArrayList<ManagerDTO>)
            }
        }
    }
}