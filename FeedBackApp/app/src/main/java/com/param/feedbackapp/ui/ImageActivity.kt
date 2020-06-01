package com.param.feedbackapp.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.param.feedbackapp.R
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val path = intent.getStringExtra(IMAGE_PATH)
        Glide.with(this).load(path).into(image_view)
    }
}
