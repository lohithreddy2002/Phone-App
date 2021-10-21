package com.example.phoneapp.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneapp.ContactsAdapter
import com.example.phoneapp.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactsFragment : Fragment() {

    lateinit var binding: FragmentContactsBinding

    val viewModel by viewModels<ContactsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater)
        return binding.root


    }

    override fun onStart() {


        super.onStart()
        val adpter = ContactsAdapter {
            viewModel.insert(it)
            findNavController().navigateUp()
        }
        binding.test.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adpter
        }
        viewModel.getcontacts(requireContext().contentResolver)
        viewModel.list.observe(viewLifecycleOwner) {
            adpter.submitList(it.toList())
        }


    }


}