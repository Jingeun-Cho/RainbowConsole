package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.Deferred

interface ProController {

    fun searchProByUid(uid : String) : DocumentReference
    fun searchProByName(name : String) : Deferred<ManagerDTO?>
    fun searchProByBranch(branch : String) : Query?
    fun getAllPro() : Query?
    fun getProSchedule(uid: String): DocumentReference

    fun updatePro(pro : ManagerDTO, proSchedule: ManagerScheduleDTO) : Task<Transaction>?
}