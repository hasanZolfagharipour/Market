package com.zolfagharipour.market.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.ProductCategory
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.databinding.ItemRowSuggestionCategoryBinding
import com.zolfagharipour.market.viewModel.HomeViewModel

class CategoryAdapter(
    val viewModel: HomeViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val categoryList: ArrayList<ProductCategory>
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    inner class CategoryHolder(val binding: ItemRowSuggestionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        @SuppressLint("ResourceType")
        fun bindCategory(category: ProductCategory, position: Int) {
            binding.category = category

            binding.circleImageViewItemRowSuggestionCategoryPhoto.load(category.image)
            binding.circleImageViewItemRowSuggestionCategoryPhoto.setColorFilter(
                Color.WHITE,
                PorterDuff.Mode.SRC_ATOP
            )
            binding.circleImageViewItemRowSuggestionCategoryPhoto.circleBackgroundColor =
                Color.parseColor(ProductRepository.colorCategorySuggestions[position])
            binding.cardViewContainerCircleImageView.setCardBackgroundColor(Color.parseColor(ProductRepository.colorCategorySuggestions[position]))
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_suggestion_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {

        holder.bindCategory(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size
}