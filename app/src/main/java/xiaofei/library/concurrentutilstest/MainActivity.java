package xiaofei.library.concurrentutilstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xiaofei.library.concurrentutils.ObjectCanary;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        class A{}
        class B extends A{}
        ObjectCanary<A> o = new ObjectCanary<>(new B());
        o.set(new B());
    }
}
