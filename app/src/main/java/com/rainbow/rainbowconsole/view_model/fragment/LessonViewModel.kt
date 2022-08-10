package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LessonViewModel : ViewModel(){

    private val reserveData : MutableLiveData<Pair<ManagerDTO, ArrayList<UserDTO>>> = MutableLiveData()
    private val proRepository = AppConfig.proRepository
    private val memberRepository = AppConfig.memberRepository
    private val lessonRepository = AppConfig.lessonRepository


    fun observeReserveData() : MutableLiveData<Pair<ManagerDTO, ArrayList<UserDTO>>> = reserveData

    fun getReserveData(uid : String){
        CoroutineScope(Dispatchers.IO).launch {
            val memberList =   memberRepository.findByProUid(uid)
                .get().await().toObjects(UserDTO::class.java) as ArrayList
            val proItem = proRepository.findByUid(uid)
                .get().await().toObject(ManagerDTO::class.java)!!

            reserveData.postValue(Pair(proItem, memberList))
        }
    }

    fun reserveLesson(documentId : String, lessonItem : LessonDTO) : Task<Void> =
        lessonRepository.reserveLesson(documentId, lessonItem)

    fun removeLesson(documentId : String) : Task<Transaction> =
        lessonRepository.deleteLesson(documentId)

}