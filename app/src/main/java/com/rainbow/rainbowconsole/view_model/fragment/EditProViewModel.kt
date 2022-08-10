package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProViewModel : ViewModel(){
    private val proItem : MutableLiveData<Pair<ManagerDTO?, ManagerScheduleDTO?>> = MutableLiveData()
    private val proRepository = AppConfig.proRepository
    fun observeProItem() : MutableLiveData<Pair<ManagerDTO?, ManagerScheduleDTO?>> = proItem

    fun getProItem(uid : String){
        CoroutineScope(Dispatchers.IO).launch {
            val pro = proRepository.findByUid(uid).get().await().toObject(ManagerDTO::class.java)
            val schedule = proRepository.findProScheduleByUid(uid).get().await().toObject(ManagerScheduleDTO::class.java)
            proItem.postValue(Pair(pro, schedule))
        }
    }

    fun updatePro(proItem : ManagerDTO, proSchedule : ManagerScheduleDTO) : Task<Transaction>? =
        proRepository.updatePro(proItem, proSchedule)
}