package com.zolfagharipour.market.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryProduct
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.databinding.ItemRowSuggestionCategoryBinding
import com.zolfagharipour.market.viewModel.HomeViewModel

class CategorySuggestionAdapter(
    val viewModel: HomeViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val categoryProductList: ArrayList<CategoryProduct>
) : RecyclerView.Adapter<CategorySuggestionAdapter.CategorySuggestionHolder>() {

    inner class CategorySuggestionHolder(val binding: ItemRowSuggestionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }


        fun bindCategory(categoryProduct: CategoryProduct, position: Int) {
            binding.category = categoryProduct

            binding.circleImageViewItemRowSuggestionCategoryPhoto.load(categoryProduct.image)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorySuggestionHolder {
        return CategorySuggestionHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_suggestion_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(suggestionHolder: CategorySuggestionHolder, position: Int) {
        suggestionHolder.bindCategory(categoryProductList[position], position)
    }

    override fun getItemCount(): Int = categoryProductList.size
}