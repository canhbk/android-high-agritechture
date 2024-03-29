package com.mandevices.iot.agriculture.ui.register

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.binding.FragmentDataBindingComponent
import com.mandevices.iot.agriculture.databinding.FragmentRegisterBinding
import com.mandevices.iot.agriculture.di.Injectable
import com.mandevices.iot.agriculture.util.autoCleared
import com.mandevices.iot.agriculture.vo.Const
import com.mandevices.iot.agriculture.vo.Status
import io.paperdb.Paper
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var registerViewModel: RegisterViewModel

    var binding by autoCleared<FragmentRegisterBinding>()

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentRegisterBinding>(
                inflater,
                R.layout.fragment_register,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding

        registerViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RegisterViewModel::class.java)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = registerViewModel

        register()

        handleResultRegister()

    }

    private fun register() {
        registerViewModel.getRegisterFields()?.observe(viewLifecycleOwner, Observer {
            registerViewModel.register(it.email!!, it.name!!, it.password!!)
        })
    }

    private fun handleResultRegister() {
        registerViewModel.user.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> binding.loading = true
                Status.SUCCESS -> {
                    binding.loading = false
                    Paper.book().write(Const.USER_ID, it.data!!.id)
                    binding.root.findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
                Status.ERROR -> {
                    binding.loading = false
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}