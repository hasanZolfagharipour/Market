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
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.ItemRowErrorOnLoadingMoreBinding
import com.zolfagharipour.market.databinding.ItemRowLoadingMoreBinding
import com.zolfagharipour.market.databinding.ItemRowProductInCategoryBinding
import com.zolfagharipour.market.view.fragment.ProductsCategoryFragmentDirections
import com.zolfagharipour.market.viewModel.ProductsCategoryViewModel

class ProductsInCategoryAdapter(
    val viewModel: ProductsCategoryViewModel,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController
) :
    ListAdapter<ProductModel, RecyclerView.ViewHolder>(DiffCallbackProductsInCategory()) {

    private val loadingType = 1
    private val errorType = 2
    private val itemType = 3

    inner class ProductsInCategoryHolder(val binding: ItemRowProductInCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindingItems(productModel: ProductModel) {

            binding.imageViewPhoto.load(productModel.images[0])
            binding.product = productModel
            binding.root.setOnClickListener {
                navController.navigate(
                    ProductsCategoryFragmentDirections.actionProductsCategoryFragmentToDetailFragment(
                        productModel
                    )
                )
            }
            binding.executePendingBindings()
        }

    }

    inner class LoadingMoreViewHolder(val binding: ItemRowLoadingMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }
    }

    inner class ErrorOnLoadingMoreViewHolder(val binding: ItemRowErrorOnLoadingMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            itemType -> return ProductsInCategoryHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_product_in_category,
                    parent,
                    false
                )
            )
            loadingType -> return LoadingMoreViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_loading_more,
                    parent,
                    false
                )
            )
            else -> return ErrorOnLoadingMoreViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_error_on_loading_more,
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (viewModel.isConnected && position == currentList.size - 1 && !viewModel.isAllDataFetched)
            loadingType
        else if (!viewModel.isConnected && position == currentList.size - 1)
            errorType
        else
            itemType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductsInCategoryHolder)
            holder.bindingItems(getItem(position))
    }

    class DiffCallbackProductsInCategory: DiffUtil.ItemCallback<ProductModel>(){
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean = oldItem == newItem

    }


}