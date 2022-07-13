package com.rainbow.rainbowconsole.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.databinding.FragmentSettingBinding

class SettingDialogFragment : PreferenceFragmentCompat() {

    private var binding : FragmentSettingBinding? = null
    private var optionRemainDate = 0
    private var optionRemainMembership = 0
    private var optionSideBarItem = 0
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dateEditTextPreference = preferenceManager.findPreference<EditTextPreference>("notification_remain_day")
        val membershipEditPreference = preferenceManager.findPreference<EditTextPreference>("notification_remain_membership")
        membershipEditPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }
        dateEditTextPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }
//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
//        binding = FragmentSettingBinding.inflate(inflater, container, false)
//        return binding?.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // SharedPreference에서 값 호출
//        initView()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val width = resources.displayMetrics.widthPixels * 0.5
//        val height = resources.displayMetrics.heightPixels * 0.7
//        dialog?.window?.setLayout(width.toInt(), height.toInt())
//    }
//
//    private fun initView(){
//        val sideBarSelectItems = resources.getStringArray(R.array.setting_side_bar)
//        val spinnerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sideBarSelectItems)
//        binding?.spinnerSideBar?.apply {
//            adapter = spinnerAdapter
//            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//
//            }
//        }
//    }

}