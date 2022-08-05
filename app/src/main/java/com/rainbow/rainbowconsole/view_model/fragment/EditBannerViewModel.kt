package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.BannerDTO

class EditBannerViewModel : ViewModel(){
    private val bannerRepository = AppConfig.bannerRepository
    private val bannerItem : MutableLiveData<BannerDTO> = MutableLiveData()


    fun observeBannerItem() : MutableLiveData<BannerDTO> =
        bannerItem

    fun getBanner(documentId : String){
        bannerRepository.getBanner(documentId)
            .get()
            .addOnSuccessListener {
                bannerItem.postValue(it.toObject(BannerDTO::class.java))
            }
    }
    fun editBanner(banner: BannerDTO, documentId: String) : Task<Transaction>
        = bannerRepository.editBanner(banner, documentId)

    fun addBanner(item : BannerDTO, documentId: String) : Task<Void>
        = bannerRepository.addBanner(item, documentId)


}