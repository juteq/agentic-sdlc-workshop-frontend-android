package com.livly.booking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.livly.booking.databinding.FragmentAmenityAvailabilityBinding
import com.livly.booking.data.model.Result
import com.livly.booking.di.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class AmenityAvailabilityFragment : Fragment(R.layout.fragment_amenity_availability) {
    companion object {
        private const val TAG = "AmenityFragment"
    }

    private val viewModel: AmenityBookingViewModel by viewModels()
    private var _binding: FragmentAmenityAvailabilityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var featureFlagManager: FeatureFlagManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAmenityAvailabilityBinding.bind(view)

        val adapter = AmenityAvailabilityAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.d(TAG, "Feature enabled: ${featureFlagManager.isFeatureEnabled("amenity_availability")}")

        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModel.amenityAvailability.observe(viewLifecycleOwner) { result ->
                Log.d(TAG, "Received result: $result")
                when (result) {
                    is Result.Loading -> {
                        Log.d(TAG, "Loading state")
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                    }
                    is Result.Success -> {
                        Log.d(TAG, "Success with ${result.data.size} items")
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error occurred", result.exception)
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = false

                        val errorMessage = when (result.exception) {
                            is ServerErrorException -> {
                                Log.e(TAG, "Server error: ${result.exception.message}")
                                result.exception.message
                            }
                            is ClientErrorException -> {
                                Log.e(TAG, "Client error: ${result.exception.message}")
                                result.exception.message
                            }
                            is NetworkErrorException -> {
                                Log.e(TAG, "Network error: ${result.exception.message}")
                                result.exception.message
                            }
                            else -> {
                                Log.e(TAG, "Unexpected error: ${result.exception.message}")
                                "An unexpected error occurred. Please try again."
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            Log.d(TAG, "Calling getAmenityAvailability")
            // Using a proper UUID format for amenity ID
            viewModel.getAmenityAvailability("00000000-0000-0000-0000-000000000001", null, null)
        } else {
            Log.d(TAG, "Feature is disabled")
            binding.progressBar.isVisible = false
            binding.recyclerView.isVisible = false
            Toast.makeText(
                requireContext(),
                "This feature is currently unavailable",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
