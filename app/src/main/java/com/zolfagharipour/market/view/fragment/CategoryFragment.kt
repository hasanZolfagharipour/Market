package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.CategoryAdapter
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.databinding.FragmentCategoryBinding
import com.zolfagharipour.market.other.Utilities
import com.zolfagharipour.market.viewModel.CategoryViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModel.checkNetwork(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        fetchDataOnObserver()
        setListener()
    }

    private fun fetchDataOnObserver(){
        viewModel.isDataFetched.observe(viewLifecycleOwner, {
            if (it) {
                lifecycleScope.launch(Main + Utilities.exceptionHandler) {
                    setRecyclerViews(
                        binding.recyclerViewDigitalCategory,
                        viewModel.digitalCategories.value!!
                    )
                    setRecyclerViews(
                        binding.recyclerViewFashionAndModelCategory,
                        viewModel.fashionCategories.value!!
                    )
                    setRecyclerViews(
                        binding.recyclerViewArtAndBookCategory,
                        viewModel.artAndBookCategories.value!!
                    )
                    setRecyclerViews(
                        binding.recyclerViewSuperMarketCategory,
                        viewModel.superMarketCategories.value!!
                    )
                    setRecyclerViews(
                        binding.recyclerViewOtherCategory,
                        viewModel.otherCategories.value!!
                    )
                }
            }
        })
    }

    private fun setRecyclerViews(recyclerView: RecyclerView, list: ArrayList<CategoryModel>) {
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryAdapter(
                viewModel,
                this@CategoryFragment,
               list,
                findNavController()
            )
        }
    }

    private fun setListener(){
        binding.searchViewBox.cardViewContainerSearchViewHome.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }


}