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
import com.zolfagharipour.market.adapter.CategorySuggestionAdapter
import com.zolfagharipour.market.adapter.HomeSlideAdapter
import com.zolfagharipour.market.adapter.ProductsAdapter
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.databinding.FragmentHomeBinding
import com.zolfagharipour.market.viewModel.HomeViewModel
import com.zolfagharipour.market.viewModel.MarketNetworkViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var autoSliderJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fetchCategoryItems()
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
        binding.viewModel = viewModel

        setSlider()
        setCategorySuggestionRecyclerView()
        setRecyclerViews(binding.recyclerViewLastProducts, viewModel.lastProducts.value!!)
        setRecyclerViews(binding.recyclerViewPopularProducts, viewModel.popularProducts.value!!)
        setRecyclerViews(binding.recyclerViewMostRatingProducts, viewModel.mostRatingProduct.value!!)
        setListener()
    }

    private fun fetchCategoryItems(){
        CoroutineScope(IO).launch {
            delay(500)
            val marketViewModel = ViewModelProvider(requireActivity()).get(MarketNetworkViewModel::class.java)
            marketViewModel.fetchProductsOfEachCategory()
        }
    }

    private fun setCategorySuggestionRecyclerView() {
        binding.recyclerViewCategorySuggestion.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                CategorySuggestionAdapter(viewModel, this@HomeFragment, viewModel.categoryProductSuggestionList.value!!)
        }
    }

    private fun setRecyclerViews(recyclerView: RecyclerView, list: ArrayList<Product>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = ProductsAdapter(viewModel, this@HomeFragment, list)
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
            binding.dotCircleIndicator.setViewPager2(binding.viewPagerSlider)
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