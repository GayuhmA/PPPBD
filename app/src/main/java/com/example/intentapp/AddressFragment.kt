package com.example.intentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.intentapp.databinding.FragmentAddressBinding

class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val provinces = resources.getStringArray(R.array.provinces)
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, provinces
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerProvinces.adapter = adapter

        val navController = findNavController()
        val previousEntry = navController.previousBackStackEntry
        // Restore the last confirmed province when opening the selector again.
        if (savedInstanceState == null) {
            val address = previousEntry?.savedStateHandle?.get<String>(ADDRESS_KEY)
            val selectedIndex = provinces.indexOf(address)
            if (selectedIndex >= 0) binding.spinnerProvinces.setSelection(selectedIndex)
        }

        binding.btnDone.setOnClickListener {
            if (navController.currentDestination?.id == R.id.addressFragment) {
                previousEntry?.savedStateHandle?.set(
                    ADDRESS_KEY, binding.spinnerProvinces.selectedItem.toString()
                )
                navController.navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ADDRESS_KEY = "address"
    }
}
