package cs371.finalproject.crashsafe.api.crashsafeapi

class CrashSafeRepository(private val api: CrashSafeApi) {
    suspend fun fetchModelsSearch(searchStr: String): List<VehicleModel> {
        return api.searchModels(searchStr)
    }

    suspend fun fetchModel(modelName: String): VehicleModel {
        return api.getModel(modelName)
    }
}