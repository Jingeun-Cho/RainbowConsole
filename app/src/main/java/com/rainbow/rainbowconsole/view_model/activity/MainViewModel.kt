package com.rainbow.rainbowconsole.view_model.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO
import com.rainbow.rainbowconsole.model.data_class.LessonDTO

class MainViewModel : ViewModel(){
    private val lessonController = AppConfig.lessonController
    private val branchController = AppConfig.branchController
    private val lessonData : MutableLiveData<ArrayList<LessonDTO>> by lazy {
        MutableLiveData<ArrayList<LessonDTO>>(arrayListOf())
    }

    private val branchStatusData : MutableLiveData<BranchStatusDTO> by lazy {
        MutableLiveData<BranchStatusDTO>()
    }

    private var updateResultData : Boolean = false

    fun getTodayLesson(today : Long) : MutableLiveData<ArrayList<LessonDTO>>{
        lessonController.searchByPeriod(today, today + 24 * 60 * 60 * 1000 -1)
            .addSnapshotListener { querySnapshot, error ->
                if(error != null || querySnapshot == null) return@addSnapshotListener
                lessonData.value = querySnapshot.toObjects(LessonDTO::class.java) as ArrayList
            }

        return lessonData
    }

    //All Branch
    fun getAllBranchStatus(){

    }


    fun getBranchStatus(branch : String) : MutableLiveData<BranchStatusDTO>{
        branchController.getBranchStatus(branch)
            .addSnapshotListener { querySnapshot, error ->
                if (querySnapshot == null || error != null) return@addSnapshotListener
                branchStatusData.value = querySnapshot.toObject(BranchStatusDTO::class.java)

            }

        return branchStatusData
    }

    fun updateBranchStatus(branch: String, status : String) : Boolean {
        Log.d("updateBranchStatus", "updateBranchStatus: 진입")
        branchController.changeBranchStatus(branch, status)
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
        branchController.changeBranchStatus(branch, status)
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