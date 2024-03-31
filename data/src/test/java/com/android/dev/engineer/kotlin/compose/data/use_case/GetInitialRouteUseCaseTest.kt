package com.android.dev.engineer.kotlin.compose.data.use_case

import com.android.dev.engineer.kotlin.compose.data.domain.local.MainNavGraph
import com.android.dev.engineer.kotlin.compose.data.fake.datastore.AppDataStoreFake
import com.android.dev.engineer.kotlin.compose.data.use_case.initial_route.GetInitialRouteUseCase
import com.android.dev.engineer.kotlin.compose.data.use_case.initial_route.GetInitialRouteUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetInitialRouteUseCaseTest {
    private lateinit var appDataStore: AppDataStoreFake
    private lateinit var getInitialRouteUseCase: GetInitialRouteUseCase

    @Before
    fun setUp() {
        appDataStore = AppDataStoreFake()
        getInitialRouteUseCase = GetInitialRouteUseCaseImpl(appDataStore = appDataStore)
    }

    @Test
    fun `test initial route is intro`() = runTest {
        appDataStore.isIntroPending = true
        val mainNavGraph = getInitialRouteUseCase.invoke()
        assertEquals(MainNavGraph.Intro, mainNavGraph)
    }

    @Test
    fun `test initial route is sign in`() = runTest {
        appDataStore.isIntroPending = false
        val mainNavGraph = getInitialRouteUseCase.invoke()
        assertEquals(MainNavGraph.SignIn, mainNavGraph)
    }
}