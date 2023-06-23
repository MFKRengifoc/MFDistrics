package com.manoffocus.mfdistricts.components.mfplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.FragmentMfPlayerBinding

class MFPlayer : Fragment(), OnClickListener {
    private var _binding : FragmentMfPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaPlayer: MediaPlayer
    private var poiAudio : String? = null
    private var isPlaying = false


    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            poiAudio = it.getString(ID_POI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMfPlayerBinding.inflate(inflater, container, false)
        mediaPlayer = MediaPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePlayer()
        binding.mfFragmentPlayerBut.setOnClickListener(this)
    }

    private fun preparePlayer(){
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        mediaPlayer.setAudioAttributes(audioAttributes)
        mediaPlayer.apply {
            reset()
            setDataSource(poiAudio)
            prepareAsync()
            setOnPreparedListener {
                binding.mfPlayerTimeAll.externalSetText(getTotalTime())
            }
        }
    }
    private fun play(){
        mediaPlayer.start()
        binding.mfFragmentPlayerBut.externalSetImageInt(R.drawable.mf_pause_icon)
        isPlaying = true
        updateMediaPlayerTime()
    }
    private fun pause(){
        binding.mfFragmentPlayerBut.externalSetImageInt(R.drawable.mf_play_icon)
        mediaPlayer.pause()
        isPlaying = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun getTotalTime(): String{
        val totalTime = mediaPlayer.duration
        val secs = totalTime / 1000
        val mins = secs / 60
        val leftSecs = secs % 60
        return String.format("%02d:%02d", mins, leftSecs)
    }
    private fun getLeftTime(): String {
        val totalTime = mediaPlayer.duration
        val currentTime = mediaPlayer.currentPosition
        val leftTime = totalTime - currentTime

        val secs = leftTime / 1000
        val mins = secs / 60
        val leftSecs = secs % 60
        return String.format("%02d:%02d", mins, leftSecs)
    }

    private fun updateMediaPlayerTime() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val leftTime = getLeftTime()
                updateLabel(leftTime)
                val percent = (mediaPlayer.currentPosition * 100) / mediaPlayer.duration
                updateSeekBar(percent)
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }
    private fun updateSeekBar(percent: Int){
        binding.mfFragmentPlayerSeekbar.progress = percent.toInt()
    }
    private fun updateLabel(lefTime: String){
        binding.mfPlayerTime.externalSetText(lefTime)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.mf_fragment_player_but -> {
                if (!isPlaying){
                    play()
                } else {
                    pause()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pause()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val TAG = "MFPlayer"
        const val ID_POI = "id_poi"
        @JvmStatic
        fun newInstance(poiAudio: String) =
            MFPlayer().apply {
                arguments = Bundle().apply {
                    putString(ID_POI, poiAudio)
                }
            }
    }
}