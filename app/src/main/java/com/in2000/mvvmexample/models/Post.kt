package com.in2000.mvvmexample.models

import com.google.firebase.Timestamp

data class Post(val date : Timestamp = Timestamp.now(), val content: String? = "", val userFullname: String? = "", val userId: String? = "", val userImage: String? = "")