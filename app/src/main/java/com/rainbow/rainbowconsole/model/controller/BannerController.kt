package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.BannerDTO
import kotlinx.coroutines.Deferred

interface BannerController {
    fun getBannerList() : CollectionReference?
    fun getBanner(documentId : String ) : DocumentReference
    fun addNewBanner(banner : BannerDTO, documentId : String) : Task<Void>
    fun deleteBanner(documentId : String) : Task<Transaction>
    fun editBanner(banner : BannerDTO, documentId : String): Task<Transaction>
}