@AndroidEntryPoint
class AmenityAvailabilityFragment : Fragment(R.layout.fragment_amenity_availability) {

    private val viewModel: AmenityBookingViewModel by viewModels()
    private val featureFlagManager: FeatureFlagManager by lazy { FeatureFlagManager() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = AmenityAvailabilityAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModel.amenityAvailability.observe(viewLifecycleOwner, { availability ->
                adapter.submitList(availability)
            })

            // Fetch availability for a sample amenity ID
            viewModel.getAmenityAvailability("sample-amenity-id", null, null)
        }
    }
}
