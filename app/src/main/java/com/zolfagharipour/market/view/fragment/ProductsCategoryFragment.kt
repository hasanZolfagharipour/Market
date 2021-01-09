package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.ProductsInCategoryAdapter
import com.zolfagharipour.market.databinding.FragmentProductsCategoryBinding
import com.zolfagharipour.market.other.TAG
import com.zolfagharipour.market.viewModel.ProductsCategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class ProductsCategoryFragment : Fragment() {

    private lateinit var binding: FragmentProductsCategoryBinding
    private lateinit var viewModel: ProductsCategoryViewModel
    private val args by navArgs<ProductsCategoryFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductsCategoryViewModel::class.java)
        viewModel.category = args.category
        viewModel.checkNetwork(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products_category, container, false)
       binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isDataFetched.observe(viewLifecycleOwner, {
            if (it)
                CoroutineScope(Main).launch { setRecyclerView() }
        })
    }
    private fun setRecyclerView(){
        binding.recyclerViewItems.apply {
            layoutManager = LinearLayoutManager(viewModel.getApplication())
            adapter = ProductsInCategoryAdapter(viewModel, this@ProductsCategoryFragment, viewModel.category.products, findNavController())
            addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        }
    }
}