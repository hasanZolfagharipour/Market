package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.ItemRowHomeSliderBinding
import com.zolfagharipour.market.viewModel.HomeViewModel

class HomeSlideAdapter(val viewModel: HomeViewModel, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<HomeSlideAdapter.HomeSlideViewHolder>() {


    inner class HomeSlideViewHolder(val binding: ItemRowHomeSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindingImages(position: Int) {
            binding.imageViewPhotoSlider.load(viewModel.slider.value!!.imageList[position]) {
                transformations(RoundedCornersTransformation(24f))
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSlideViewHolder {
        val binding: ItemRowHomeSliderBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_home_slider,
                parent,
                false
            )
        return HomeSlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeSlideViewHolder, position: Int) {
        holder.bindingImages(position)
    }

    override fun getItemCount(): Int = viewModel.slider.value!!.imageList.size
}