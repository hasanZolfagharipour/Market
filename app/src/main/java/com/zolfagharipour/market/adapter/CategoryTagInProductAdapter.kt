package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.databinding.ItemRowCategoriesInProductBinding

class CategoryTagInProductAdapter(val viewModel: AndroidViewModel, val lifecycleOwner: LifecycleOwner,private val list: ArrayList<CategoryModel>):
    RecyclerView.Adapter<CategoryTagInProductAdapter.CategoryTagInProductViewHolder>() {

    inner class CategoryTagInProductViewHolder(val binding: ItemRowCategoriesInProductBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindCategories(categoryModel: CategoryModel){
            binding.category = categoryModel
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryTagInProductViewHolder {
        return CategoryTagInProductViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewModel.getApplication()), R.layout.item_row_categories_in_product, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryTagInProductViewHolder, position: Int) {
        holder.bindCategories(list[position])
    }

    override fun getItemCount(): Int = list.size
}