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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import xiaofei.library.concurrentutils.util.Action;
import xiaofei.library.concurrentutils.util.Condition;
import xiaofei.library.concurrentutils.util.Function;
import xiaofei.library.concurrentutils.util.IdenticalFunction;
import xiaofei.library.concurrentutils.util.NonNullCondition;

/**
 * Created by Xiaofei on 16/6/30.
 */
public class ObjectCanary<T> {

    private final Condition<T> nonNullCondition = new NonNullCondition<T>();

    private final Function<T, T> identicalFunction = new IdenticalFunction<T>();

    private volatile T object;

    private final Lock lock;

    private final java.util.concurrent.locks.Condition condition;

    public ObjectCanary(T object) {
        this.object = object;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public ObjectCanary() {
        this(null);
    }

    public void actionNonNull(Action<? super T> action) {
        action(action, nonNullCondition);
    }

    public void action(Action<? super T> action) {
        action(action, null);
    }

    public void action(final Action<? super T> action, Condition<? super T> condition) {
        performFunctionUnderCondition(new Function<T, Void>() {
            @Override
            public Void call(T o) {
                action.call(o);
                return null;
            }
        }, condition);
    }

    public <R> R calculateNonNull(Function<? super T, ? extends R> function) {
        return calculate(function, nonNullCondition);
    }

    public <R> R calculate(Function<? super T, ? extends R> function) {
        return calculate(function, null);
    }

    public <R> R calculate(Function<? super T, ? extends R> function, Condition<? super T> condition) {
        return performFunctionUnderCondition(function, condition);
    }

    public void wait(Condition<? super T> condition) {
        performFunctionUnderCondition(null, condition);
    }

    public void waitUntilNonNull() {
        wait(nonNullCondition);
    }

    public boolean satisfy(Condition<? super T> condition) {
        lock.lock();
        boolean result = condition.satisfy(object);
        lock.unlock();
        return result;
    }

    public void set(T object) {
        lock.lock();
        this.object = object;
        condition.signalAll();
        lock.unlock();
    }

    public T get() {
        return object;
    }

    public T getNonNull() {
        return get(nonNullCondition);
    }

    public T get(Condition<? super T> condition) {
        return performFunctionUnderCondition(identicalFunction, condition);
    }

    private <R> R performFunctionUnderCondition( Function<? super T, ? extends R> function, Condition<? super T> condition) {
        R result = null;
        lock.lock();
        try {
            while (condition != null && !condition.satisfy(object)) {
                this.condition.await();
            }
            if (function != null) {
                result = function.call(object);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

}
