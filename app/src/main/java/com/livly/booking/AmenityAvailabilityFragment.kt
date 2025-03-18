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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AmenityAvailabilityFragment : Fragment(R.layout.fragment_amenity_availability) {

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

        Log.d("AmenityFragment", "Feature enabled: ${featureFlagManager.isFeatureEnabled("amenity_availability")}")

        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModel.amenityAvailability.observe(viewLifecycleOwner) { result ->
                Log.d("AmenityFragment", "Received result: $result")
                when (result) {
                    is Result.Loading -> {
                        Log.d("AmenityFragment", "Loading state")
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                    }
                    is Result.Success -> {
                        Log.d("AmenityFragment", "Success with ${result.data.size} items")
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        Log.d("AmenityFragment", "Error: ${result.exception}")
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Error: ${result.exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            Log.d("AmenityFragment", "Calling getAmenityAvailability")
            viewModel.getAmenityAvailability("sample-amenity-id", null, null)
        } else {
            Log.d("AmenityFragment", "Feature is disabled")
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
