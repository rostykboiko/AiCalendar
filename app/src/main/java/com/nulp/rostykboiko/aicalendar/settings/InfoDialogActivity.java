package com.nulp.rostykboiko.aicalendar.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nulp.rostykboiko.aicalendar.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dialog);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.close_button)
    void close() {
        onBackPressed();
        finish();
    }

}
