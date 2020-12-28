package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.ItemRowLabelBinding
import com.zolfagharipour.market.databinding.ItemRowLastProductsBinding
import com.zolfagharipour.market.viewModel.HomeViewModel

class LastProductsAdapter(var homeViewModel: HomeViewModel, var lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeLabel: Int = 0
    private val viewTypeItems: Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeLabel) {
            val binding: ItemRowLabelBinding = DataBindingUtil.inflate(
                LayoutInflater.from(homeViewModel.getApplication()),
                R.layout.item_row_label,
                parent,
                false
            )
            return LabelProductHolder(binding)
        } else {
            val binding: ItemRowLastProductsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(homeViewModel.getApplication()),
                R.layout.item_row_last_products, parent, false
            )
            return LastProductsHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is LastProductsHolder)
            holder.bindProducts(position.dec())
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == viewTypeLabel)
            viewTypeLabel
        else
            viewTypeItems
    }

    override fun getItemCount(): Int = homeViewModel.productList.value!!.size + 1

    inner class LastProductsHolder(var binding: ItemRowLastProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = homeViewModel
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindProducts(position: Int) {
            binding.position = position
            binding.imageViewPhotoProduct.load(homeViewModel.productList.value!![position].images[0])
            binding.executePendingBindings()
        }

    }

    inner class LabelProductHolder(binding: ItemRowLabelBinding) :
        RecyclerView.ViewHolder(binding.root)
}