package com.rainbow.rainbowconsole.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rainbow.rainbowconsole.adapter.MemberRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.controller.MemberController
import com.rainbow.rainbowconsole.databinding.FragmentMemberBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberFragment : Fragment() {

    private var binding : FragmentMemberBinding? = null
    private val memberController : MemberController = AppConfig.memberController

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentMemberBinding.inflate(inflater, container, false)
        val branch = arguments?.getString("branch")
        initView(branch!!)
        return binding?.root
    }

    private fun initView(branch : String){
        initRecyclerView(branch)
    }

    private fun initRecyclerView(branch : String){
        CoroutineScope(Dispatchers.Main).launch {
            val memberItems = if(branch == "전체")memberController.searchAll().await() else memberController.searchByBranch(branch).await()
            Log.d("MemberFragment", "initRecyclerView: ${memberItems}")
            binding!!.recyclerMemberList.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = MemberRecyclerViewAdapter(memberItems)
            }
        }

    }
}