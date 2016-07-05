/**
 *
 * Copyright 2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.concurrentutils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Xiaofei on 16/7/4.
 */
public class ConcurrentQueue<T> {

    private final ConcurrentLinkedQueue<T> queue;

    public ConcurrentQueue() {
        queue = new ConcurrentLinkedQueue<T>();
    }

    public T poll() {
        synchronized (this) {
            return queue.poll();
        }
    }

    public boolean offerIfNotEmpty(T e) {
        synchronized (this) {
            return !queue.isEmpty() && queue.offer(e);
        }
    }

    public boolean offer(T e) {
        synchronized (this) {
            return queue.offer(e);
        }
    }
}
