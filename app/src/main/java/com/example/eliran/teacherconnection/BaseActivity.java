package com.example.eliran.teacherconnection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

abstract class BaseActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // this is a generic way of getting your root view element
        View rootView = (View) findViewById(android.R.id.content);
        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.z123));
    }
}