package com.nimroddayan.coroutinesplayground

import java.io.Closeable
import java.lang.Exception

interface DataSource : Closeable {
    fun saveTweets(tweets: List<Tweet>, onResult: (isSuccess: Boolean, error: Exception?) -> Unit)
    fun getTweets(onResult: (isSuccess: Boolean, error: Exception?, tweets: List<Tweet>) -> Unit)
}