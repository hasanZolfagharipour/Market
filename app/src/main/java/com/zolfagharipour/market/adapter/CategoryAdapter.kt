package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryProduct
import com.zolfagharipour.market.databinding.ItemRowCategoryBinding
import com.zolfagharipour.market.viewModel.CategoryViewModel

class CategoryAdapter(val viewModel: CategoryViewModel, val lifecycleOwner: LifecycleOwner, val categories: ArrayList<CategoryProduct>): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
        CategoryHolder(DataBindingUtil.inflate(LayoutInflater.from(viewModel.getApplication()), R.layout.item_row_category, parent, false))


    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindingRecyclerViews(categories[position], position)
    }

    override fun getItemCount(): Int = categories.size


    inner class CategoryHolder(val binding: ItemRowCategoryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.lifecycleOwner = lifecycleOwner
            binding.viewModel = viewModel
        }
        fun bindingRecyclerViews(categoryProduct: CategoryProduct, position: Int){
            binding.position = position
            binding.recyclerViewCategory.apply {
                layoutManager = LinearLayoutManager(viewModel.getApplication(), LinearLayoutManager.HORIZONTAL, false)
                adapter = ProductsAdapter(viewModel, lifecycleOwner, categoryProduct.products)
            }
            binding.executePendingBindings()
        }
    }
}