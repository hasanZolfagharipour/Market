package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.HomeSlideAdapter
import com.zolfagharipour.market.adapter.LastProductsAdapter
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.databinding.FragmentHomeBinding
import com.zolfagharipour.market.viewModel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var autoSliderJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setSlider()
        setListener()
    }

    private fun setRecyclerView() {
        val lastProductsAdapter = LastProductsAdapter(homeViewModel, this)
        binding.recyclerViewLastProducts.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = lastProductsAdapter
        }
    }

    private fun getData() {

        CoroutineScope(IO).launch {
            withContext(Main) {
                homeViewModel.productList.value = ProductRepository.lastProductList
                setRecyclerView()
            }
        }
    }

    private fun setSlider() {
        CoroutineScope(Default).launch {
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(30))


            binding.viewPagerSlider.apply {
                adapter = HomeSlideAdapter(homeViewModel, viewLifecycleOwner)
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setPageTransformer(compositePageTransformer)
            }
            setDots()
        }
    }

    private fun setListener() {
        binding.viewPagerSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                autoSliderJob?.cancel()
                autoSliderJob = CoroutineScope(Default).launch {
                    delay(3000)
                    binding.viewPagerSlider.currentItem = homeViewModel.getSliderNextPosition(position)
                }

            }
        })

        binding.cardViewContainerSearchViewHome.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    private fun setDots() {
        binding.dotCircleIndicator.setViewPager2(binding.viewPagerSlider)
    }

    override fun onStop() {
        super.onStop()
        autoSliderJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        autoSliderJob?.cancel()
    }

    override fun onStart() {
        super.onStart()
        binding.viewPagerSlider.currentItem = 0
    }

}

