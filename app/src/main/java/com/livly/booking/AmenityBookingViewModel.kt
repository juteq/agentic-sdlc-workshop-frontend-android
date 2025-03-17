@HiltViewModel
class AmenityBookingViewModel @Inject constructor(
    private val repository: AmenityBookingRepository,
    private val featureFlagManager: FeatureFlagManager
) : ViewModel() {

    private val _amenityAvailability = MutableLiveData<List<TimeSlot>>()
    val amenityAvailability: LiveData<List<TimeSlot>> get() = _amenityAvailability

    private val _bookingResult = MutableLiveData<String>()
    val bookingResult: LiveData<String> get() = _bookingResult

    fun getAmenityAvailability(id: String, startDate: String?, endDate: String?) {
        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModelScope.launch {
                try {
                    val availability = repository.getAmenityAvailability(id, startDate, endDate)
                    _amenityAvailability.postValue(availability)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun createBooking(createBookingCommand: CreateBookingCommand) {
        if (featureFlagManager.isFeatureEnabled("create_booking")) {
            viewModelScope.launch {
                try {
                    val result = repository.createBooking(createBookingCommand)
                    _bookingResult.postValue(result)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}
