package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.UserDTO

class MemberViewModel : ViewModel(){
    private val memberRepository =  AppConfig.memberRepository
    private val memberItemData : MutableLiveData<ArrayList<UserDTO>> by lazy {
            MutableLiveData()
        }
    fun getUserItems(branch : String){
        if(branch == "전체"){
            memberRepository.findAll()
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null) return@addSnapshotListener

                    memberItemData.value = querySnapshot.toObjects(UserDTO::class.java) as ArrayList
                }

        }
        else{
            memberRepository.finByBranch(branch)
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null) return@addSnapshotListener
                    memberItemData.value = querySnapshot.toObjects(UserDTO::class.java) as ArrayList
                }
        }
    }
    fun observeMemberData() : MutableLiveData<ArrayList<UserDTO>> = memberItemData

}