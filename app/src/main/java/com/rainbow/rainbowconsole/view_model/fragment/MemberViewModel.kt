package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.UserDTO

class MemberViewModel : ViewModel(){
    private val memberController =  AppConfig.memberController
    private val memberItemData : MutableLiveData<ArrayList<UserDTO>> by lazy {
            MutableLiveData()
        }
    fun getUserItems(branch : String) : MutableLiveData<ArrayList<UserDTO>>{
        if(branch == "전체"){
            memberController.searchAll()
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null) return@addSnapshotListener

                    memberItemData.value = querySnapshot.toObjects(UserDTO::class.java) as ArrayList
                }

        }
        else{
            memberController.searchByBranch(branch)
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null) return@addSnapshotListener
                    memberItemData.value = querySnapshot.toObjects(UserDTO::class.java) as ArrayList
                }

        }
        return memberItemData
    }
}