package com.finchina.treeview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_adapter:
                start(this, MainActivity.class);
                break;
            case R.id.btn_list2:
                start(this, SecondActivity.class);
                break;
            case R.id.btn_header:
                start(this, HeaderActivity.class);
                break;
        }
    }

    private void start(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
}