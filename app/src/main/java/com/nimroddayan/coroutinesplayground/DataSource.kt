package com.nimroddayan.coroutinesplayground

import io.reactivex.Completable
import io.reactivex.Single

interface DataSource {
    fun saveTweets(tweets: List<Tweet>): Completable
    fun getTweets(): Single<List<Tweet>>
}