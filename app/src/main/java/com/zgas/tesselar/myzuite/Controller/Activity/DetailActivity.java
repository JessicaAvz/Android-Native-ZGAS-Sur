package com.zgas.tesselar.myzuite.Controller.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.zgas.tesselar.myzuite.R;

public class DetailActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "DetailActivity";
    private Bundle mBundle;
    private int mVisitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(DEBUG_TAG, "OnCreate");
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);

        initUi();
    }

    private void initUi() {
        mBundle = getIntent().getExtras();
        mVisitId = mBundle.getInt(MainActivity.EXTRA_CASE_ID);
        Log.d("DEBUG_TAG", String.valueOf(mVisitId));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del pedido " + mVisitId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
    }
}
