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

import xiaofei.library.concurrentutils.ObjectCanary;
import xiaofei.library.concurrentutils.util.Action;
import xiaofei.library.concurrentutils.util.Condition;

import static org.junit.Assert.assertEquals;

/**
 * Created by Xiaofei on 16/7/5.
 */
public class ActionNonBlockingTest {
    class A {
        volatile int i;
    }
    @Test
    public void test() throws Exception {
        {
            Condition<A> condition = new Condition<A>() {
                @Override
                public boolean satisfy(A o) {
                    return o != null && o.i == 9;
                }
            };
            ObjectCanary<A> a = new ObjectCanary<>();
            for (int i = 0; i < 100; ++i) {
                final int tmp = i;
                a.actionNonBlocking(new Action<A>() {
                    @Override
                    public void call(A o) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        System.out.println(tmp);
                    }
                }, condition);
            }
            System.out.println("start");
            A tmp = new A();
            tmp.i = 9;
            a.set(tmp);
            a.finish();
            System.out.println("end");
        }
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}