package com.android.dev.engineer.kotlin.compose.data.use_case

import com.android.dev.engineer.kotlin.compose.data.fake.datastore.AppDataStoreFake
import com.android.dev.engineer.kotlin.compose.data.use_case.mark_intro.MarkIntroCompleteUseCase
import com.android.dev.engineer.kotlin.compose.data.use_case.mark_intro.MarkIntroCompleteUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MarkIntroCompleteUseCaseTest {
    private lateinit var appDataStore: AppDataStoreFake
    private lateinit var markIntroCompleteUseCase: MarkIntroCompleteUseCase

    @Before
    fun setUp() {
        appDataStore = AppDataStoreFake()
        markIntroCompleteUseCase = MarkIntroCompleteUseCaseImpl(appDataStore = appDataStore)
    }

    @Test
    fun `test mark intro is updated`() = runTest {
        assertEquals(true, appDataStore.isIntroPending)
        markIntroCompleteUseCase.invoke()
        assertEquals(false, appDataStore.isIntroPending)
    }
}