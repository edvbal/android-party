package com.edvinas.balkaitis.party.utils.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class RxViewModel : ViewModel() {
    val subscription = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        subscription.clear()
    }
}
