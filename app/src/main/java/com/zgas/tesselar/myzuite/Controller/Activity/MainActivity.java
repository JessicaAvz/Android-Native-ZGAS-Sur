package com.zgas.tesselar.myzuite.Controller.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.zgas.tesselar.myzuite.Controller.Adapter.PagerAdapter;
import com.zgas.tesselar.myzuite.Controller.Fragment.HelpFragment;
import com.zgas.tesselar.myzuite.Controller.Fragment.MainFragment;
import com.zgas.tesselar.myzuite.Controller.Fragment.OrderFragment;
import com.zgas.tesselar.myzuite.CustomViewPager;
import com.zgas.tesselar.myzuite.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "MainActivity";
    public static final String EXTRA_CASE_ID = "CaseId";
    public static final String EXTRA_CASE_TIME_IN = "CaseTimeIn";
    public static final String EXTRA_CASE_TIME_SEEN = "CaseTimeSeen";
    public static final String EXTRA_CASE_TIME_ARRIVAL = "CaseTimeArrival";
    public static final String EXTRA_CASE_TIME_PROGRAMMED = "CaseTimeProgrammed";
    public static final String EXTRA_CASE_STATUS = "CaseStatus";
    public static final String EXTRA_CASE_PRIORITY = "CasePriority";
    public static final String EXTRA_CASE_USER_NAME = "CaseUserName";
    public static final String EXTRA_CASE_USER_LASTNAME = "CaseUserLastname";
    public static final String EXTRA_CASE_USER_ID = "CaseUserId";
    public static final String EXTRA_CASE_ADDRESS = "CaseAddress";
    public static final String EXTRA_CASE_TYPE = "CaseType";

    private AHBottomNavigation mAhBottomNavigation;
    private CustomViewPager mViewPager;
    private FloatingActionButton mFabCall;
    private HelpFragment mHelpFragment;
    private MainFragment mMainFragment;
    private OrderFragment mOrderFragment;
    private PagerAdapter mPagerAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(DEBUG_TAG, "OnCreate");
        initUi();

    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment);

        mViewPager = (CustomViewPager) findViewById(R.id.activity_main_cv_view_pager);
        mViewPager.setPagingEnabled(false);
        mFabCall = (FloatingActionButton) findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mHelpFragment = new HelpFragment();
        mMainFragment = new MainFragment();
        mOrderFragment = new OrderFragment();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mOrderFragment);
        mPagerAdapter.addFragment(mMainFragment);
        mPagerAdapter.addFragment(mHelpFragment);
        mViewPager.setAdapter(mPagerAdapter);

        initBottomNavigation();
    }

    private void initBottomNavigation() {
        mAhBottomNavigation = (AHBottomNavigation) findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_fragment), R.drawable.icon_check, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_main_fragment), R.drawable.icon_gas_cylinder_menu, R.color.pink_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        mAhBottomNavigation.addItem(item1);
        mAhBottomNavigation.addItem(item2);
        mAhBottomNavigation.addItem(item3);

        mAhBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        mAhBottomNavigation.setForceTint(true);
        mAhBottomNavigation.setTranslucentNavigationEnabled(true);
        mAhBottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        mAhBottomNavigation.setCurrentItem(1);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.prompt_order_fragment);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.prompt_main_fragment);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(R.string.prompt_help_fragment);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mAhBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    mViewPager.setCurrentItem(0);
                    Log.d(DEBUG_TAG, "Hello orders fragment");
                } else if (position == 1) {
                    mViewPager.setCurrentItem(1);
                    Log.d(DEBUG_TAG, "Hello main fragment");
                } else if (position == 2) {
                    mViewPager.setCurrentItem(2);
                    Log.d(DEBUG_TAG, "Hello help fragment");
                }
                return true;
            }
        });
        mAhBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void animateFab(int pPosition) {
        switch (pPosition) {
            case 0:
                mFabCall.hide();
                break;
            case 1:
                mFabCall.show();
                break;
            case 2:
                mFabCall.hide();
                break;
            default:
                mFabCall.hide();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_fab_call:
                Log.d(DEBUG_TAG, "Llamada");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6241370525"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                break;
        }
    }
}
