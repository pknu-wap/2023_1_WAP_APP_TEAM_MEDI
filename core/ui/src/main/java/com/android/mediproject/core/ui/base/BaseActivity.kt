package com.android.mediproject.core.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel>(
    val bindingFactory: (LayoutInflater) -> T,
) : AppCompatActivity() {

    protected lateinit var binding: T
        private set

    protected abstract val activityViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        setSplash()
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)

        setContentView(binding.root)
        binding.lifecycleOwner = this

        afterBinding()
    }

    abstract fun afterBinding()


    open fun setSplash() {}


    fun log(str: String) = Log.e("wap", str) //for test

    fun toast(str: String) = Toast.makeText(this, str, Toast.LENGTH_LONG).show()
}
