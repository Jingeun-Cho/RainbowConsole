package com.rainbow.rainbowconsole.model.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.rainbow.rainbowconsole.model.data_class.BannerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class BannerRepositoryImpl(private val firestore : FirebaseFirestore) : BannerRepository {


    override fun getBannerList() : CollectionReference =
        firestore
            .collection("banner")


    override fun getBanner(documentId: String): DocumentReference =
        firestore
            .collection("banner")
            .document(documentId)


    override fun addBanner(banner : BannerDTO, documentId: String) : Task<Void>{
        return firestore
            .collection("banner")
            .document(documentId)
            .set(banner)
    }

    override fun deleteBanner(documentId: String): Task<Transaction> {
        val target = firestore.collection("banner").document(documentId)
        return firestore.runTransaction { transaction ->
            transaction.delete(target)
        }
    }

    override fun editBanner(banner : BannerDTO, documentId: String): Task<Transaction> {
        val target = firestore.collection("banner").document(documentId)
        return firestore.runTransaction { transaction ->
            transaction.set(target, banner)
        }
    }
}