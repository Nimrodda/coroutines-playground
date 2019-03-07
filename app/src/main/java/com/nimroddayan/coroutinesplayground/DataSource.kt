package com.nimroddayan.coroutinesplayground

interface DataSource {
    suspend fun saveTweets(tweets: List<Tweet>)
    suspend fun getTweets(): Result<List<Tweet>>
}
