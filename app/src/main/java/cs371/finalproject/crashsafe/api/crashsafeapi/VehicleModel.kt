package cs371.finalproject.crashsafe.api.crashsafeapi

import com.google.gson.annotations.SerializedName

data class VehicleModel (
    @SerializedName("year")
    val year: Int,
    @SerializedName("make")
    val make: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("engine_type")
    val engineType: String,
    @SerializedName("vehicle_type")
    val vehicleType: String,
    @SerializedName("horse_power")
    val horsePowers: String,
    @SerializedName("IIHS_frontModerateOverlap")
    val IIHS_frontModerateOverlap: String,
    @SerializedName("IIHS_frontSmallOverlap")
    val IIHS_frontSmallOverlap: String,
    @SerializedName("IIHS_side")
    val IIHS_side: String,
    @SerializedName("IIHS_rollover")
    val IIHS_rollover: String,
    @SerializedName("IIHS_rear")
    val IIHS_rear: String,
    @SerializedName("NHTSA_overall_rating")
    val NHTSA_overallRating: String,
    @SerializedName("NHTSA_overall_side_crash_rating")
    val NHTSA_overallSideCrashRating: String,
    @SerializedName("NHTSA_overall_front_crash_rating")
    val NHTSA_overallFrontCrashRating: String,
    @SerializedName("NHTSA_rollover_rating")
    val NHTSA_rolloverRating: String
)