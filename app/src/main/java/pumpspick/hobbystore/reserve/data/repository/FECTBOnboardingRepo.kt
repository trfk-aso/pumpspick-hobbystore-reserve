package pumpspick.hobbystore.reserve.data.repository

import pumpspick.hobbystore.reserve.data.datastore.FECTBOnboardingPrefs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FECTBOnboardingRepo(
    private val fectbOnboardingStoreManager: FECTBOnboardingPrefs,
    private val coroutineDispatcher: CoroutineDispatcher,
) {

    fun observeOnboardingState(): Flow<Boolean?> {
        return fectbOnboardingStoreManager.onboardedStateFlow
    }

    suspend fun setOnboardingState(state: Boolean) {
        withContext(coroutineDispatcher) {
            fectbOnboardingStoreManager.setOnboardedState(state)
        }
    }
}