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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneapp.ContactsAdapter
import com.example.phoneapp.databinding.FragmentContactsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ContactsFragment : Fragment() {

    lateinit var binding: FragmentContactsBinding

    val viewModel by viewModels<ContactsViewModel>()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                binding.noPermission.visibility = View.GONE
                viewModel.getContacts(requireContext().contentResolver)
            } else {
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

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.noPermission.visibility = View.GONE
            viewModel.getContacts(requireContext().contentResolver)
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onStart() {
        super.onStart()
        val adpter = ContactsAdapter {
            MaterialAlertDialogBuilder(requireContext()).setTitle("test").setPositiveButton("proceed") { a, b ->
                viewModel.insert(it)
                findNavController().navigateUp()
            }.setNegativeButton("cancel") { a, b ->
                a.dismiss()
            }.setMessage("Are you sure you want to block this number").show()

        }
        binding.test.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adpter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllContacts().collectLatest {
                if (it.isNotEmpty()) {
                    binding.noBlocks.visibility = View.GONE
                    adpter.submitList(it)
                } else {
                    binding.noBlocks.visibility = View.VISIBLE
                }
            }
        }
    }

}