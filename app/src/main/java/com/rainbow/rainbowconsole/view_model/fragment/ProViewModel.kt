package com.rainbow.rainbowconsole.view_model.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProViewModel : ViewModel(){
    private val proRepository = AppConfig.proRepository
    private val proItems : MutableLiveData<ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>>> = MutableLiveData()

    fun observeProItems() : MutableLiveData<ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>>> = proItems

    fun getProItems(branch : String){
        if (branch == "전체"){
            proRepository.findAllByBranch().get().addOnSuccessListener {
                if(it == null) return@addOnSuccessListener
                val item : ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>> = arrayListOf()
                val proList = it.toObjects(ManagerDTO::class.java) as ArrayList<ManagerDTO>
                Log.d("ProViewModel", "getProItems: ${proList}")
                CoroutineScope(Dispatchers.IO).launch {
                    proList.forEach { pro ->
                        Log.d("ProViewModel", "getProItems: ${pro.uid}/ ${pro.name}")
                        val schedule = proRepository.findProScheduleByUid(pro.uid!!).get().await().toObject(ManagerScheduleDTO::class.java)
                        if(schedule == null) item.add(Pair(pro, ManagerScheduleDTO()))
                        else item.add(Pair(pro, schedule))

                    }
                    proItems.postValue(item)
                }

            }
        }
        else{
            proRepository.findByBranch(branch).get().addOnSuccessListener {
                if(it == null) return@addOnSuccessListener
                val item : ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>> = arrayListOf()
                val proList = it.toObjects(ManagerDTO::class.java) as ArrayList<ManagerDTO>
                CoroutineScope(Dispatchers.IO).launch {
                    proList.forEach { pro ->
                        val schedule = proRepository.findProScheduleByUid(pro.uid!!).get().await().toObject(ManagerScheduleDTO::class.java)
                        if(schedule == null) item.add(Pair(pro, ManagerScheduleDTO()))
                        else item.add(Pair(pro, schedule))
                    }
                    proItems.postValue(item)
                }

            }
        }
    }
}