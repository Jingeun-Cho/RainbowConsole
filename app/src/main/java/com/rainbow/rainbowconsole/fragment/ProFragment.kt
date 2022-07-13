package com.rainbow.rainbowconsole.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rainbow.rainbowconsole.adapter.ProRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.controller.LessonController
import com.rainbow.rainbowconsole.controller.ProController
import com.rainbow.rainbowconsole.databinding.FragmentProBinding
import com.zzoin.temprainbow.model.ManagerDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProFragment : Fragment(){
    private var binding : FragmentProBinding? = null
    private val proController : ProController = AppConfig.proController
    private val lessonController : LessonController = AppConfig.lessonController
    private lateinit var currentBranch : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       currentBranch = arguments?.getString("branch")!!
        initView(currentBranch)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentProBinding.inflate(inflater, container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //뷰 동작
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initView(branch : String){
        CoroutineScope(Dispatchers.Main).launch {
            //Branch 선택 관련 옵션 필요
            val proItems = if(branch == "전체") proController.getAllPro().await()  else proController.searchProByBranch(branch).await()
//            val schedule = proController.getProSchedule()
            initProRecyclerView(proItems)

        }
    }

    private fun initProRecyclerView(proItems : ArrayList<ManagerDTO>){
        binding!!.recyclerProList.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = ProRecyclerViewAdapter(proItems)
        }
    }


}