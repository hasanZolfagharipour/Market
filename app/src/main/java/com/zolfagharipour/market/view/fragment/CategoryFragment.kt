package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.CategoryAdapter
import com.zolfagharipour.market.databinding.FragmentCategoryBinding
import com.zolfagharipour.market.viewModel.CategoryViewModel

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryRecyclerView()
    }

    private fun setCategoryRecyclerView(){
        binding.recyclerViewCategoryFragment.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = CategoryAdapter(viewModel, this@CategoryFragment, viewModel.categoryProductList.value!!)
        }
    }


}