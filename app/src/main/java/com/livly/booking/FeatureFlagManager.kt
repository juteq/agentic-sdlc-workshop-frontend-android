package com.livly.booking

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeatureFlagManager @Inject constructor() {
    private val enabledFeatures = setOf(
        "amenity_availability",
        "create_booking"
    )

    fun isFeatureEnabled(featureName: String): Boolean {
        return enabledFeatures.contains(featureName)
    }
}
