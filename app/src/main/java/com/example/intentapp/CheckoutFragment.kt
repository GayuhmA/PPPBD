package com.example.intentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.intentapp.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val args: CheckoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtProductName.text = args.productName

        val navController = findNavController()
        navController.getBackStackEntry(R.id.checkoutFragment).savedStateHandle
            .getLiveData<String>(AddressFragment.ADDRESS_KEY)
            .observe(viewLifecycleOwner) { address ->
                binding.edtAddress.setText(address)
            }

        binding.edtAddress.setOnClickListener {
            if (navController.currentDestination?.id == R.id.checkoutFragment) {
                val action = CheckoutFragmentDirections.actionCheckoutFragmentToAddressFragment()
                navController.navigate(action)
            }
        }
        binding.btnDone.setOnClickListener {
            if (navController.currentDestination?.id == R.id.checkoutFragment) {
                navController.navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
