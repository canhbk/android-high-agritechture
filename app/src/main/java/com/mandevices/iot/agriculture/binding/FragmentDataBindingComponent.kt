package com.mandevices.iot.agriculture.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment


/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(val fragment: Fragment) : DataBindingComponent {

    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}
