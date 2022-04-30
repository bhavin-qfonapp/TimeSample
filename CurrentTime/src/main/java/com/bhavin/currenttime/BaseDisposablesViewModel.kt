package com.bhavin.currenttime

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */
abstract class BaseDisposablesViewModel : ViewModel() {
    protected val myCompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        myCompositeDisposable.dispose()
    }
}