package ru.bupyc9.demotv

import ru.bupyc9.demotv.model.Movie

class MovieProvider private constructor() {
    private object Holder {
        val INSTANCE = MovieProvider()
    }

    companion object {
        val instance: MovieProvider by lazy { Holder.INSTANCE }
    }

    private val mItems: ArrayList<Movie> = arrayListOf()

    init {
        mItems.add(
                Movie(
                        1,
                        "Title 1",
                        "Studio 1",
                        "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg",
                        "Description 1",
                        "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"
                )
        )

        mItems.add(
                Movie(
                        2,
                        "Title 2",
                        "Studio 2",
                        "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02630.jpg",
                        "Description 2",
                        "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4"
                )
        )

        mItems.add(
                Movie(
                        3,
                        "Title 3",
                        "Studio 3",
                        "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02529.jpg",
                        "Description 3",
                        "http://corochann.com/wp-content/uploads/2015/07/MVI_1112.mp4"
                )
        )
    }

    fun getItems(): ArrayList<Movie> {
        return mItems
    }
}