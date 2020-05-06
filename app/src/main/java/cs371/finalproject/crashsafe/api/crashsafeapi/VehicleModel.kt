package cs371.finalproject.crashsafe.api.crashsafeapi

import com.google.gson.annotations.SerializedName

data class VehicleModel (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("year")
    val year: Int = 404,
    @SerializedName("make")
    val make: String = "Vehicle Not Found",
    @SerializedName("model")
    val model: String = "",
    @SerializedName("img")
    val img: String = "",
    @SerializedName("engine_type")
    val engineType: String = "N/A",
    @SerializedName("vehicle_type")
    val vehicleType: String = "N/A",
    @SerializedName("horse_power")
    val horsePowers: String = "N/A",
    @SerializedName("IIHS_frontModerateOverlap")
    val iihs_frontModerateOverlap: String = "N/A",
    @SerializedName("IIHS_frontSmallOverlap")
    val iihs_frontSmallOverlap: String = "N/A",
    @SerializedName("IIHS_side")
    val iihs_side: String = "N/A",
    @SerializedName("IIHS_rollover")
    val iihs_rollover: String = "N/A",
    @SerializedName("IIHS_rear")
    val iihs_rear: String = "N/A",
    @SerializedName("NHTSA_overall_rating")
    val nhtsa_overallRating: String = "N/A",
    @SerializedName("NHTSA_overall_side_crash_rating")
    val nhtsa_overallSideCrashRating: String = "N/A",
    @SerializedName("NHTSA_overall_front_crash_rating")
    val nhtsa_overallFrontCrashRating: String = "N/A",
    @SerializedName("NHTSA_rollover_rating")
    val nhtsa_rolloverRating: String = "N/A"
)