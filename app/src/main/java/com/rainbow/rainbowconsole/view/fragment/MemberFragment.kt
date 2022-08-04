package com.rainbow.rainbowconsole.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.MemberRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.controller.MemberController
import com.rainbow.rainbowconsole.databinding.FragmentMemberBinding
import com.rainbow.rainbowconsole.view_model.fragment.MemberViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

class MemberFragment : Fragment() {

    private var binding : FragmentMemberBinding? = null
    private lateinit var memberViewModel : MemberViewModel

    private val offset = ZoneOffset.systemDefault()
    private val today = LocalDate.now().atStartOfDay(offset).toInstant().toEpochMilli()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member, container, false)
        memberViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MemberViewModel::class.java)
        binding?.apply {
            lifecycleOwner = requireActivity()
            memberViewModel = memberViewModel
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branch = arguments?.getString("branch", "")!!
        initViewModel(branch)
        initView()
    }

    private fun initViewModel(branch : String){
        memberViewModel.getUserItems(branch)
    }
    private fun initView(){
        initRecyclerView(today)
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView( today : Long){
        binding!!.recyclerMemberList.layoutManager = GridLayoutManager(requireContext(), 3)
        memberViewModel.observeMemberData().observe(requireActivity()){ memberItems ->
            val totalMembers = memberItems.size
            var remainMembers : Int = 0
            memberItems.forEach { if((it.lessonMembership - it.lessonMembershipUsed) > 10 || (it.lessonMembershipEnd - today)/ (24 * 60 * 60 * 1000) > 30 ) remainMembers++ }
            binding?.recyclerMemberList?.adapter = MemberRecyclerViewAdapter(memberItems)
            binding?.textTotalMember?.text = "총 회원 : ${totalMembers}명"
            binding?.textDeadline?.text = "마감 예정 회원 : ${remainMembers}명"
        }
    }
}