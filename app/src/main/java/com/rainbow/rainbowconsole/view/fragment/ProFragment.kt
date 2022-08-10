package com.rainbow.rainbowconsole.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.ProRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.controller.LessonController
import com.rainbow.rainbowconsole.model.controller.ProController
import com.rainbow.rainbowconsole.databinding.FragmentProBinding
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import com.rainbow.rainbowconsole.view_model.fragment.ProViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProFragment : Fragment(){
    private var binding : FragmentProBinding? = null
    private lateinit var proViewModel : ProViewModel
    private lateinit var currentBranch : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       currentBranch = arguments?.getString("branch")!!
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro , container, false )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proViewModel = ViewModelProvider(this).get(ProViewModel::class.java)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            proViewModel = proViewModel
        }
        proViewModel.getProItems(currentBranch)
        //뷰 동작
        initView(currentBranch)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initView(branch : String){
        binding!!.recyclerProList.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = ProRecyclerViewAdapter()
        }
        proViewModel.observeProItems().observe(viewLifecycleOwner){ proItems ->
            (binding!!.recyclerProList.adapter as ProRecyclerViewAdapter).getData(proItems)
        }
    }

}