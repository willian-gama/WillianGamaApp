package com.android.dev.engineer.kotlin.compose.feature.routing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dev.engineer.kotlin.compose.data.domain.local.MainNavGraph
import com.android.dev.engineer.kotlin.compose.data.use_case.initial_route.GetInitialRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.time.measureTimedValue

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val getInitialRouteUseCase: GetInitialRouteUseCase
) : ViewModel() {
    companion object {
        private const val ROUTING_DELAY = 1000
    }

    private val _effect by lazy { MutableSharedFlow<MainNavGraph>() }
    val effect = _effect.asSharedFlow()

    init {
        getInitialRoute()
    }

    private fun getInitialRoute() {
        viewModelScope.launch {
            try {
                val (mainNavGraph, duration) = measureTimedValue {
                    getInitialRouteUseCase.invoke()
                }
                delay(timeMillis = ROUTING_DELAY - duration.inWholeMilliseconds)
                _effect.emit(mainNavGraph)
            } catch (e: IOException) {
                Timber.e(e, "Error when getting initial route")
                _effect.emit(MainNavGraph.SignIn)
            }
        }
    }
}