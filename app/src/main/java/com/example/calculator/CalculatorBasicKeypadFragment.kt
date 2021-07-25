package com.example.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calculator.databinding.FragmentCalculatorBasicKeypadBinding
import com.example.calculator.model.CalculatorViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CalculatorBasicKeypadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalculatorBasicKeypadFragment : Fragment() {


    private var binding: FragmentCalculatorBasicKeypadBinding? = null
    private val sharedViewModel: CalculatorViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding =
            FragmentCalculatorBasicKeypadBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}