package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.FragmentSplashBinding
import com.zolfagharipour.market.viewModel.SplashViewModel

private const val TAG = "tag"

class SplashFragment : Fragment() {

    private lateinit var splashBinding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        viewModel.checkNetwork(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        splashBinding.lifecycleOwner = this
        return splashBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashBinding.splashViewModel = viewModel
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        splashBinding.txtSplashLogo.setOnClickListener {
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            findNavController().navigate(action)
        }

    }


}