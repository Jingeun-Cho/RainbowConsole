package com.rainbow.rainbowconsole.view_model.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DashboardViewModel : ViewModel(){
    private val lessonRepository = AppConfig.lessonRepository
    private val proRepository = AppConfig.proRepository
    private val memberRepository = AppConfig.memberRepository
    private val lessonItem : MutableLiveData<ArrayList<Triple<LessonDTO, String,UserDTO >>> = MutableLiveData()

    private val proItems : MutableLiveData<ArrayList<ManagerDTO>> = MutableLiveData()

    fun getProItems(branch : String){
        if (branch == "전체"){
            proRepository.findAllByBranch().get()
                .addOnSuccessListener {
                    if(it.documents.isEmpty()) return@addOnSuccessListener
                    val items = it.toObjects(ManagerDTO::class.java) as ArrayList
                    proItems.value = items
                }
        }
        else{
            proRepository.findByBranch(branch)
                .get()
                .addOnSuccessListener {
                    if(it == null) return@addOnSuccessListener
                    val items = it.toObjects(ManagerDTO::class.java) as ArrayList
                    proItems.value = items
                }
        }


    }
    fun getLessonItems(start : Long, end : Long, proItems : ArrayList<String>){
        lessonRepository.findByUidWithPeriod(start, end, proItems)
            .addSnapshotListener { querySnapshot, error ->
                if(querySnapshot == null || error != null) return@addSnapshotListener
                val items : ArrayList<Triple<LessonDTO, String,UserDTO>> = arrayListOf()

                CoroutineScope(Dispatchers.IO).launch {
                    querySnapshot.forEach {
                        val item = it.toObject(LessonDTO::class.java)
                        val user = memberRepository.findByUid(item.uid!!).get().await().toObjects(UserDTO::class.java)[0]
                        items.add(Triple(item, it.id, user))
                    }

                    lessonItem.postValue(items)
                }

            }
    }

    fun observeProItems() : MutableLiveData<ArrayList<ManagerDTO>> = proItems

    fun observeLessonItem() :MutableLiveData<ArrayList<Triple<LessonDTO, String,UserDTO >>> = lessonItem



}