package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.FragmentSplashBinding
import com.zolfagharipour.market.viewModel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private lateinit var splashBinding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        CoroutineScope(Main).launch { viewModel.checkNetwork(this@SplashFragment) }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return splashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashBinding.splashViewModel = viewModel
        splashBinding.lifecycleOwner = this
        setAnimation()


        viewModel.isDataFetched.observe(viewLifecycleOwner, { isDataFetched ->
            navigateToHomeFragment(isDataFetched)
        })


    }

    private fun navigateToHomeFragment(isConnect: Boolean?) {
        if (isConnect!!) {
            CoroutineScope(Main).launch {
                val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setAnimation() {
        CoroutineScope(Main).launch {
            delay(500)
            val animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in)
            animation.duration = 1000
            splashBinding.txtSplashLogo.startAnimation(animation)
            splashBinding.txtSplashLogo.text = getString(R.string.app_name)
        }

    }

}