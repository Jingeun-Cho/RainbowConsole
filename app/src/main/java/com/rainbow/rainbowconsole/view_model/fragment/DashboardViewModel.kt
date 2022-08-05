package com.rainbow.rainbowconsole.view_model.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel(){
    private val lessonRepository = AppConfig.lessonRepository
    private val proRepository = AppConfig.proRepository

    private val lessonItem : MutableLiveData<Pair<ArrayList<LessonDTO>, ArrayList<String>>> = MutableLiveData(
        Pair(arrayListOf(), arrayListOf()))

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
                val todayLessonItems : ArrayList<LessonDTO> = arrayListOf()
                val documentIds : ArrayList<String> = arrayListOf()

                querySnapshot.forEach {
                    val item = it.toObject(LessonDTO::class.java)
                    todayLessonItems.add(item)
                    documentIds.add(it.id)
                }

                lessonItem.postValue(Pair(todayLessonItems, documentIds))
            }
    }

    fun observeProItems() : MutableLiveData<ArrayList<ManagerDTO>> = proItems

    fun observeLessonItem() : MutableLiveData<Pair<ArrayList<LessonDTO>, ArrayList<String>>> = lessonItem



}