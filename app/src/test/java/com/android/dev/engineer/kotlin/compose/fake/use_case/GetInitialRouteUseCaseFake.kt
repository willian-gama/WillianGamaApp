package com.android.dev.engineer.kotlin.compose.fake.use_case

import com.android.dev.engineer.kotlin.compose.data.domain.local.MainNavGraph
import com.android.dev.engineer.kotlin.compose.data.use_case.initial_route.GetInitialRouteUseCase

class GetInitialRouteUseCaseFake : GetInitialRouteUseCase {
    lateinit var mainNavGraph: MainNavGraph
    var error: Exception? = null

    override suspend fun invoke(): MainNavGraph {
        error?.let { throw it }
        return mainNavGraph
    }
}