package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO

interface BranchController {

    fun getBranchStatus(branch: String) : DocumentReference
    fun changeBranchStatus(branch : Array<String>, status : String) : Task<Transaction>
    fun changeBranchStatus(branch : String, status: String) : Task<Transaction>


}