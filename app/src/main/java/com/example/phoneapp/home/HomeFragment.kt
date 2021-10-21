package com.example.phoneapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneapp.R
import com.example.phoneapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var adpter: BlockedContactAdapter
    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllBlockedContacts()
    }

    override fun onStart() {
        super.onStart()
        adpter = BlockedContactAdapter {
            viewModel.deleteNumber(it)
        }
        binding.blockedNumList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adpter
        }
        viewModel.blockedNumbers.observe(viewLifecycleOwner, {
            adpter.submitList(it)
        })

        binding.addBlockContacts.setOnClickListener {
            findNavController().navigate(R.id.contactsFragment)
        }
        binding.addBlockRandom.setOnClickListener {
            findNavController().navigate(R.id.randomNumberFragment)
        }
    }
}