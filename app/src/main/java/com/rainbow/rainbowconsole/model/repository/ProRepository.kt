package com.rainbow.rainbowconsole.model.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.Deferred

interface ProRepository {
    /**
     * Create By 조진근
     */
    fun findByUid(uid : String) : DocumentReference

    fun findByName(name : String) : Deferred<ManagerDTO?>

    fun findByEmail(email : String) : Query

    fun findByBranch(branch : String) : Query

    fun findAllByBranch() : Query?

    fun findProScheduleByUid(uid : String) : DocumentReference

    fun updatePro(pro : ManagerDTO, proScheduleDTO: ManagerScheduleDTO): Task<Transaction>?
}