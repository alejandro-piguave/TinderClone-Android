package com.apiguave.tinderclone.ui.extension

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.openActivity(cls: Class<*>){
    val intent = Intent(this, cls)
    startActivity(intent)
    finish()
}