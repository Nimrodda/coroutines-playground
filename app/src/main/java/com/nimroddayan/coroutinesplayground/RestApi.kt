package com.nimroddayan.coroutinesplayground

import io.reactivex.Completable
import io.reactivex.Single

class RestApi : DataSource {
    private val db = mutableListOf<Tweet>()

    override fun saveTweets(tweets: List<Tweet>): Completable {
        return Completable.fromAction {
            Thread.sleep(500L)
            db += tweets
        }
    }

    override fun getTweets(): Single<List<Tweet>> {
        return Single.fromCallable {
            Thread.sleep(500L)
            db
        }
    }
}
