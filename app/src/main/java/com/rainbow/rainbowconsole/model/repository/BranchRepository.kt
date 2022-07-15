package com.rainbow.rainbowconsole.model.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO

interface BranchRepository {

    fun getStatus(branch: String) : DocumentReference

    fun updateStatus(branch : Array<String>,status : String): Task<Transaction>

    fun updateStatus(branch : String, status: String): Task<Transaction>
}