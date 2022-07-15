package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.repository.BranchRepository
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO

class BranchControllerImpl(private val branchRepository : BranchRepository) : BranchController{

    override fun getBranchStatus(branch: String) : DocumentReference = branchRepository.getStatus(branch)

    override fun changeBranchStatus(branch : Array<String>, status: String): Task<Transaction> = branchRepository.updateStatus(branch, status)

    override fun changeBranchStatus(branch: String, status: String): Task<Transaction> = branchRepository.updateStatus(branch, status)


}