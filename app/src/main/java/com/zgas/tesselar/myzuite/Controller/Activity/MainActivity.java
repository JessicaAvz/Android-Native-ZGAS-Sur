package com.zgas.tesselar.myzuite.Controller.Activity;

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

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.zgas.tesselar.myzuite.Controller.Adapter.PagerAdapter;
import com.zgas.tesselar.myzuite.Controller.Adapter.SupervisorAdapter;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserLeakage.HelpFragmentLeak;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserLeakage.MainFragmentLeak;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator.HelpFragmentOperator;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator.MainFragmentOperator;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator.OrderFragmentOperator;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserService.HelpFragmentService;
import com.zgas.tesselar.myzuite.Controller.Fragment.UserService.MainFragmentService;
import com.zgas.tesselar.myzuite.CustomViewPager;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.util.ArrayList;

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

    public static final String EXTRA_USER_ID = "UserId";
    public static final String EXTRA_USER_NAME = "UserName";
    public static final String EXTRA_USER_LASTNAME = "UserLastname";
    public static final String EXTRA_USER_EMAIL = "UserEmail";
    public static final String EXTRA_USER_TYPE = "UserType";
    public static final String EXTRA_USER_STATUS = "UserStatus";
    public static final String EXTRA_USER_ROUTE = "UserRoute";
    public static final String EXTRA_USER_ZONE = "UserZone";

    private final ArrayList<User> mUserList = new ArrayList();

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

    private User mUser;
    private RecyclerView mRecyclerViewSupervised;
    private Toolbar toolbar;
    private UserPreferences mUserPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getApplicationContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());

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
            initUiSupervisor();
        } else if (mUser.getUserType() == User.userType.LEAKAGE) {
            setContentView(R.layout.activity_main);
            Log.d(DEBUG_TAG, "OnCreate Técnico de fugas");
            initUiLeakage();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSupervisorAdapter = new SupervisorAdapter(this, mUserList);

        mRecyclerViewSupervised = (RecyclerView) findViewById(R.id.activity_supervisor_recycler_view);
        mRecyclerViewSupervised.setHasFixedSize(true);
        mRecyclerViewSupervised.setItemViewCacheSize(20);
        mRecyclerViewSupervised.setDrawingCacheEnabled(true);
        mRecyclerViewSupervised.setLayoutManager(layoutManager);
        mRecyclerViewSupervised.setAdapter(mSupervisorAdapter);

        for (int x = 0; x < 15; x++) {
            mUser = new User();
            mUser.setUserId(x);
            mUser.setUserName("Juan");
            mUser.setUserLastname("López");
            mUser.setUserEmail("jlopez@test.com");
            mUser.setUserRoute("Ruta 4");
            mUser.setUserZone("Cancún");
            mUser.setUserType(User.userType.OPERATOR);
            mUser.setUserstatus(User.userStatus.ACTIVE);
            mUserList.add(mUser);
        }
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
        callIntent.setData(Uri.parse("tel:6241370525"));
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
}
