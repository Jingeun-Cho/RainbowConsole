package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.ManagerScheduleDTO
import kotlinx.coroutines.Deferred

interface ProController {

    fun searchProByUid(uid : String) : Deferred<ManagerDTO?>
    fun searchProByName(name : String) : Deferred<ManagerDTO?>
    fun searchProByBranch(branch : String) : Deferred<ArrayList<ManagerDTO>>
    fun getAllPro() : Deferred<ArrayList<ManagerDTO>>
    fun getProSchedule(uid: String): Deferred<ManagerScheduleDTO>

    fun updatePro(pro : ManagerDTO, proSchedule: ManagerScheduleDTO) : Task<Transaction>?
}