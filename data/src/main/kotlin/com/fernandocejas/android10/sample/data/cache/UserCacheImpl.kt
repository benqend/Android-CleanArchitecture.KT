/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.cache

import android.content.Context
import com.fernandocejas.android10.sample.data.cache.serializer.JsonSerializer
import com.fernandocejas.android10.sample.data.entity.UserEntity
import com.fernandocejas.android10.sample.data.exception.UserNotFoundException
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import rx.Observable

/**
 * [UserCache] implementation.
 */
@Singleton
class UserCacheImpl
/**
 * Constructor of the class [UserCacheImpl].

 * @param context A
 * *
 * @param userCacheSerializer [JsonSerializer] for object serialization.
 * *
 * @param fileManager [FileManager] for saving serialized objects to the file system.
 */
@Inject
constructor(context: Context?, private val serializer: JsonSerializer,
            private val fileManager: FileManager, private val threadExecutor: ThreadExecutor) : UserCache {

    private val context: Context
    private val cacheDir: File

    init {
        if (context == null || serializer == null || fileManager == null || threadExecutor == null) {
            throw IllegalArgumentException("Invalid null parameter")
        }
        this.context = context.applicationContext
        this.cacheDir = this.context.cacheDir
    }

    override fun get(userId: Int): Observable<UserEntity> {
        return Observable.create<UserEntity> { subscriber ->
            val userEntityFile = this@UserCacheImpl.buildFile(userId)
            val fileContent = this@UserCacheImpl.fileManager.readFileContent(userEntityFile)
            val userEntity = this@UserCacheImpl.serializer.deserialize(fileContent)

            if (userEntity != null) {
                subscriber.onNext(userEntity)
                subscriber.onCompleted()
            } else {
                subscriber.onError(UserNotFoundException())
            }
        }
    }

    override fun put(userEntity: UserEntity) {
        if (userEntity != null) {
            val userEntitiyFile = this.buildFile(userEntity.userId)
            if (!isCached(userEntity.userId)) {
                val jsonString = this.serializer.serialize(userEntity)
                this.executeAsynchronously(CacheWriter(this.fileManager, userEntitiyFile,
                        jsonString))
                setLastCacheUpdateTimeMillis()
            }
        }
    }

    override fun isCached(userId: Int): Boolean {
        val userEntitiyFile = this.buildFile(userId)
        return this.fileManager.exists(userEntitiyFile)
    }

    override val isExpired: Boolean
        get() {
            val currentTime = System.currentTimeMillis()
            val lastUpdateTime = this.lastCacheUpdateTimeMillis

            val expired = currentTime - lastUpdateTime > EXPIRATION_TIME

            if (expired) {
                this.evictAll()
            }

            return expired
        }

    override fun evictAll() {
        this.executeAsynchronously(CacheEvictor(this.fileManager, this.cacheDir))
    }

    /**
     * Build a file, used to be inserted in the disk cache.

     * @param userId The id user to build the file.
     * *
     * @return A valid file.
     */
    private fun buildFile(userId: Int): File {
        val fileNameBuilder = StringBuilder()
        fileNameBuilder.append(this.cacheDir.path)
        fileNameBuilder.append(File.separator)
        fileNameBuilder.append(DEFAULT_FILE_NAME)
        fileNameBuilder.append(userId)

        return File(fileNameBuilder.toString())
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private fun setLastCacheUpdateTimeMillis() {
        val currentMillis = System.currentTimeMillis()
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis)
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private val lastCacheUpdateTimeMillis: Long
        get() = this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE)

    /**
     * Executes a [Runnable] in another Thread.

     * @param runnable [Runnable] to execute
     */
    private fun executeAsynchronously(runnable: Runnable) {
        this.threadExecutor.execute(runnable)
    }

    /**
     * [Runnable] class for writing to disk.
     */
    private class CacheWriter internal constructor(private val fileManager: FileManager, private val fileToWrite: File, private val fileContent: String) : Runnable {

        override fun run() {
            this.fileManager.writeToFile(fileToWrite, fileContent)
        }
    }

    /**
     * [Runnable] class for evicting all the cached files
     */
    private class CacheEvictor internal constructor(private val fileManager: FileManager, private val cacheDir: File) : Runnable {

        override fun run() {
            this.fileManager.clearDirectory(this.cacheDir)
        }
    }

    companion object {

        private val SETTINGS_FILE_NAME = "com.fernandocejas.android10.SETTINGS"
        private val SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update"

        private val DEFAULT_FILE_NAME = "user_"
        private val EXPIRATION_TIME = 60 * 10 * 1000.toLong()
    }
}
