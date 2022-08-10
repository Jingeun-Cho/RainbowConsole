package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.UserDTO

class EditMemberViewModel : ViewModel(){

    private val memberItem : MutableLiveData<UserDTO> = MutableLiveData()
    private val memberRepository = AppConfig.memberRepository

    fun observeMemberItem() : MutableLiveData<UserDTO> = memberItem

    fun getMemberItem(uid : String){
        memberRepository.findByUid(uid)
            .get()
            .addOnSuccessListener {
                memberItem.postValue(it.documents[0].toObject(UserDTO::class.java))
            }
    }

    fun updateMemberItem(userItem : UserDTO, documentId : String) : Task<Transaction> =
        memberRepository.updateUser(userItem, documentId)
}