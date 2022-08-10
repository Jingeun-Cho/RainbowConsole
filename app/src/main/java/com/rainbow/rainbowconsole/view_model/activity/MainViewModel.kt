package com.rainbow.rainbowconsole.view_model.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel(){
    private val lessonRepository = AppConfig.lessonRepository
    private val branchRepository = AppConfig.branchRepository
    private val memberRepository = AppConfig.memberRepository
    private val proRepository = AppConfig.proRepository
    private val recentItem : MutableLiveData<Pair<UserDTO, ManagerDTO>> = MutableLiveData()

    private val lessonData : MutableLiveData<ArrayList<Triple<LessonDTO,UserDTO, ManagerDTO>>> = MutableLiveData()

    private val branchStatusData : MutableLiveData<BranchStatusDTO> by lazy {
        MutableLiveData<BranchStatusDTO>()
    }

    private var updateResultData : Boolean = false


    fun getRecentItem() :MutableLiveData<Pair<UserDTO, ManagerDTO>> = recentItem


    fun observeLessonData() : MutableLiveData<ArrayList<Triple<LessonDTO,UserDTO, ManagerDTO>>>{
        return lessonData
    }
    fun observeBranchStatusData() : MutableLiveData<BranchStatusDTO>{
        return branchStatusData
    }

    fun getTodayLesson(today : Long){
            lessonRepository
                .findByPeriod(today, today + 24 * 60 * 60 * 1000 -1)
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null) return@addSnapshotListener
                    val items : ArrayList<Triple<LessonDTO, UserDTO, ManagerDTO>> = arrayListOf()
                    val lessonItems = querySnapshot.toObjects(LessonDTO::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        lessonItems.forEach { item ->
                            val user = memberRepository.findByUid(item.uid!!).get().await().toObjects(UserDTO::class.java)[0]
                            val pro = proRepository.findByUid(item.coachUid!!).get().await().toObject(ManagerDTO::class.java)!!

                            items.add(Triple(item, user, pro))
                        }
                        lessonData.postValue(items)
                    }

                }

    }

    fun getBranchStatus(branch : String){
        branchRepository.getStatus(branch)
            .addSnapshotListener { querySnapshot, error ->
                if (querySnapshot == null || error != null) return@addSnapshotListener
                branchStatusData.value = querySnapshot.toObject(BranchStatusDTO::class.java)
            }
    }

    fun updateBranchStatus(branch: String, status : String) : Boolean {
        Log.d("updateBranchStatus", "updateBranchStatus: 진입")
        branchRepository.updateStatus(branch, status)
            .addOnSuccessListener {
                updateResultData = true
            }
            .addOnFailureListener {
                Log.d("updateBranchStatus", "updateBranchStatus: ${it.message}", )
                updateResultData = false
            }

        return  updateResultData
    }


    fun updateBranchStatus(branch: Array<String>, status : String) : Boolean {
        branchRepository.updateStatus(branch, status)
            .addOnSuccessListener {
                updateResultData = true
            }
            .addOnFailureListener {
                Log.e("updateBranchStatus", "updateBranchStatus: ${it.message}", )
                updateResultData = false
            }

        return  updateResultData
    }


}