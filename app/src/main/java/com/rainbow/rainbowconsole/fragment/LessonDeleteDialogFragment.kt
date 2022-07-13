package com.rainbow.rainbowconsole.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.controller.LessonController
import com.rainbow.rainbowconsole.databinding.DialogAlertBinding

class LessonDeleteDialogFragment : DialogFragment(){

    private var binding : DialogAlertBinding? = null
    private val lessonController : LessonController =  AppConfig.lessonController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAlertBinding.inflate(inflater, container, false)
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
                lessonController.deleteLesson(documentId)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}