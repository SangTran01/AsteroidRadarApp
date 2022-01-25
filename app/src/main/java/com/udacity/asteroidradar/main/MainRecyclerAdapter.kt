package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemFragmentMainBinding

class MainRecyclerAdapter(private val clickListener: AsteroidClickListener) : ListAdapter<Asteroid, MainRecyclerAdapter.ViewHolder>(DiffUtil()) {

    class ViewHolder(private val binding: ListItemFragmentMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

            companion object {
                fun getInstance(viewGroup: ViewGroup) : ViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    val binding = ListItemFragmentMainBinding.inflate(inflater, viewGroup, false)
                    return ViewHolder(binding)
                }
            }

            fun bind(asteroid: Asteroid, clickListener: AsteroidClickListener) {
                binding.asteroid = asteroid
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.getInstance(viewGroup)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item, clickListener)
    }
}

class DiffUtil : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(old: Asteroid, new: Asteroid): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: Asteroid, new: Asteroid): Boolean {
        return old == new
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) {
        clickListener(asteroid)
    }
}