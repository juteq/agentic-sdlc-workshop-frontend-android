@Singleton
class FeatureFlagManager @Inject constructor() {

    private val featureFlags = mutableMapOf<String, Boolean>()

    fun isFeatureEnabled(feature: String): Boolean {
        return featureFlags[feature] ?: false
    }

    fun setFeatureEnabled(feature: String, isEnabled: Boolean) {
        featureFlags[feature] = isEnabled
    }
}
