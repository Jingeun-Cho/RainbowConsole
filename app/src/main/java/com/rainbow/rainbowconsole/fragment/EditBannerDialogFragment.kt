package com.rainbow.rainbowconsole.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.databinding.FragmentEditBannerDialogBinding
import com.rainbow.rainbowconsole.model.BannerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditBannerDialogFragment : DialogFragment(){
    private var binding : FragmentEditBannerDialogBinding? = null
    private val bannerController = AppConfig.bannerController

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentEditBannerDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 0 -> Add
        // 1 -> Edit

        val dialogType = arguments?.getInt("type", 0)!!
        binding?.textTitle?.apply {
            text = when(dialogType){
                0 -> "배너 추가"
                1 -> "배너 수정"
                else -> {""}
            }
        }

        var bannerItem : BannerDTO?
        if(dialogType == 1){
            val documentId = arguments?.getString("documentId", "")!!
            CoroutineScope(Dispatchers.Main).launch {
                bannerItem = bannerController.getBanner(documentId).await()

                binding?.inputDescription?.setText(bannerItem?.description)
                binding?.inputDialogTitle?.setText(bannerItem?.dialogTitle)
                binding?.inputImageUrl?.setText(bannerItem?.imageUrl)
                binding?.inputRedirectUrl?.setText(bannerItem?.redirectUrl)


                binding?.btnConfirm?.setOnClickListener {
                    editBanner(bannerItem!!, documentId)
                }
            }
        }
        else{
            binding?.inputDescription?.setText("")
            binding?.inputDialogTitle?.setText("")
            binding?.inputImageUrl?.setText("")
            binding?.inputRedirectUrl?.setText("")
            binding?.btnConfirm?.setOnClickListener {
                bannerItem = BannerDTO()
                addBanner(bannerItem!!)
            }
        }

        binding?.btnBtnCancel?.setOnClickListener {
            dismiss()
        }

    }

    private fun addBanner(bannerItem : BannerDTO){
        val currentTime = System.currentTimeMillis()
        val description = binding?.inputDescription?.text.toString()
        val dialogTitle = binding?.inputDialogTitle?.text.toString()
        val imageUrl = binding?.inputImageUrl?.text.toString()
        val redirectionUrl = binding?.inputRedirectUrl?.text.toString()
        val documentId = "banner_${currentTime}"
        bannerItem.apply {
            this.description = description
            this.dialogTitle = dialogTitle
            this.documentId = documentId
            this.imageUrl = imageUrl
            this.redirectUrl = redirectionUrl
        }
        bannerController.addNewBanner(bannerItem, documentId)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener {
                Snackbar.make(binding!!.root, "배너 등록에 실패 했습니다.", Snackbar.LENGTH_SHORT).show()
            }

    }

    private fun editBanner(bannerItem : BannerDTO, documentId : String){
        val description = binding?.inputDescription?.text.toString()
        val dialogTitle = binding?.inputDialogTitle?.text.toString()
        val imageUrl = binding?.inputImageUrl?.text.toString()
        val redirectionUrl = binding?.inputRedirectUrl?.text.toString()
        bannerItem.apply {
            this.description = description
            this.dialogTitle = dialogTitle
            this.documentId = documentId
            this.imageUrl = imageUrl
            this.redirectUrl = redirectionUrl
        }

        bannerController.editBanner(bannerItem, documentId)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener {
                Snackbar.make(binding!!.root, "배너 수정에 실패 했습니다. 다시 시도 해주세요.", Snackbar.LENGTH_SHORT).show()
            }

    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels * 0.6
        val height = resources.displayMetrics.heightPixels * 0.7

        dialog?.window?.setLayout(width.toInt(), height.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}