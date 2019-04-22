package com.edvinas.balkaitis.party.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.edvinas.balkaitis.party.utils.mvvm.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseDaggerFragment : DaggerFragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(getLayoutId(), container, false)

    protected fun <T : ViewModel> getViewModel(clazz: KClass<T>): T {
        return ViewModelProviders.of(this, viewModelFactory)[clazz.java]
    }

    protected fun <T> LiveData<T>.onResult(action: (T) -> Unit) {
        observe(this@BaseDaggerFragment, Observer { data -> data?.let(action) })
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
}
