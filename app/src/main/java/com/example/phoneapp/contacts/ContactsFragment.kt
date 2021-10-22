package com.example.phoneapp.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.example.phoneapp.ContactsAdapter
import com.example.phoneapp.databinding.FragmentContactsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.security.Permission


@AndroidEntryPoint
class ContactsFragment : Fragment() {

    lateinit var binding: FragmentContactsBinding

    val viewModel by viewModels<ContactsViewModel>()

    private val permissionLauncher =registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            binding.noPermission.visibility = View.GONE
            viewModel.getcontacts(requireContext().contentResolver)
        }
        else{
            binding.noPermission.visibility = View.VISIBLE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            binding.noPermission.visibility = View.GONE
            viewModel.getcontacts(requireContext().contentResolver)
        }
        else{
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
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
        viewModel.list.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                binding.noBlocks.visibility = View.GONE
                adpter.submitList(it.toList())
            }
            else{
                binding.noBlocks.visibility = View.VISIBLE
            }
        }

    }

}