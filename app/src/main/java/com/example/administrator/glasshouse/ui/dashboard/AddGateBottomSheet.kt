package com.example.administrator.glasshouse.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetAddGateBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddGateBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetAddGateBinding>()

    companion object {
        private lateinit var dashBoardViewModel: DashBoardViewModel
        fun newInstance(dashBoardViewModel: DashBoardViewModel): AddGateBottomSheet {
            this.dashBoardViewModel = dashBoardViewModel
            return AddGateBottomSheet()
        }
    }

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): BottomSheetDialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetAddGateBinding>(
                inflater,
                R.layout.bottom_sheet_add_gate,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


        return dataBinding.root
    }

    override fun onViewCreated(
            view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        dashBoardViewModel.initAddGate()

        binding.viewModel = dashBoardViewModel

        binding.result = dashBoardViewModel.addGate

        addGate()

    }

    private fun addGate() {
        dashBoardViewModel.getAddGateFields()?.observe(viewLifecycleOwner, Observer {
            dashBoardViewModel.addGate(it.id!!, it.name!!)
        })
    }
}