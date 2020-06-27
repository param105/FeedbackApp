package com.param.feedbackapp.ui.details
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.param.feedbackapp.R
import com.param.feedbackapp.ui.mainlist.IMAGE_PATH
import kotlinx.android.synthetic.main.activity_display_image.*

class DisplayImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        val path = intent.getStringExtra(IMAGE_PATH)
        Glide.with(this).load(path).into(image_view)
    }
}
