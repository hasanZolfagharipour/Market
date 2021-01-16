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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.ProductsInCategoryAdapter
import com.zolfagharipour.market.databinding.FragmentProductsCategoryBinding
import com.zolfagharipour.market.other.Utilities
import com.zolfagharipour.market.viewModel.ProductsCategoryViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class ProductsCategoryFragment : Fragment() {

    private lateinit var binding: FragmentProductsCategoryBinding
    private lateinit var viewModel: ProductsCategoryViewModel
    private lateinit var adapter: ProductsInCategoryAdapter
    private val args by navArgs<ProductsCategoryFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductsCategoryViewModel::class.java)
        viewModel.category = args.category

        viewModel.checkNetwork(this)
        adapter = ProductsInCategoryAdapter(
            viewModel,
            this@ProductsCategoryFragment,
            findNavController()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_products_category, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setRecyclerView()
        setListener()


    }

    private fun setObservers(){
        viewModel.isDataFetched.observe(viewLifecycleOwner, {
            if (it)
                lifecycleScope.launch(Main + Utilities.exceptionHandler) {
                    adapter.submitList(viewModel.category.products)
                    adapter.notifyDataSetChanged()
                }
        })

        viewModel.isLoadingMore.observe(viewLifecycleOwner, {
            if (!it)
                adapter.notifyDataSetChanged()

        })
    }

    private fun setRecyclerView() {

        binding.recyclerViewItems.apply {
            layoutManager = LinearLayoutManager(viewModel.getApplication())
            adapter = this@ProductsCategoryFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun setListener() {
        binding.recyclerViewItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL) && !viewModel.isLoadingMore.value!! && !viewModel.isAllDataFetched)
                    viewModel.isLoadingMore.postValue(true)

            }
        })


    }
}