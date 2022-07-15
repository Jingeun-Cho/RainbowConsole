package com.rainbow.rainbowconsole.model.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.rainbow.rainbowconsole.model.data_class.BranchStatusDTO
import kotlinx.coroutines.tasks.await

class BranchRepositoryImpl(private val firestore: FirebaseFirestore) : BranchRepository{

    override fun getStatus(branch: String) : DocumentReference =
            firestore.collection("branch")
                .document(branch)




    override fun updateStatus(branch : Array<String>, status: String) : Task<Transaction>{

        val branchItems : ArrayList<DocumentReference> = arrayListOf()

        branch.forEach { branchItems.add(firestore.collection("branch").document(it)) }

        return firestore.runTransaction { transaction ->
            branchItems.forEach { target ->
                transaction.update(target, "status", status)
            }
            transaction
        }

    }

    override fun updateStatus(branch: String, status: String) : Task<Transaction> {
        val target = firestore.collection("branch").document(branch)
        return firestore.runTransaction { transaction ->
            transaction.update(target, "status", status)
        }
    }
}