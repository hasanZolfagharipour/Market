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
import com.zolfagharipour.market.adapter.CategoryAdapter
import com.zolfagharipour.market.adapter.HomeSlideAdapter
import com.zolfagharipour.market.adapter.ProductsAdapter
import com.zolfagharipour.market.databinding.FragmentHomeBinding
import com.zolfagharipour.market.viewModel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var autoSliderJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


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
        setSlider()
        setCategorySuggestionRecyclerView()
        setRecyclerView()
        setRecyclerView2()
        setRecyclerView3()
        setListener()
    }

    private fun setCategorySuggestionRecyclerView() {
        binding.recyclerViewCategorySuggestion.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                CategoryAdapter(viewModel, this@HomeFragment, viewModel.categorySuggestion.value!!)
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewLastProducts.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = ProductsAdapter(viewModel, this@HomeFragment, viewModel.lastProducts.value!!)
        }
    }

    private fun setRecyclerView2() {
        binding.recyclerViewPopularProducts.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                ProductsAdapter(viewModel, this@HomeFragment, viewModel.popularProducts.value!!)
        }
    }

    private fun setRecyclerView3() {
        binding.recyclerViewMostRatingProducts.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                ProductsAdapter(viewModel, this@HomeFragment, viewModel.mostRatingProduct.value!!)
        }
    }

    private fun setSlider() {
        CoroutineScope(Default).launch {
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(30))


            binding.viewPagerSlider.apply {
                adapter = HomeSlideAdapter(viewModel, viewLifecycleOwner)
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
                    binding.viewPagerSlider.currentItem = viewModel.getSliderNextPosition(position)
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

    override fun onResume() {
        super.onResume()
        binding.viewPagerSlider.currentItem = 0
    }

}

