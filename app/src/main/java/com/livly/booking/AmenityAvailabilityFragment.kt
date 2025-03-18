package com.livly.booking

import android.os.Bundle
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

        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModel.amenityAvailability.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                    }
                    is Result.Success -> {
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
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

            viewModel.getAmenityAvailability("sample-amenity-id", null, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
