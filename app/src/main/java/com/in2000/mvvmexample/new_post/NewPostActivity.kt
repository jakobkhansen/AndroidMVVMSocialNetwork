package com.in2000.mvvmexample.new_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.in2000.mvvmexample.R
import kotlinx.android.synthetic.main.activity_new_post.*

class NewPostActivity : AppCompatActivity() {
    private val newPostViewModel = NewPostViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        initSubmitButton()
    }

    private fun initSubmitButton() {
        new_post_submit.setOnClickListener {
            val content = new_post_text_field.text.toString()
            newPostViewModel.createAndSavePost(content)
            finish()
        }
    }
}
