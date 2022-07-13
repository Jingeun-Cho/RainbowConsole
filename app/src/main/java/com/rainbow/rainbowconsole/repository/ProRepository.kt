package com.rainbow.rainbowconsole.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.ManagerScheduleDTO
import kotlinx.coroutines.Deferred

interface ProRepository {
    /**
     * Create By 조진근
     */
    fun findByUid(uid : String) : Deferred<ManagerDTO?>

    fun findByName(name : String) : Deferred<ManagerDTO?>

    fun findByBranch(branch : String) : Deferred<ArrayList<ManagerDTO>>

    fun findAllByBranch() : Deferred<ArrayList<ManagerDTO>>

    fun findProScheduleByUid(uid : String) : Deferred<ManagerScheduleDTO>

    fun updatePro(pro : ManagerDTO, proScheduleDTO: ManagerScheduleDTO): Task<Transaction>?
}