package com.rainbow.rainbowconsole.view.fragment

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.TodayScheduleRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.controller.LessonController
import com.rainbow.rainbowconsole.model.controller.ProController
import com.rainbow.rainbowconsole.databinding.FragmentDashboardBinding
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.view_model.fragment.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

class DashboardFragment : Fragment(){
    private var binding : FragmentDashboardBinding? = null
    private lateinit var dashboardViewModel : DashboardViewModel
    private val proController : ProController = AppConfig.proController

    companion object{
        const val TAG = "DashboardFragment"
        const val TIME_OFFSET = 24 * 60 * 60 * 1000 - 1
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false)
        dashboardViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(DashboardViewModel::class.java)

        binding?.apply {
            lifecycleOwner = requireActivity()
            dashboardViewModel = dashboardViewModel
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branch = arguments?.getString("branch", "전체")!!
        val startTime= getToday()
        dashboardViewModel.getProItems(branch)
        dashboardViewModel.observeProItems().observe(requireActivity()){ proItems ->
            val proUids: ArrayList<String> = arrayListOf()

            proItems.withIndex().forEach { (index, proData) ->
                addTableHead(index, proData)
                proUids.add(proData.uid!!)
            }
            addSnapshotListener(startTime, startTime + TIME_OFFSET, proUids)
            initView(proItems, startTime)
        }
    }

    private fun addSnapshotListener(startTime : Long, endTime : Long, proUids : ArrayList<String>){
        dashboardViewModel.getLessonItems(startTime, endTime, proUids)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(proItems : ArrayList<ManagerDTO>, startTime : Long){
        binding!!.recyclerTd.layoutManager = LinearLayoutManager(requireContext(),  LinearLayoutManager.VERTICAL,  false )

        dashboardViewModel.observeLessonItem().observe(requireActivity()){(todayLessonItems, documentIds) ->
            var completeLesson = 0
            val totalLesson = todayLessonItems.size
            todayLessonItems.forEach { if(it.lessonNote.isNullOrEmpty()) completeLesson++ }

            binding!!.textTotalLesson.text = "레슨 예정 : ${totalLesson}회"
            binding!!.textCompleteLesson.text = "레슨 완료 : ${completeLesson}회"
            binding!!.recyclerTd.adapter = TodayScheduleRecyclerViewAdapter( proItems, todayLessonItems, documentIds ,startTime )
        }
    }

    private fun addTableHead(index : Int, proData : ManagerDTO){
        val textView = TextView(requireContext())
        val layoutParams = LinearLayout.LayoutParams( 250, LinearLayout.LayoutParams.MATCH_PARENT)

        layoutParams.marginStart = 10
        layoutParams.marginEnd = 10
        textView.text = proData.name
        textView.textSize = 18f
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        textView.gravity = Gravity.CENTER
        textView.setTypeface(null, Typeface.NORMAL)
        textView.id = index

        binding!!.layoutTh.addView(textView, layoutParams)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    private fun getToday() : Long{
        val offset = ZoneOffset.systemDefault()
        val today = LocalDate.now().atStartOfDay().atZone(offset).toInstant().toEpochMilli()

        return today
    }
}