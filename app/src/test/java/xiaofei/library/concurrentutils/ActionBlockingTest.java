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
import xiaofei.library.concurrentutils.util.Condition;

/**
 * Created by Xiaofei on 16/7/5.
 */
public class ActionBlockingTest {
    class B {
        volatile int i;
    }
    @Test
    public void test() throws Exception {
        {
            final Condition<B> condition = new Condition<B>() {
                @Override
                public boolean satisfy(B o) {
                    return o != null && o.i == 9;
                }
            };
            final ObjectCanary<B> a = new ObjectCanary<>();
            for (int i = 0; i < 10; ++i) {
                final int tmp = i;
                new Thread() {
                    public void run() {
                        a.action(new Action<B>() {
                            @Override
                            public void call (B o){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread().getName() + ":" + tmp);
                            }
                        }, condition);
                    }
                }.start();
            }
            System.out.println("start");
            B tmp = new B();
            tmp.i = 9;
            a.set(tmp);
            System.out.println("end");
        }
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}