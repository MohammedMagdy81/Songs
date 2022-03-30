package com.example.songapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.songapp.R
import com.example.songapp.adapters.SongsAdapter
import com.example.songapp.databinding.FragmentMusicListBinding
import com.example.songapp.helper.Utils
import com.example.songapp.helper.Utils.toast
import com.example.songapp.models.Song

class MusicListFragment : Fragment(R.layout.fragment_music_list) {

    private var _binding:FragmentMusicListBinding?=null
    private val binding get() = _binding!!
    private var songList : MutableList<Song> = ArrayList()
    private lateinit var songAdapter: SongsAdapter
    val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentMusicListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //checkPermission
    fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(),permission) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(permission),Utils.REQUEST_CODE_FOR_PERMISSION)
            return
        }
        loadSong()
    }
    @SuppressLint("Range")
    fun loadSong(){
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC+"!=0"
        val sortOrder = " ${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = activity?.applicationContext?.contentResolver!!.query(allSongsURI,null
            ,selection,null,sortOrder,null)

        if (cursor != null){
            while (cursor.moveToNext()){

                val songUri= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                //val songAuthor= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.AUTHOR))
                val songDuration= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val songTitle= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val songDurationLong= songDuration.toLong()

                songList.add(Song(songTitle = songTitle, songArtist = "",
                    songUri = songUri, songDuration= Utils.convertDuration(songDurationLong)))
            }

        }

    }

    private fun setUpRecyclerView(){
        songAdapter= SongsAdapter()
        binding.songsRecyclerView.apply {
            adapter = songAdapter
            setHasFixedSize(true)
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.HORIZONTAL){})
        }
        songAdapter.differ.submitList(songList)
        songList.clear()
        loadSong()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            Utils.REQUEST_CODE_FOR_PERMISSION-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSong()
                } else {
                    activity?.toast("Permission is Denied")
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSong()
        checkPermission()
        setUpRecyclerView()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}





