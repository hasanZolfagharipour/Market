package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.CategorySuggestionAdapter
import com.zolfagharipour.market.adapter.HomeProductsAdapter
import com.zolfagharipour.market.adapter.HomeSlideAdapter
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.FragmentHomeBinding
import com.zolfagharipour.market.network.NetworkParams
import com.zolfagharipour.market.other.Utilities
import com.zolfagharipour.market.viewModel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var autoSliderJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
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

        lifecycleScope.launch(Main + Utilities.exceptionHandler) {
            setSlider()
            setCategorySuggestionRecyclerView()
            setRecyclerViews(
                binding.recyclerViewLastProducts,
                viewModel.lastProducts.value!!,
                R.drawable.last_label,
                CategoryModel(NetworkParams.CategoryID.LAST_PRODUCTS_ID, getString(R.string.all_products))
            )
            setRecyclerViews(
                binding.recyclerViewPopularProducts,
                viewModel.popularProducts.value!!,
                R.drawable.popular_label,
                CategoryModel(NetworkParams.CategoryID.POPULAR_PRODUCTS_ID, getString(R.string.all_products))
            )
            setRecyclerViews(
                binding.recyclerViewMostRatingProducts,
                viewModel.bestProducts.value!!,
                R.drawable.best_label,
                CategoryModel(NetworkParams.CategoryID.BEST_PRODUCTS_ID, getString(R.string.all_products))
            )
        }

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
                CategorySuggestionAdapter(
                    viewModel,
                    this@HomeFragment,
                    viewModel.categoryModelSuggestionList.value!!,
                    findNavController()
                )
        }
    }

    private fun setRecyclerViews(
        recyclerView: RecyclerView,
        list: ArrayList<ProductModel>,
        drawable: Int,
        category: CategoryModel
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = HomeProductsAdapter(
                viewModel,
                this@HomeFragment,
                list,
                findNavController(),
                drawable,
                category
            )
        }
    }

    private fun setSlider() {
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

    private fun setListener() {
        binding.viewPagerSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                autoSliderJob?.cancel()
                autoSliderJob = lifecycleScope.launch(Default + Utilities.exceptionHandler) {
                    delay(3000)
                    binding.viewPagerSlider.currentItem = viewModel.getSliderNextPosition(position)
                }

            }
        })

        binding.searchViewBox.cardViewContainerSearchViewHome.setOnClickListener {
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