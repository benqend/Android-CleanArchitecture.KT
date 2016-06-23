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
package com.fernandocejas.android10.sample.domain.interactor

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor
import rx.Subscriber
import rx.Observable
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).

 * By convention each UseCase implementation will return the result using a [rx.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCase protected constructor(private val threadExecutor: ThreadExecutor,
                                             private val postExecutionThread: PostExecutionThread) {

    private var subscription = Subscriptions.empty()

    /**
     * Builds an [rx.Observable] which will be used when executing the current [UseCase].
     */
    protected abstract fun buildUseCaseObservable(): Observable<Any>

    /**
     * Executes the current use case.

     * @param UseCaseSubscriber The guy who will be listen to the observable build
     * * with [.buildUseCaseObservable].
     */
    @SuppressWarnings("unchecked")
    open fun execute(UseCaseSubscriber: Subscriber<Any>) {
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
                .subscribe(UseCaseSubscriber)
    }

    /**
     * Unsubscribes from current [rx.Subscription].
     */
    fun unsubscribe() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }
}
