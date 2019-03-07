package com.nimroddayan.coroutinesplayground

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Database(
    private val ioDispatcher: CoroutineDispatcher
) : DataSource {
    private val db = mutableListOf<Tweet>()

    override suspend fun saveTweets(tweets: List<Tweet>) = withContext(ioDispatcher) {
        delay(500L)
        db += tweets
    }

    override suspend fun getTweets(): Result<List<Tweet>> = withContext(ioDispatcher) {
        try {
            delay(500L)
            return@withContext Result.Success(db)
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }
}
