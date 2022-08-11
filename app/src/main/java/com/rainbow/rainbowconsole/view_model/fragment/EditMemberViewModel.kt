package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.UserDTO

class EditMemberViewModel : ViewModel(){

    private val memberItem : MutableLiveData<Pair<UserDTO?, String>> = MutableLiveData()
    private val memberRepository = AppConfig.memberRepository

    fun observeMemberItem() : MutableLiveData<Pair<UserDTO?, String>> = memberItem

    fun getMemberItem(uid : String){
        memberRepository.findByUid(uid)
            .get()
            .addOnSuccessListener {
                memberItem.postValue( Pair(it.documents[0].toObject(UserDTO::class.java), it.documents[0].id) )
            }
    }

    fun updateMemberItem(userItem : UserDTO, documentId : String) : Task<Transaction> =
        memberRepository.updateUser(userItem, documentId)
}