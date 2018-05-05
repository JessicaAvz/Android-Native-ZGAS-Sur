package com.zgas.tesselar.myzuite.View.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.zgas.tesselar.myzuite.Controller.Adapter.PagerAdapter;
import com.zgas.tesselar.myzuite.Controller.Adapter.SupervisorAdapter;
import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Service.RefreshTokenTask;
import com.zgas.tesselar.myzuite.Utilities.CustomViewPager;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.HelpFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.MainFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.ExtraOrderFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.HelpFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.MainFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserService.HelpFragmentService;
import com.zgas.tesselar.myzuite.View.Fragment.UserService.MainFragmentService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that manages all the main fragments to be used by the application, it also separates them
 * according to the userType of the user that's logged in.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see RefreshTokenTask
 * @see GetUserInfoTask
 * @see MainFragmentLeak
 * @see HelpFragmentLeak
 * @see MainFragmentOperator
 * @see HelpFragmentOperator
 * @see ExtraOrderFragmentOperator
 * @see MainFragmentService
 * @see HelpFragmentService
 * @see CustomViewPager
 * @see PagerAdapter
 * @see SupervisorAdapter
 * @see RecyclerView
 * @see LinearLayoutManager
 * @see AHBottomNavigation
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetUserInfoTask.UserInfoListener,
        RefreshTokenTask.RefreshTokenListener {

    private static final String DEBUG_TAG = "MainActivity";

    private AHBottomNavigation ahBottomNavigation;
    private CustomViewPager customViewPager;
    private FloatingActionButton mFabCall;
    private FloatingActionButton mFabCallSupervisor;
    private HelpFragmentOperator mHelpFragmentOperator;
    private MainFragmentOperator mMainFragmentOperator;
    private ExtraOrderFragmentOperator mExtraOrderFragmentOperator;
    private MainFragmentService mMainFragmentService;
    private HelpFragmentService mHelpFragmentService;
    private HelpFragmentLeak mHelpFragmentLeak;
    private MainFragmentLeak mMainFragmentLeak;
    private PagerAdapter mPagerAdapter;
    private SupervisorAdapter mSupervisorAdapter;

    private RecyclerView mRecyclerViewSupervised;
    private Toolbar toolbar;
    private UserPreferences userPreferences;
    private LinearLayoutManager linearLayoutManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));

        userPreferences = new UserPreferences(this);
        user = userPreferences.getUserObject();

        /*If a user is logged in, it will check it's type so the app will show the correct
        user interface.*/
        if (userPreferences.isLoggedIn()) {
            if (user.getUserType() == User.userType.OPERATOR) {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiOperator();
            } else if (user.getUserType() == User.userType.SERVICE) {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiService();
            } else if (user.getUserType() == User.userType.SUPERVISOR) {
                setContentView(R.layout.activity_supervisor);
                ButterKnife.bind(this);
                getSupervisedCallAsyncTask();
                initUiSupervisor();
            } else if (user.getUserType() == User.userType.LEAKAGE) {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiLeakage();
            } else {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiOperator();
            }
        } else {

            /*try {
                JSONObject params = new JSONObject();
                params.put(EMAIL_TAG, UrlHelper.ADMIN_EMAIL);
                params.put(PASS_TAG, UrlHelper.ADMIN_PASS);

                RefreshTokenTask refreshTokenTask = new RefreshTokenTask(this, params);
                refreshTokenTask.setRefreshTokenListener(this);
                refreshTokenTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_fab_call:
                callIntent();
                break;
            case R.id.ativity_supervisor_fab_call:
                callIntent();
                break;
        }
    }

    private void initUiOperator() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment);

        customViewPager = findViewById(R.id.activity_main_cv_view_pager);
        customViewPager.setPagingEnabled(false);
        mFabCall = findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mHelpFragmentOperator = new HelpFragmentOperator();
        mMainFragmentOperator = new MainFragmentOperator();
        mExtraOrderFragmentOperator = new ExtraOrderFragmentOperator();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mExtraOrderFragmentOperator);
        mPagerAdapter.addFragment(mMainFragmentOperator);
        mPagerAdapter.addFragment(mHelpFragmentOperator);
        customViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationOperator();
    }

    private void initUiService() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment_service);

        customViewPager = findViewById(R.id.activity_main_cv_view_pager);
        customViewPager.setPagingEnabled(false);
        mFabCall = findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mMainFragmentService = new MainFragmentService();
        mHelpFragmentService = new HelpFragmentService();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mMainFragmentService);
        mPagerAdapter.addFragment(mHelpFragmentService);
        customViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationService();
    }

    private void initUiSupervisor() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_supervised);
        mFabCallSupervisor = findViewById(R.id.ativity_supervisor_fab_call);
        mFabCallSupervisor.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSupervised = findViewById(R.id.activity_supervisor_recycler_view);
    }

    private void initUiLeakage() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_order_leak);

        customViewPager = findViewById(R.id.activity_main_cv_view_pager);
        customViewPager.setPagingEnabled(false);
        mFabCall = findViewById(R.id.activity_main_fab_call);
        mFabCall.setOnClickListener(this);

        mMainFragmentLeak = new MainFragmentLeak();
        mHelpFragmentLeak = new HelpFragmentLeak();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mMainFragmentLeak);
        mPagerAdapter.addFragment(mHelpFragmentLeak);
        customViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationLeakage();
    }

    private void initBottomNavigationOperator() {
        ahBottomNavigation = findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_fragment), R.drawable.icon_check, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_main_fragment), R.drawable.icon_gas_cylinder_menu, R.color.pink_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);

        ahBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        ahBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        ahBottomNavigation.setCurrentItem(1);
        customViewPager.setCurrentItem(1);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
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

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    customViewPager.setCurrentItem(0);
                } else if (position == 1) {
                    customViewPager.setCurrentItem(1);
                } else if (position == 2) {
                    customViewPager.setCurrentItem(2);
                }
                return true;
            }
        });
        ahBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void initBottomNavigationService() {
        ahBottomNavigation = findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_main_fragment_service), R.drawable.icon_gas_cylinder_menu, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);

        ahBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        ahBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setCurrentItem(0);
        customViewPager.setCurrentItem(0);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
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

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    customViewPager.setCurrentItem(0);
                } else if (position == 1) {
                    customViewPager.setCurrentItem(1);
                }
                return true;
            }
        });
        ahBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void initBottomNavigationLeakage() {
        ahBottomNavigation = findViewById(R.id.activity_main_cv_bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_leak), R.drawable.icon_truck_fast, R.color.pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), R.drawable.icon_help, R.color.pink_500);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);

        ahBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setAccentColor(getResources().getColor(R.color.pink_500));
        ahBottomNavigation.setInactiveColor(getResources().getColor(R.color.pink_50));
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setCurrentItem(0);
        customViewPager.setCurrentItem(0);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
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

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    customViewPager.setCurrentItem(0);
                } else if (position == 1) {
                    customViewPager.setCurrentItem(1);
                }
                return true;
            }
        });
        ahBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void animateFab(int position) {
        switch (position) {
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

    private void animateFabOperator(int position) {
        switch (position) {
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
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ExtrasHelper.CALL));
        startActivity(intent);
    }

    private void getSupervisedAsyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(ExtrasHelper.EMAIL_TAG, userPreferences.getLoginObject().getLoginEmail());
            params.put(ExtrasHelper.ADMIN_TOKEN, userPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + params.getString(ExtrasHelper.EMAIL_TAG) + " " + params.getString(ExtrasHelper.ADMIN_TOKEN));
            GetUserInfoTask getUserInfoTask = new GetUserInfoTask(this, params);
            getUserInfoTask.setUserInfoListener(this);
            getUserInfoTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSupervisedCallAsyncTask() {
        try {
            getSupervisedAsyncTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                userPreferences.logoutUser();
                this.finish();
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
        Log.d(DEBUG_TAG, "User preference id: " + userPreferences.getUserObject().getUserId());
        Log.d(DEBUG_TAG, "User preference name: " + userPreferences.getUserObject().getUserName());
        Log.d(DEBUG_TAG, "User preference type: " + userPreferences.getUserObject().getUserType());
        Log.d(DEBUG_TAG, "User preference email: " + userPreferences.getUserObject().getUserEmail());
        Log.d(DEBUG_TAG, "User preference zone: " + userPreferences.getUserObject().getUserZone());
        Log.d(DEBUG_TAG, "User preference route: " + userPreferences.getUserObject().getUserRoute());
        Log.d(DEBUG_TAG, "User preference status: " + userPreferences.getUserObject().getUserStatus());
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

    @Override
    public void refreshErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void refreshSuccessResponse(Login login) {
        userPreferences.setLoginData(login);
    }
}
