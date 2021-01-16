package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.databinding.ItemRowCategoryBinding
import com.zolfagharipour.market.view.fragment.CategoryFragmentDirections
import com.zolfagharipour.market.viewModel.CategoryViewModel

class CategoryAdapter(
    val viewModel: CategoryViewModel,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController
) : ListAdapter<CategoryModel, CategoryAdapter.CategoryHolder>(DiffCallbackCategory()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
        CategoryHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_category,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindingRecyclerViews(getItem(position))
    }


    inner class CategoryHolder(val binding: ItemRowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindingRecyclerViews(categoryModel: CategoryModel) {
            binding.category = categoryModel
            binding.imageViewPhoto.load(categoryModel.image)
            binding.root.setOnClickListener {
                navController.navigate(
                    CategoryFragmentDirections.actionCategoryFragmentToProductsCategoryFragment(
                        categoryModel
                    )
                )
            }
            binding.executePendingBindings()
        }
    }

    class DiffCallbackCategory(): DiffUtil.ItemCallback<CategoryModel>(){
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean = oldItem == newItem

    }
}