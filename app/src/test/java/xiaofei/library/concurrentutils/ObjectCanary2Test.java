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

import org.junit.Test;

import xiaofei.library.concurrentutils.util.Action;
import xiaofei.library.concurrentutils.util.Function;

/**
 * Created by Xiaofei on 16/8/31.
 */
public class ObjectCanary2Test {

    class A {
        volatile int i;
    }
    @Test
    public void test() throws Exception {
        {
            final ObjectCanary2<A> a = new ObjectCanary2<>();
            for (int i = 0; i < 5; ++i) {
                final int tmp = i;
                a.action(new Action<A>() {
                    @Override
                    public void call(A o) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + ":" + tmp);
                    }
                });
            }
            System.out.println("start");
            A tmp = new A();
            tmp.i = 9;
            System.out.println("set");
            a.set(tmp);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int tmp = 100;
                    a.action(new Action<A>() {
                        @Override
                        public void call(A o) {
                            System.out.println(Thread.currentThread().getName() + ":" + tmp);
                        }
                    });
                    System.out.println(Thread.currentThread().getName() + " get:" + a.calculate(new Function<A, Integer>() {
                        @Override
                        public Integer call(A o) {
                            System.out.println(Thread.currentThread().getName() + " inside 1");
                            return o.i;
                        }
                    }));
                }
            }.start();
            System.out.println(Thread.currentThread().getName() + " get:" + a.calculate(new Function<A, Integer>() {
                @Override
                public Integer call(A o) {
                    System.out.println(Thread.currentThread().getName() + " inside 2");
                    return o.i;
                }
            }));
            System.out.println("end");
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
