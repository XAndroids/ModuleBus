package com.cangwang.modulebus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.cangwang.modulebus.ExModule.ModuleMainExActivity;

/**
 * 启动页
 */
public class AdviceActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startActivity(new Intent(this,ModuleMainExActivity.class));
        finish();
    }
}
