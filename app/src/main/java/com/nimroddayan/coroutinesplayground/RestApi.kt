package com.nimroddayan.coroutinesplayground

import java.lang.Exception
import java.util.concurrent.ExecutorService

class RestApi(
    private val ioExecutor: ExecutorService
) : DataSource {
    private val db = mutableListOf<Tweet>()

    override fun saveTweets(tweets: List<Tweet>, onResult: (isSuccess: Boolean, error: Exception?) -> Unit) {
        ioExecutor.execute {
            Thread.sleep(500L)
            db += tweets
            onResult(true, null)
        }
    }

    override fun getTweets(onResult: (isSuccess: Boolean, error: Exception?, tweets: List<Tweet>) -> Unit) {
        ioExecutor.execute {
            Thread.sleep(500L)
            onResult(true, null, db)
        }
    }

    override fun close() {
        ioExecutor.shutdownNow()
    }
}
