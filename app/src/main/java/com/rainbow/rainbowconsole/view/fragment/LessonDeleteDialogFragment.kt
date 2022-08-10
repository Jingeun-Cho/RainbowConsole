package com.rainbow.rainbowconsole.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.controller.LessonController
import com.rainbow.rainbowconsole.databinding.DialogAlertBinding
import com.rainbow.rainbowconsole.view_model.fragment.LessonViewModel

class LessonDeleteDialogFragment : DialogFragment(){

    private var binding : DialogAlertBinding? = null
    private lateinit var lessonDeleteViewModel : LessonViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        lessonDeleteViewModel = ViewModelProvider(requireActivity()).get(LessonViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_alert, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")
        val message = arguments?.getString("message")
        binding?.textTitle!!.text = title
        binding?.textMessage!!.text = message

        binding?.btnConfirm?.setOnClickListener {
            val documentId = arguments?.getString("documentId")
            if(!documentId.isNullOrEmpty()){
                lessonDeleteViewModel.removeLesson(documentId)
                .addOnSuccessListener {
                    Log.d("lessonDeleteDialogFragment", "onViewCreated: lesson Deleted ")
                }
                .addOnFailureListener {
                    Log.e("lessonDeleteDialogFragment", "onViewCreated: ${it.message}", )
                    throw it
                }
            }
            else{
                Log.d("lessonDeleteDialogFragment", "onViewCreated: lesson documentId is empty ")
            }
            dismiss()

        }
        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels / 3
        val height = resources.displayMetrics.heightPixels / 3
        dialog?.window?.setLayout(width, height)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}