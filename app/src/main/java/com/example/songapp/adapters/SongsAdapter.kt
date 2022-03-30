package com.example.songapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.songapp.databinding.SongLayoutBinding
import com.example.songapp.fragments.MusicListFragmentDirections
import com.example.songapp.models.Song

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

    private var binding:SongLayoutBinding?=null

    inner class SongsViewHolder(val binding:SongLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }


    private val differCallback= object :DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
           return oldItem.songUri==newItem.songUri
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem ==newItem
        }
    }
    val differ= AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        binding= SongLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SongsViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
       val currentSong= differ.currentList[position]

        holder.binding.apply {
            tvDuration.text = currentSong.songDuration
            songTitle.text=currentSong.songTitle
            songArtist.text=currentSong.songArtist
            tvOrder.text="${position+1}"
        }
        holder.itemView.setOnClickListener {
            val direction = MusicListFragmentDirections.actionMusicListFragmentToMuicPlayingFragment(currentSong)
            it.findNavController().navigate(direction)
        }

    }

    override fun getItemCount() = differ.currentList.size

}