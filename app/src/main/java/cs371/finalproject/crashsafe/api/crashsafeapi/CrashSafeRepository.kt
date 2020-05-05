package cs371.finalproject.crashsafe.api.crashsafeapi

import java.lang.Exception

class CrashSafeRepository(private val api: CrashSafeApi) {
    suspend fun fetchModelsSearch(searchStr: String): List<VehicleModel> {
        return try {
            api.searchModels(searchStr)
        } catch (e: Exception) {
            listOf<VehicleModel>()
        }
    }

    suspend fun fetchModel(modelName: String): VehicleModel {
        return try {
            api.getModel(modelName)
        } catch (e: Exception) {
            VehicleModel()
        }
    }
}