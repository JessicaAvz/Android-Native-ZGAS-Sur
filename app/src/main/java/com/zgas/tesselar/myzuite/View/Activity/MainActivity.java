package com.zgas.tesselar.myzuite.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Adapter.PagerAdapter;
import com.zgas.tesselar.myzuite.View.Adapter.SupervisorAdapter;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.HelpFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.MainFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.HelpFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.MainFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.OrderFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserService.HelpFragmentService;
import com.zgas.tesselar.myzuite.View.Fragment.UserService.MainFragmentService;
import com.zgas.tesselar.myzuite.CustomViewPager;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Controller.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetUserInfoTask.UserInfoListener {

    private static final String DEBUG_TAG = "MainActivity";

    private AHBottomNavigation mAhBottomNavigation;
    private CustomViewPager mViewPager;
    private FloatingActionButton mFabCall;
    private FloatingActionButton mFabCallSupervisor;
    private HelpFragmentOperator mHelpFragmentOperator;
    private MainFragmentOperator mMainFragmentOperator;
    private OrderFragmentOperator mOrderFragmentOperator;
    private MainFragmentService mMainFragmentService;
    private HelpFragmentService mHelpFragmentService;
    private HelpFragmentLeak mHelpFragmentLeak;
    private MainFragmentLeak mMainFragmentLeak;
    private PagerAdapter mPagerAdapter;
    private SupervisorAdapter mSupervisorAdapter;

    private RecyclerView mRecyclerViewSupervised;
    private Toolbar toolbar;
    private UserPreferences mUserPreferences;
    private LinearLayoutManager linearLayoutManager;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        initUiOperator();
        JSONObject params = new JSONObject();
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();

        Log.d(DEBUG_TAG, "Admin token: " + mUserPreferences.getAdminToken());
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getLoginObject().getLoginEmail());

        if (mUser.getUserType() == User.userType.OPERATOR) {
            Log.d(DEBUG_TAG, "OnCreate Operator");
            setContentView(R.layout.activity_main);
            initUiOperator();
        } else if (mUser.getUserType() == User.userType.SERVICE) {
            Log.d(DEBUG_TAG, "OnCreate Servicio medido");
            setContentView(R.layout.activity_main);
            initUiService();
        } else if (mUser.getUserType() == User.userType.SUPERVISOR) {
            Log.d(DEBUG_TAG, "OnCreate Supervisor");
            setContentView(R.layout.activity_supervisor);
            try {
                params.put(ExtrasHelper.EMAIL_TAG, mUserPreferences.getLoginObject().getLoginEmail());
                params.put(ExtrasHelper.ADMIN_TOKEN, mUserPreferences.getAdminToken());
                Log.d(DEBUG_TAG, "Parámetros: " + params.getString(ExtrasHelper.EMAIL_TAG) + " " + params.getString(ExtrasHelper.ADMIN_TOKEN));
                GetUserInfoTask getUserInfoTask = new GetUserInfoTask(this, params);
                getUserInfoTask.setUserInfoListener(this);
                getUserInfoTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            initUiSupervisor();
        } else if (mUser.getUserType() == User.userType.LEAKAGE) {
            setContentView(R.layout.activity_main);
            Log.d(DEBUG_TAG, "OnCreate Técnico de fugas");
            initUiLeakage();
        } else {
            Log.d(DEBUG_TAG, "OnCreate Default");
            setContentView(R.layout.activity_main);
            initUiOperator();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_fab_call:
                Log.d(DEBUG_TAG, "Llamada");
                callIntent();
                break;
            case R.id.ativity_supervisor_fab_call:
                Log.d(DEBUG_TAG, "Llamada");
                callIntent();
                break;
        }
    }

    private void initUiOperator() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment);

        mViewPager = (CustomViewPager) findViewById(R.id.activity_main_cv_view_pager);
        mViewPager.setPagingEnabled(false);
        mFabCall = (FloatingActionButton) findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mHelpFragmentOperator = new HelpFragmentOperator();
        mMainFragmentOperator = new MainFragmentOperator();
        mOrderFragmentOperator = new OrderFragmentOperator();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mOrderFragmentOperator);
        mPagerAdapter.addFragment(mMainFragmentOperator);
        mPagerAdapter.addFragment(mHelpFragmentOperator);
        mViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationOperator();
    }

    private void initUiService() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment_service);

        mViewPager = (CustomViewPager) findViewById(R.id.activity_main_cv_view_pager);
        mViewPager.setPagingEnabled(false);
        mFabCall = (FloatingActionButton) findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mMainFragmentService = new MainFragmentService();
        mHelpFragmentService = new HelpFragmentService();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mMainFragmentService);
        mPagerAdapter.addFragment(mHelpFragmentService);
        mViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationService();
    }

    private void initUiSupervisor() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_supervised);
        mFabCallSupervisor = (FloatingActionButton) findViewById(R.id.ativity_supervisor_fab_call);
        mFabCallSupervisor.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSupervised = (RecyclerView) findViewById(R.id.activity_supervisor_recycler_view);
    }

    private void initUiLeakage() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_order_leak);

        mViewPager = (CustomViewPager) findViewById(R.id.activity_main_cv_view_pager);
        mViewPager.setPagingEnabled(false);
        mFabCall = (FloatingActionButton) findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mMainFragmentLeak = new MainFragmentLeak();
        mHelpFragmentLeak = new HelpFragmentLeak();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mMainFragmentLeak);
        mPagerAdapter.addFragment(mHelpFragmentLeak);
        mViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationLeakage();
    }

    private void initBottomNavigationOperator() {
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
                animateFabOperator(position);
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
                } else if (position == 1) {
                    mViewPager.setCurrentItem(1);
                } else if (position == 2) {
                    mViewPager.setCurrentItem(2);
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

    private void initBottomNavigationService() {
        mAhBottomNavigation = (AHBottomNavigation) findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_main_fragment_service), R.drawable.icon_gas_cylinder_menu, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        mAhBottomNavigation.addItem(item1);
        mAhBottomNavigation.addItem(item2);

        mAhBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        mAhBottomNavigation.setForceTint(true);
        mAhBottomNavigation.setTranslucentNavigationEnabled(true);
        mAhBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mAhBottomNavigation.setCurrentItem(0);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.prompt_main_fragment_service);
                        break;
                    case 1:
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
                } else if (position == 1) {
                    mViewPager.setCurrentItem(1);
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

    private void initBottomNavigationLeakage() {
        mAhBottomNavigation = (AHBottomNavigation) findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_leak), R.drawable.icon_truck_fast, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        mAhBottomNavigation.addItem(item1);
        mAhBottomNavigation.addItem(item2);

        mAhBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setBehaviorTranslationEnabled(false);
        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        mAhBottomNavigation.setForceTint(true);
        mAhBottomNavigation.setTranslucentNavigationEnabled(true);
        mAhBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mAhBottomNavigation.setCurrentItem(0);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.prompt_order_leak);
                        break;
                    case 1:
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
                } else if (position == 1) {
                    mViewPager.setCurrentItem(1);
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
                mFabCall.show();
                break;
            case 1:
                mFabCall.hide();
                break;
            default:
                mFabCall.hide();
                break;
        }
    }

    private void animateFabOperator(int pPosition) {
        switch (pPosition) {
            case 0:
                mFabCall.hide();
                break;
            case 1:
                mFabCall.show();
                break;
            default:
                mFabCall.hide();
                break;
        }
    }

    private void callIntent() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(UrlHelper.CALL));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                mUserPreferences.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void userInfoErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoSuccessResponse(User user) {
        Log.d(DEBUG_TAG, "User preference id: " + mUserPreferences.getUserObject().getUserId());
        Log.d(DEBUG_TAG, "User preference name: " + mUserPreferences.getUserObject().getUserName());
        Log.d(DEBUG_TAG, "User preference type: " + mUserPreferences.getUserObject().getUserType());
        Log.d(DEBUG_TAG, "User preference email: " + mUserPreferences.getUserObject().getUserEmail());
        Log.d(DEBUG_TAG, "User preference zone: " + mUserPreferences.getUserObject().getUserZone());
        Log.d(DEBUG_TAG, "User preference route: " + mUserPreferences.getUserObject().getUserRoute());
        Log.d(DEBUG_TAG, "User preference status: " + mUserPreferences.getUserObject().getUserstatus());
    }

    @Override
    public void userSupervisedSuccessResponse(List<User> userList) {
        mSupervisorAdapter = new SupervisorAdapter(this, (ArrayList<User>) userList);
        mRecyclerViewSupervised.setLayoutManager(linearLayoutManager);
        mRecyclerViewSupervised.setAdapter(mSupervisorAdapter);
        mRecyclerViewSupervised.setHasFixedSize(true);
        mRecyclerViewSupervised.setItemViewCacheSize(20);
        mRecyclerViewSupervised.setDrawingCacheEnabled(true);
    }
}
