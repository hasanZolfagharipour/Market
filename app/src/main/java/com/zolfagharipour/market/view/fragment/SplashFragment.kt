package com.zolfagharipour.market.view.fragment

import android.animation.Animator
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
import com.zolfagharipour.market.viewModel.MarketNetworkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: MarketNetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MarketNetworkViewModel::class.java)
        CoroutineScope(Main).launch { viewModel.checkNetwork(this@SplashFragment) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        logoAnimation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.splashViewModel = viewModel
        binding.lifecycleOwner = this
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
            binding.txtSplashLogo.startAnimation(animation)
            binding.txtSplashLogo.text = getString(R.string.app_name)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                binding.txtSplashLogo.setTypeface(resources.getFont(R.font.orbitron_bold))
            }
        }

    }

    private fun logoAnimation() {

        binding.lottieAnimationLogo.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {
                binding.lottieAnimationLogo.setMinAndMaxProgress(0.3f, 1f)
            }


        })
    }

}