package com.example.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calculator.databinding.FragmentCalculatorDisplayBinding
import com.example.calculator.model.CalculatorViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CalculatorDisplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalculatorDisplayFragment : Fragment() {

    private var binding: FragmentCalculatorDisplayBinding? = null
    private val sharedViewModel: CalculatorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCalculatorDisplayBinding.inflate(inflater, container, false)
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

}