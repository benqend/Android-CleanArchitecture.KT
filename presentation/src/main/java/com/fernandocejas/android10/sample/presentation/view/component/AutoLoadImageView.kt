/**
 * Copyright (C) 2014 android10.org. All rights reserved.

 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.component

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * Simple implementation of [android.widget.ImageView] with extended features like setting an
 * image from an url and an internal file cache using the application cache directory.
 */
class AutoLoadImageView : ImageView {

    private var imageUrl: String = ""
    private var imagePlaceHolderResId = -1
    private val cache = DiskCache(context.cacheDir)

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.imagePlaceHolderResId = this.imagePlaceHolderResId
        savedState.imageUrl = this.imageUrl
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.getSuperState())
        this.imagePlaceHolderResId = savedState.imagePlaceHolderResId
        this.imageUrl = savedState.imageUrl
        this.setImageUrl(this.imageUrl)
    }

    /**
     * Set an image from a remote url.

     * @param imageUrl The url of the resource to load.
     */
    fun setImageUrl(imageUrl: String?) {
        this.imageUrl = imageUrl?:""
        this@AutoLoadImageView.loadImagePlaceHolder()
        if (imageUrl != null) {
            this.loadImageFromUrl(this.imageUrl)
        } else {
            this.loadImagePlaceHolder()
        }
    }

    /**
     * Set a place holder used for loading when an image is being downloaded from the internet.

     * @param resourceId The resource id to use as a place holder.
     */
    fun setImagePlaceHolder(resourceId: Int) {
        this.imagePlaceHolderResId = resourceId
        this.loadImagePlaceHolder()
    }

    /**
     * Invalidate the internal cache by evicting all cached elements.
     */
    fun invalidateImageCache() {
        this.cache?.evictAll()
    }

    /**
     * Loads and image from the internet (and cache it) or from the internal cache.

     * @param imageUrl The remote image url to load.
     */
    private fun loadImageFromUrl(imageUrl: String) {
        object : Thread() {
            override fun run() {
                val bitmap = this@AutoLoadImageView.getFromCache(getFileNameFromUrl(imageUrl))
                if (bitmap != null) {
                    this@AutoLoadImageView.loadBitmap(bitmap)
                } else {
                    if (isThereInternetConnection) {
                        val imageDownloader = ImageDownloader()
                        imageDownloader.download(imageUrl, object : ImageDownloader.Callback {
                            override fun onImageDownloaded(bitmap: Bitmap) {
                                this@AutoLoadImageView.cacheBitmap(bitmap, getFileNameFromUrl(imageUrl))
                                this@AutoLoadImageView.loadBitmap(bitmap)
                            }

                            override fun onError() {
                                this@AutoLoadImageView.loadImagePlaceHolder()
                            }
                        })
                    } else {
                        this@AutoLoadImageView.loadImagePlaceHolder()
                    }
                }
            }
        }.start()
    }

    /**
     * Run the operation of loading a bitmap on the UI thread.

     * @param bitmap The image to load.
     */
    private fun loadBitmap(bitmap: Bitmap) {
        (context as Activity).runOnUiThread { this@AutoLoadImageView.setImageBitmap(bitmap) }
    }

    /**
     * Loads the image place holder if any has been assigned.
     */
    private fun loadImagePlaceHolder() {
        if (this.imagePlaceHolderResId != -1) {
            (context as Activity).runOnUiThread {
                this@AutoLoadImageView.setImageResource(
                        this@AutoLoadImageView.imagePlaceHolderResId)
            }
        }
    }

    /**
     * Get a [android.graphics.Bitmap] from the internal cache or null if it does not exist.

     * @param fileName The name of the file to look for in the cache.
     * *
     * @return A valid cached bitmap, otherwise null.
     */
    private fun getFromCache(fileName: String): Bitmap? {
        var bitmap: Bitmap? = null
        if (this.cache != null) {
            bitmap = this.cache[fileName]
        }
        return bitmap
    }

    /**
     * Cache an image using the internal cache.

     * @param bitmap The bitmap to cache.
     * *
     * @param fileName The file name used for caching the bitmap.
     */
    private fun cacheBitmap(bitmap: Bitmap, fileName: String) {
        this.cache?.put(bitmap, fileName)
    }

    /**
     * Checks if the device has any active internet connection.

     * @return true device with internet connection, otherwise false.
     */
    private val isThereInternetConnection: Boolean
        get() {
            val isConnected: Boolean

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting

            return isConnected
        }

    /**
     * Creates a file name from an image url

     * @param imageUrl The image url used to build the file name.
     * *
     * @return An String representing a unique file name.
     */
    private fun getFileNameFromUrl(imageUrl: String): String {
        //we could generate an unique MD5/SHA-1 here
        var hash = imageUrl.hashCode().toString()
        if (hash.startsWith("-")) {
            hash = hash.substring(1)
        }
        return BASE_IMAGE_NAME_CACHED + hash
    }

    /**
     * Class used to download images from the internet
     */
    private class ImageDownloader internal constructor() {
        internal interface Callback {
            fun onImageDownloaded(bitmap: Bitmap)

            fun onError()
        }

        /**
         * Download an image from an url.

         * @param imageUrl The url of the image to download.
         * *
         * @param callback A callback used to be reported when the task is finished.
         */
        internal fun download(imageUrl: String, callback: Callback?) {
            try {
                val conn = URL(imageUrl).openConnection()
                conn.connect()
                val bitmap = BitmapFactory.decodeStream(conn.inputStream)
                callback?.onImageDownloaded(bitmap)
            } catch (e: MalformedURLException) {
                reportError(callback)
            } catch (e: IOException) {
                reportError(callback)
            }

        }

        /**
         * Report an error to the caller

         * @param callback Caller implementing [Callback]
         */
        private fun reportError(callback: Callback?) {
            callback?.onError()
        }
    }

    /**
     * A simple disk cache implementation
     */
    private class DiskCache internal constructor(private val cacheDir: File) {

        /**
         * Get an element from the cache.

         * @param fileName The name of the file to look for.
         * *
         * @return A valid element, otherwise false.
         */
        @Synchronized operator fun get(fileName: String): Bitmap? {
            var bitmap: Bitmap? = null
            val file = buildFileFromFilename(fileName)
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.path)
            }
            return bitmap
        }

        /**
         * Cache an element.

         * @param bitmap The bitmap to be put in the cache.
         * *
         * @param fileName A string representing the name of the file to be cached.
         */
        @Synchronized internal fun put(bitmap: Bitmap, fileName: String) {
            val file = buildFileFromFilename(fileName)
            if (!file.exists()) {
                try {
                    val fileOutputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: FileNotFoundException) {
                    Log.e(TAG, e.message)
                } catch (e: IOException) {
                    Log.e(TAG, e.message)
                }

            }
        }

        /**
         * Invalidate and expire the cache.
         */
        internal fun evictAll() {
            if (cacheDir.exists()) {
                for (file in cacheDir.listFiles()) {
                    file.delete()
                }
            }
        }

        /**
         * Creates a file name from an image url

         * @param fileName The image url used to build the file name.
         * *
         * @return A [java.io.File] representing a unique element.
         */
        private fun buildFileFromFilename(fileName: String): File {
            val fullPath = this.cacheDir.path + File.separator + fileName
            return File(fullPath)
        }

        companion object {

            private val TAG = "DiskCache"
        }
    }

    private class SavedState : View.BaseSavedState {
        internal var imagePlaceHolderResId: Int = 0
        internal var imageUrl: String = ""

        internal constructor(superState: Parcelable) : super(superState) {
        }

        private constructor(`in`: Parcel) : super(`in`) {
            this.imagePlaceHolderResId = `in`.readInt()
            this.imageUrl = `in`.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.imagePlaceHolderResId)
            out.writeString(this.imageUrl)
        }

        companion object {
            @JvmStatic
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {

        private val BASE_IMAGE_NAME_CACHED = "image_"
    }
}
