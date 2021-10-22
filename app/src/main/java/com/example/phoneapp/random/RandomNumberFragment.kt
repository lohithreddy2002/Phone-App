package com.example.phoneapp.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.phoneapp.databinding.FragmentRandomNumberBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomNumberFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentRandomNumberBinding
    val viewModel by viewModels<RandomNumberViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomNumberBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding.block.setOnClickListener {
            if(!binding.phoneNumber.text.isNullOrEmpty() && !binding.name.text.isNullOrEmpty()){
                if(binding.phoneNumber.text!!.length != 10){
                    binding.textInputLayout2.error = "Enter valid Phone Number"
                    binding.textInputLayout.error = ""
                }
                else{
                    binding.textInputLayout2.error = ""
                    binding.textInputLayout.error = ""
                 viewModel.insert(binding.phoneNumber.text.toString(),binding.name.text.toString())
                    dismiss()
                }
            }
            else{
                binding.textInputLayout2.error = "Enter valid Phone Number"
                binding.textInputLayout.error= "Enter valid Name"
            }
        }
    }


}