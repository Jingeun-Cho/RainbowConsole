package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.repository.ProRepository
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.*

class ProControllerImpl(private val proRepository : ProRepository) : ProController{

    override fun searchProByUid(uid: String): Deferred<ManagerDTO?> = proRepository.findByUid(uid)

    override fun searchProByName(name: String): Deferred<ManagerDTO?> = proRepository.findByName(name)

    override fun searchProByBranch(branch: String): Query? = proRepository.findByBranch(branch)

    override fun getAllPro(): Query? = proRepository.findAllByBranch()

    override fun getProSchedule(uid : String): Deferred<ManagerScheduleDTO> = proRepository.findProScheduleByUid(uid)
    override fun updatePro(pro: ManagerDTO, proSchedule : ManagerScheduleDTO): Task<Transaction>? = proRepository.updatePro(pro, proSchedule)


}