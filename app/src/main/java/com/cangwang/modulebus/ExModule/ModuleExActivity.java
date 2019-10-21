package com.cangwang.modulebus.ExModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cangwang.modulebus.R;

/**
 * Fragmetn模块分发Activity容器，包含了分发的Fragment
 */
public class ModuleExActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ModuleExFragment()).commit();
    }
}
