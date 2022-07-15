package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.Deferred

interface ProController {

    fun searchProByUid(uid : String) : Deferred<ManagerDTO?>
    fun searchProByName(name : String) : Deferred<ManagerDTO?>
    fun searchProByBranch(branch : String) : Deferred<ArrayList<ManagerDTO>>
    fun getAllPro() : Deferred<ArrayList<ManagerDTO>>
    fun getProSchedule(uid: String): Deferred<ManagerScheduleDTO>

    fun updatePro(pro : ManagerDTO, proSchedule: ManagerScheduleDTO) : Task<Transaction>?
}