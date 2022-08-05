package com.rainbow.rainbowconsole.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.BannerRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.databinding.FragmentBannerSettingBinding
import com.rainbow.rainbowconsole.model.data_class.BannerDTO
import com.rainbow.rainbowconsole.view_model.fragment.BannerViewModel

class BannerSettingFragment : Fragment(){
    private var binding : FragmentBannerSettingBinding? = null
    private lateinit var bannerViewModel : BannerViewModel
    private val editBannerFragment = AppConfig.editBannerDialogFragment

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner_setting ,container, false)
        bannerViewModel = ViewModelProvider(this).get(BannerViewModel::class.java)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            bannerViewModel = bannerViewModel
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callSnapshotListener()
        initView()

    }

    private fun callSnapshotListener(){
        bannerViewModel.getBanner()
    }

    fun initView(){
        binding?.btnAddBanner?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 0)
            editBannerFragment.arguments = bundle
            editBannerFragment.show(requireActivity().supportFragmentManager, "addBanner")
        }

        binding?.recyclerBannerList?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        bannerViewModel.observeBannerList().observe(viewLifecycleOwner){
            val bannerItems : ArrayList<BannerDTO> = arrayListOf()
            val bannerIds : ArrayList<String> = arrayListOf()
            it.forEach { item ->
                bannerItems.add(item.toObject(BannerDTO::class.java))
                bannerIds.add(item.id)
            }

            binding?.recyclerBannerList?.adapter = BannerRecyclerViewAdapter(bannerItems, bannerIds, bannerViewModel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}