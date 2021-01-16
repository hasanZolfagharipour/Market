package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.CategoryModel
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.ItemRowEndLayoutBinding
import com.zolfagharipour.market.databinding.ItemRowLabelLayoutBinding
import com.zolfagharipour.market.databinding.ItemRowProductsBinding
import com.zolfagharipour.market.view.fragment.HomeFragmentDirections

class HomeProductsAdapter(
    private val viewModel: AndroidViewModel,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController,
    val drawable: Int,
    val category: CategoryModel
) :
    ListAdapter<ProductModel, RecyclerView.ViewHolder>(DiffUtilCallbackHomeProducts()) {

    private val itemLabelType = 0
    private val itemRowType = 1
    private val itemEndType = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemLabelType -> LabelHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_label_layout,
                    parent,
                    false
                )
            )
            itemEndType -> EndViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_end_layout,
                    parent,
                    false
                )
            )
            else -> ProductsHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewModel.getApplication()),
                    R.layout.item_row_products,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductsHolder -> holder.bindProducts(getItem(position))
            is LabelHolder -> holder.bindLabelImage()
            is EndViewHolder -> holder.navigating()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> itemLabelType
            itemCount - 1 -> itemEndType
            else -> itemRowType
        }
    }

    override fun getItemCount(): Int = 12

    inner class ProductsHolder(var binding: ItemRowProductsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindProducts(productModel: ProductModel) {
            binding.product = productModel
            binding.imageViewPhotoProduct.load(productModel.images[0])
            binding.cardViewContainerItemRowProduct.setOnClickListener {
                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        productModel
                    )
                )
            }
            binding.executePendingBindings()
        }
    }

    inner class LabelHolder(val binding: ItemRowLabelLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindLabelImage() {
            binding.imageViewPhoto.load(drawable)
            binding.executePendingBindings()
        }
    }

    inner class EndViewHolder(val binding: ItemRowEndLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun navigating() {
            binding.root.setOnClickListener {
                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToProductsCategoryFragment(
                        category
                    )
                )
            }
        }

    }

    class DiffUtilCallbackHomeProducts: DiffUtil.ItemCallback<ProductModel>(){
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean = oldItem == newItem
    }
}