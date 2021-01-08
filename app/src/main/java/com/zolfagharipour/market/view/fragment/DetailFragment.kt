package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.CategoryTagInProductAdapter
import com.zolfagharipour.market.adapter.PhotoSliderAdapter
import com.zolfagharipour.market.adapter.ProductsAdapter
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.FragmentDetailBinding
import com.zolfagharipour.market.enums.FragmentHostEnum
import com.zolfagharipour.market.viewModel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.checkNetwork(this, args.product)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        CoroutineScope(Main).launch {
            viewModel.productModel.observe(viewLifecycleOwner, {
                setSlider(it)
                setCategoryTagRecyclerView(it)
                setSimilarProductRecyclerView(it)
            })
        }
    }

    private fun setSlider(productModel: ProductModel) {
        binding.viewPagerPhotoSlider.apply {
            adapter = PhotoSliderAdapter(
                viewModel,
                viewLifecycleOwner,
                productModel.images
            )
        }
        binding.dotCircleIndicatorPhoto.setViewPager2(binding.viewPagerPhotoSlider)
    }

    private fun setCategoryTagRecyclerView(productModel: ProductModel) {
        binding.recyclerViewCategoriesInProduct.apply {
            layoutManager = LinearLayoutManager(
                viewModel.getApplication(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter =
                CategoryTagInProductAdapter(viewModel, this@DetailFragment, productModel.categories)
        }
    }

    private fun setSimilarProductRecyclerView(productModel: ProductModel) {
        binding.recyclerViewSimilarProduct.apply {
            layoutManager = LinearLayoutManager(
                viewModel.getApplication(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = ProductsAdapter(
                viewModel,
                this@DetailFragment,
                productModel.relatedProductModel,
                findNavController(),
                FragmentHostEnum.DETAIL
            )
        }
    }
}