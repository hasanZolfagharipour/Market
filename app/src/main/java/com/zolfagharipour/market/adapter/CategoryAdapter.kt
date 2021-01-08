package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.databinding.ItemRowCategoryBinding
import com.zolfagharipour.market.enums.FragmentHostEnum
import com.zolfagharipour.market.viewModel.CategoryViewModel

class CategoryAdapter(val viewModel: CategoryViewModel, val lifecycleOwner: LifecycleOwner, private val categories: ArrayList<CategoryModel>, val navController: NavController): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
        CategoryHolder(DataBindingUtil.inflate(LayoutInflater.from(viewModel.getApplication()), R.layout.item_row_category, parent, false))


    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindingRecyclerViews(categories[position])
    }

    override fun getItemCount(): Int = categories.size


    inner class CategoryHolder(val binding: ItemRowCategoryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.lifecycleOwner = lifecycleOwner
        }
        fun bindingRecyclerViews(categoryModel: CategoryModel){
            binding.category = categoryModel
            binding.imageViewPhotoItem.load(categoryModel.image)
            binding.executePendingBindings()
        }
    }
}