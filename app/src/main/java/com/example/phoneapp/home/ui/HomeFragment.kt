package com.example.phoneapp.home.ui

import android.Manifest
import android.app.role.RoleManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneapp.R
import com.example.phoneapp.databinding.FragmentHomeBinding
import com.example.phoneapp.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var adpter: BlockedContactAdapter
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()


    @RequiresApi(Build.VERSION_CODES.Q)
    private val startActivityForRole =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    binding.noPermission.visibility = View.GONE
                    binding.addBlockContacts.isEnabled = true
                    binding.addBlockRandom.isEnabled = true
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    Snackbar.make(binding.root, "Can not Function properly", Snackbar.LENGTH_SHORT)
                        .setAction("retry") {
                            getRole()
                        }.show()

                    binding.noPermission.visibility = View.VISIBLE
                    binding.addBlockContacts.isEnabled = false
                    binding.addBlockRandom.isEnabled = false
                }
            }
        }
    private val permissionList = listOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    )


    private val startActivityForPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (keys in it.keys) {
                if (it[keys] != true) {
                    Snackbar.make(binding.root, "Can not Function properly", Snackbar.LENGTH_SHORT)
                        .setAction("retry") {
                            requestPermission(permissionList)
                        }.show()
                    binding.noPermission.visibility = View.VISIBLE
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllBlockedContacts()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getRole()
        } else {
            requestPermission(permissionList)
        }

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
            if (it.isNotEmpty()) {
                adpter.submitList(it)
            } else {
                binding.noBlocks.visibility = View.VISIBLE
            }
        })

        binding.addBlockContacts.setOnClickListener {
            findNavController().navigate(R.id.contactsFragment)
        }
        binding.addBlockRandom.setOnClickListener {
            findNavController().navigate(R.id.randomNumberFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getRole() {
        val roleManager =
            requireActivity().getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
        if(roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)){
            binding.noPermission.visibility = View.GONE

        }
        else {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            startActivityForRole.launch(intent)
        }
    }

    private fun requestPermission(permissionList: List<String>) {
        val list = mutableListOf<String>()
        for (i in permissionList.indices) {
            list.add(permissionList[i])
        }
        if (list.isNotEmpty()) {
            startActivityForPermission.launch(list.toTypedArray())
        }
        else{
            binding.noPermission.visibility = View.GONE

        }
    }

}