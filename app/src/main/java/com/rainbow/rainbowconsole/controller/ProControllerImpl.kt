package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.repository.ProRepository
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.ManagerScheduleDTO
import kotlinx.coroutines.*

class ProControllerImpl(private val proRepository : ProRepository) : ProController{

    override fun searchProByUid(uid: String): Deferred<ManagerDTO?> = proRepository.findByUid(uid)

    override fun searchProByName(name: String): Deferred<ManagerDTO?> = proRepository.findByName(name)

    override fun searchProByBranch(branch: String): Deferred<ArrayList<ManagerDTO>> = proRepository.findByBranch(branch)

    override fun getAllPro(): Deferred<ArrayList<ManagerDTO>> = proRepository.findAllByBranch()

    override fun getProSchedule(uid : String): Deferred<ManagerScheduleDTO> = proRepository.findProScheduleByUid(uid)
    override fun updatePro(pro: ManagerDTO, proSchedule : ManagerScheduleDTO): Task<Transaction>? = proRepository.updatePro(pro, proSchedule)


}