package com.rainbow.rainbowconsole.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainbow.rainbowconsole.view.adapter.BannerRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.databinding.FragmentBannerSettingBinding

class BannerSettingFragment : Fragment(){
    private var binding : FragmentBannerSettingBinding? = null
    private val editBannerFragment = AppConfig.editBannerDialogFragment

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentBannerSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    fun initView(){
        binding?.btnAddBanner?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 0)
            editBannerFragment.arguments = bundle
            editBannerFragment.show(requireActivity().supportFragmentManager, "addBanner")
        }

        binding?.recyclerBannerList?.apply {
            adapter = BannerRecyclerViewAdapter()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}