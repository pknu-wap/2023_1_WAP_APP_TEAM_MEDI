package com.android.mediproject.core.data.remote.di

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepository
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepositoryImpl
import com.android.mediproject.core.data.remote.ai.VertexAiRepository
import com.android.mediproject.core.data.remote.ai.VertexAiRepositoryImpl
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.data.remote.comments.CommentsRepositoryImpl
import com.android.mediproject.core.data.remote.dur.DurRepository
import com.android.mediproject.core.data.remote.dur.DurRepositoryImpl
import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepository
import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepositoryImpl
import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepository
import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepositoryImpl
import com.android.mediproject.core.data.remote.interestedmedicine.InterestedMedicineRepository
import com.android.mediproject.core.data.remote.interestedmedicine.InterestedMedicineRepositoryImpl
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepositoryImpl
import com.android.mediproject.core.data.remote.medicineid.MedicineIdRepository
import com.android.mediproject.core.data.remote.medicineid.MedicineIdRepositoryImpl
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepositoryImpl
import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.data.remote.sign.SignRepositoryImpl
import com.android.mediproject.core.data.remote.user.UserInfoRepository
import com.android.mediproject.core.data.remote.user.UserInfoRepositoryImpl
import com.android.mediproject.core.data.remote.user.UserRepository
import com.android.mediproject.core.data.remote.user.UserRepositoryImpl
import com.android.mediproject.core.data.search.SearchHistoryRepository
import com.android.mediproject.core.data.search.SearchHistoryRepositoryImpl
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.datastore.TokenDataSourceImpl
import com.android.mediproject.core.network.datasource.ai.VertextAiDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.dur.DurDataSource
import com.android.mediproject.core.network.datasource.elderlycaution.ElderlyCautionDataSource
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import com.android.mediproject.core.network.datasource.interestedmedicine.InterestedMedicineDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSource
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionListDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionListDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.datasource.user.UserDataSource
import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository = UserRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideInterestedMedicineRepository(interestedMedicineDataSource: InterestedMedicineDataSource): InterestedMedicineRepository =
        InterestedMedicineRepositoryImpl(interestedMedicineDataSource)

    @Provides
    @Singleton
    fun provideMedicineApprovalRepository(
        medicineApprovalDataSource: MedicineApprovalDataSource,
        searchHistoryRepository: SearchHistoryRepository,
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): MedicineApprovalRepository = MedicineApprovalRepositoryImpl(medicineApprovalDataSource, searchHistoryRepository, ioDispatcher)

    @Provides
    @Singleton
    fun provideRecallSuspensionRepository(
        recallSuspensionDataSource: RecallSuspensionDataSource, recallSuspensionListDataSource: RecallSuspensionListDataSourceImpl
    ): RecallSuspensionRepository = RecallSuspensionRepositoryImpl(recallSuspensionDataSource, recallSuspensionListDataSource)

    @Provides
    @Singleton
    fun providesAdminActionRepository(
        adminActionDataSource: AdminActionListDataSourceImpl
    ): AdminActionRepository = AdminActionRepositoryImpl(adminActionDataSource)

    @Provides
    @Singleton
    fun providesGranuleIdentificationRepository(
        granuleIdentificationDataSource: GranuleIdentificationDataSource
    ): GranuleIdentificationRepository = GranuleIdentificationRepositoryImpl(granuleIdentificationDataSource)

    @Provides
    @Singleton
    fun providesElderlyCautionRepository(elderlyCautionDataSource: ElderlyCautionDataSource): ElderlyCautionRepository =
        ElderlyCautionRepositoryImpl(elderlyCautionDataSource)

    @Provides
    @Singleton
    fun providesDurRepository(durDataSource: DurDataSource): DurRepository = DurRepositoryImpl(durDataSource)


    @Provides
    @Singleton
    fun providesCommentsRepository(commentsDataSource: CommentsDataSource, signRepository: SignRepository): CommentsRepository =
        CommentsRepositoryImpl(commentsDataSource, signRepository)


    @Provides
    @Singleton
    fun providesSignRepository(
        signDataSource: SignDataSource,
        connectionTokenDataSourceImpl: TokenDataSourceImpl,
        appDataStore: AppDataStore,
        userInfoRepository: UserInfoRepository
    ): SignRepository = SignRepositoryImpl(signDataSource, connectionTokenDataSourceImpl, appDataStore, userInfoRepository)

    @Provides
    @Singleton
    fun providesSearchHistoryRepository(
        searchHistoryDao: SearchHistoryDao
    ): SearchHistoryRepository = SearchHistoryRepositoryImpl(searchHistoryDao)

    @Provides
    @Singleton
    fun providesMedicineIdRepository(
        medicineIdDataSource: MedicineIdDataSource
    ): MedicineIdRepository = MedicineIdRepositoryImpl(medicineIdDataSource)

    @Provides
    @Singleton
    fun providesVertexAiRepository(
        vertextAiDataSource: VertextAiDataSource
    ): VertexAiRepository = VertexAiRepositoryImpl(vertextAiDataSource)

    @Provides
    @Singleton
    fun providesUserInfoRepository(
        userInfoDataSource: UserInfoDataSource, appDataStore: AppDataStore
    ): UserInfoRepository = UserInfoRepositoryImpl(userInfoDataSource, appDataStore)

}