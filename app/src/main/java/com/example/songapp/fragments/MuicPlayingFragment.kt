package com.example.songapp.fragments

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.songapp.R
import com.example.songapp.databinding.FragmentMuicPlayingBinding
import com.example.songapp.helper.Utils
import com.example.songapp.models.Song

class MuicPlayingFragment : Fragment(R.layout.fragment_muic_playing) , SeekBar.OnSeekBarChangeListener {

    private var _binding : FragmentMuicPlayingBinding?=null
    private val binding get() = _binding!!
    private val args : MuicPlayingFragmentArgs by navArgs()
    private lateinit var song:Song
    private  var mediaPlayer:MediaPlayer?=null
    private var seekLength:Int = 0
    private val seekForwardTime= 5000
    private val seekbackwardTime= 5000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMuicPlayingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        song= args.song
        mediaPlayer= MediaPlayer()
        setUpSong(song)
        setUpListener()

    }

    private fun setUpListener() {
        binding.ibPlay.setOnClickListener {
            playSong()
        }
        binding.seekBar.setOnSeekBarChangeListener(this)
        binding.ibForwardSong.setOnClickListener {
            forwardSong()
        }
        binding.ibBackwardSong.setOnClickListener {
            backWardSong()
        }
        binding.ibRepeat.setOnClickListener {
            repeatSong()
        }
        displaySongArt()
    }

    private fun displaySongArt() {
        val mediaMetadataRetriever= MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(song.songUri)
        val data = mediaMetadataRetriever.embeddedPicture
        if (data != null){
            val bitmap = BitmapFactory.decodeByteArray(data,0,data.size)
            binding.ibCover.setImageBitmap(bitmap)
        }
    }

    private fun repeatSong() {
        if (!mediaPlayer!!.isLooping){
           setUpRepeatSong(true , R.drawable.ic_repeat_one)
        }
        else{
            setUpRepeatSong(false, R.drawable.ic_repeat)
        }
    }

    fun setUpRepeatSong(isLooping:Boolean , image:Int){
        mediaPlayer!!.isLooping=isLooping
        setImageDrawablebtnRepeat(image)
    }


    private fun backWardSong() {
        if (mediaPlayer!= null){
            val currentPosition = mediaPlayer!!.currentPosition
            if (mediaPlayer!!.currentPosition - seekForwardTime>=0){
                mediaPlayer!!.seekTo(currentPosition - seekbackwardTime)
            }else{
                mediaPlayer!!.seekTo(0)
            }

        }
    }

    private fun forwardSong() {

        if (mediaPlayer != null){
            val currentPosition = mediaPlayer!!.currentPosition
            if (mediaPlayer!!.currentPosition+seekForwardTime<=mediaPlayer!!.duration){
                mediaPlayer?.seekTo(currentPosition+seekForwardTime)
            }else{
                mediaPlayer!!.seekTo(mediaPlayer!!.duration)
            }

        }
    }

    private fun playSong() {
        if (!mediaPlayer!!.isPlaying){
            mediaPlayer!!.apply {
                reset()
                setDataSource(song.songUri)
                prepare()
                seekTo(seekLength)
                start()
                setImageDrawable(R.drawable.ic_pause)
            }
            updateSeekBar()
        }else{
                mediaPlayer!!.pause()
                seekLength= mediaPlayer!!.currentPosition
                setImageDrawable(R.drawable.ic_play )
        }

    }

    private fun setImageDrawable(image:Int) {
        binding.ibPlay.setImageDrawable(activity?.let { ContextCompat.getDrawable(it,image) })
    }


    private fun setImageDrawablebtnRepeat(image:Int) {
        binding.ibRepeat.setImageDrawable(activity?.let { ContextCompat.getDrawable(it,image) })
    }

    private fun updateSeekBar(){
        if (mediaPlayer!= null&&mediaPlayer!!!!.isPlaying){
            binding.tvCurrentTime.text = Utils.convertDuration(mediaPlayer!!!!.currentPosition.toLong())
        }
        setUpSeekbar()
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

    var runnable = Runnable { updateSeekBar() }

    private fun setUpSeekbar() {
        if (mediaPlayer!=null &&mediaPlayer!!.isPlaying){
            binding.seekBar.apply {
                progress= mediaPlayer!!.currentPosition
                max= mediaPlayer!!.duration
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser){
            mediaPlayer!!.seekTo(progress)
            binding.tvCurrentTime.text = Utils.convertDuration(progress.toLong())
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (mediaPlayer!!!= null&&mediaPlayer!!.isPlaying){
            if (seekBar !=null){
                mediaPlayer!!.seekTo(seekBar.progress)
            }
        }
    }

    private fun setUpSong(song: Song) {
        binding.tvAuthor.text=""
        binding.tvTitle.text= song.songTitle
        binding.tvDuration.text= song.songDuration
    }

    private fun clearmediaPlayer(){
        if (mediaPlayer != null){
            if (mediaPlayer!!.isPlaying){
                mediaPlayer!!.stop()
            }
        }
        mediaPlayer!!.release()
        mediaPlayer =null
    }

    override fun onResume() {
        super.onResume()
        playSong()
    }

    override fun onStop() {
        super.onStop()
        playSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearmediaPlayer()
    }

}