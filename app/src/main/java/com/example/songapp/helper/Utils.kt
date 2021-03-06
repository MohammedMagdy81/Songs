package com.example.songapp.helper

import android.content.Context
import android.widget.Toast
import java.util.concurrent.TimeUnit

object Utils {
    
    const val REQUEST_CODE_FOR_PERMISSION = 111
    
    fun Context.toast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun convertDuration(duration:Long):String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        )
    }
}