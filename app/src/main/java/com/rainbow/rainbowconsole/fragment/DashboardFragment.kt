package com.rainbow.rainbowconsole.fragment

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.adapter.TodayScheduleRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.createTimeTableRowTime
import com.rainbow.rainbowconsole.controller.LessonController
import com.rainbow.rainbowconsole.controller.ProController
import com.rainbow.rainbowconsole.databinding.FragmentDashboardBinding
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class DashboardFragment : Fragment(){
    private var binding : FragmentDashboardBinding? = null
    private val proController : ProController = AppConfig.proController
    private val lessonController : LessonController = AppConfig.lessonController
    private val TAG = "DashboardFragment"
    private val firestore = AppConfig.firestore
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branch = arguments?.getString("branch", "전체")!!
        initView(branch)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(branch : String){
        CoroutineScope(Dispatchers.IO).launch {
            val proItems = if(branch == "전체") proController.getAllPro().await() else proController.searchProByBranch(branch).await()
            val proUids : ArrayList<String> = arrayListOf()
            val (startTime, endTime) = getToday()
            proItems.forEach { proUids.add(it.uid!!) }
            requireActivity().runOnUiThread {
                proItems.withIndex().forEach { (index, proData) ->
                addTableHead(index, proData)
            } }

            firestore
                .collection("lesson")
                .whereIn("coachUid", proUids)
                .whereGreaterThanOrEqualTo("lessonDateTime", startTime)
                .whereLessThanOrEqualTo("lessonDateTime", endTime)
                .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if(error != null || querySnapshot == null){
                        Log.e(TAG, "initView: ${error?.message}", )
                        return@addSnapshotListener
                    }
                    val todayLessonItems : ArrayList<LessonDTO> = arrayListOf()
                    val documentIds : ArrayList<String> = arrayListOf()
                    todayLessonItems.clear()
                    documentIds.clear()
                    val totalLesson = todayLessonItems.size
                    var completeLesson = 0
                    querySnapshot.forEach {
                        val item = it.toObject(LessonDTO::class.java)
                        todayLessonItems.add(item)
                        documentIds.add(it.id)
                        if(item.lessonNote!!.isNotEmpty()) completeLesson ++;
                    }
                    requireActivity().runOnUiThread {
                        binding!!.textTotalLesson.text = "레슨 예정 : ${totalLesson}회"
                        binding!!.textCompleteLesson.text = "레슨 완료 : ${completeLesson}회"
                        binding!!.recyclerTd.apply {
                            layoutManager = LinearLayoutManager(requireContext(),  LinearLayoutManager.VERTICAL,  false )
                            adapter = TodayScheduleRecyclerViewAdapter( proItems, todayLessonItems, documentIds ,startTime )
                        }
                    }
//            val todayLessonItems = lessonController.searchByUidWithPeriod(startTime, endTime, proUids).await()
//            Log.d(TAG, "initView: items.size : ${todayLessonItems.size} / ${todayLessonItems}")

            }
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

    private fun drawRecyclerView(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    private fun getToday() : Pair<Long, Long>{
        val offset = ZoneOffset.systemDefault()
        val today = LocalDate.now().atStartOfDay().atZone(offset).toInstant().toEpochMilli()
        val timeOffset = 24 * 60 * 60 * 1000 - 1
        return Pair(today, today + timeOffset)
    }
}