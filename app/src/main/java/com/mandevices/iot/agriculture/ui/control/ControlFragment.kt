package com.mandevices.iot.agriculture.ui.control


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.FragmentControlBinding
import com.mandevices.iot.agriculture.databinding.FragmentDashboardBinding
import com.mandevices.iot.agriculture.ui.dashboard.*
import com.mandevices.iot.agriculture.util.AppExecutors
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Const
import com.mandevices.iot.agriculture.vo.Status
import com.mandevices.iot.agriculture.vo.User
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.lang.Exception
import javax.inject.Inject


class ControlFragment : Fragment() {

    //TODO
    private var addBottomSheet: AddGateBottomSheet? = null
    private var deleteBottomSheet: DeleteGateBottomSheet? = null
    private var editBottomSheet: EditGateBottomSheet? = null
    private var userBottomSheet: UserBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var controlViewModel: ControlViewModel


    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentControlBinding>()

    private var adapter by autoCleared<ControlAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val dataBinding = DataBindingUtil.inflate<FragmentControlBinding>(
                inflater,
                R.layout.fragment_control,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        controlViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ControlViewModel::class.java)


        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(topToolbar)
        (activity as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)


        controlViewModel.loadControls()


        binding.setLifecycleOwner(viewLifecycleOwner)

        val userId = Paper.book().read<String>(Const.USER_ID)


        val rvAdapter = GateAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                onDeleteClick = {
                    deleteBottomSheet = DeleteGateBottomSheet.newInstance(
                            gate = it, dashBoardViewModel = dashBoardViewModel)
                    deleteBottomSheet?.show(activity!!.supportFragmentManager, deleteBottomSheet?.tag)
                },
                onEditClick = {
                    editBottomSheet = EditGateBottomSheet.newInstance(gate = it, dashBoardViewModel = dashBoardViewModel)
                    editBottomSheet?.show(activity!!.supportFragmentManager, editBottomSheet?.tag)
                }
        )
        binding.rvListDevice.adapter = rvAdapter
        adapter = rvAdapter
        binding.gates = dashBoardViewModel.gates
        addBottomSheet = AddGateBottomSheet.newInstance(dashBoardViewModel)

        dashBoardViewModel.apply {
            gates.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                    adapter.submitList(it.data)
                }
            })

            addGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        addBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            removeGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        deleteBottomSheet?.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            editGate.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS) {
                    try {
                        editBottomSheet?.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }

        binding.fabAdd.setOnClickListener {

            addBottomSheet?.show(activity!!.supportFragmentManager, addBottomSheet?.tag)

        }

        userViewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                user = it.data!!
            }
        })

        if (userId == null) {
            view.findNavController().navigate(R.id.log_out)
        }

        topToolbar.inflateMenu(R.menu.menu_dashboard)

        binding.topToolbar.findViewById<View>(R.id.action_sync).setOnClickListener {
            dashBoardViewModel.loadGates(true)
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            //                Paper.book().delete(Const.USER_ID)
            userBottomSheet = UserBottomSheet.newInstance(
                    user = user,
                    userViewModel = userViewModel) {
                try {
                    userBottomSheet?.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                view.findNavController().navigate(R.id.log_out)
            }
            userBottomSheet?.show(activity!!.supportFragmentManager, userBottomSheet?.tag)
        }
    }


}