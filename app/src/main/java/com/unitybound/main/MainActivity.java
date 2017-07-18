package com.unitybound.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.unitybound.R;
import com.unitybound.account.fragment.MyAccountFragment;
import com.unitybound.church.fragment.ChurchesFragment;
import com.unitybound.events.fragment.fragment.EventsFragment;
import com.unitybound.groups.fragment.GroupsFragment;
import com.unitybound.main.adapter.SliderCustomAdapter;
import com.unitybound.main.friendrequest.fragment.FriendRequestFragment;
import com.unitybound.main.home.fragment.HomeFeedsFragment;
import com.unitybound.main.model.SideMenu;
import com.unitybound.main.my.prayer.request.fragment.MyPrayerRequestFragment;
import com.unitybound.notification.fragment.NotificationsFragment;
import com.unitybound.obtiuaries.fragment.ObtiuariesFragment;
import com.unitybound.prefrence.fragment.PrefrenceFragment;
import com.unitybound.settings.fragment.SettingsFragment;
import com.unitybound.utility.AppSession;
import com.unitybound.utility.Util;
import com.unitybound.weddings.fragment.WeddingsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    //SocialIntegrations mSocialIntegrations = null;
    AppSession mAppSession;
    Handler mHandler;
    SliderCustomAdapter sliderCustomAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.lst_menu_items)
    ListView lstMenuItems;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.img_close_menu)
    ImageView imgCloseMenu;
    @BindView(R.id.view_container)
    RelativeLayout viewContainer;
    private ArrayList<SideMenu> menuArrayList;
    public static int sNavItemIndex = 100;
    public final int HOME_INDEX = 100;
    public final int NOTIFICATION_INDEX = 200;
    public final int FRIEND_REQUEST_INDEX = 300;
    public final int LIST_INDEX = 400;
    // tags used to attach the fragments
    private static final String TAG_SEARCH_QUEUE = "searchqueue";
    private static final String TAG_VIEW_QUEUE = "viewqueue";
    private static final String TAG_FAVORITE = "favorite";
    private static final String TAG_NOTIFICATION = "notification";
    private static final String TAG_SETTINGS = "fragment_settings";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_SEARCH_QUEUE;
    // mToolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Menu mOptionMenu = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("FragmentPosition", sNavItemIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sNavItemIndex = savedInstanceState.getInt("FragmentPosition");
        // Load Default Fragment
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            loadHomeScreenFragments(sNavItemIndex);
            if (mOptionMenu != null)
                mOptionMenu.findItem(R.id.action_home).setIcon(R.drawable.ac_home_active);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initClass();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbarData();
        initAdapter();
        setUpNavigationView();
// Load Default Fragment
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            loadHomeScreenFragments(sNavItemIndex);
        }
        lstMenuItems.setOnItemClickListener(navBarListListner);
    }

    private void initAdapter() {
        sliderCustomAdapter = new SliderCustomAdapter(this, generateSideMenu());
        lstMenuItems.setAdapter(sliderCustomAdapter);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void initToolbarData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.iv_menu);
    }

    private void initClass() {
        //  mSocialIntegrations = SocialIntegrations.getInstance(MainActivity.this);
        mAppSession = new AppSession(MainActivity.this);
        mHandler = new Handler();
    }

    /**
     * @param mContext the m context
     */
    public static void startMainActivity(Context mContext) {
        Intent i = new Intent(mContext, MainActivity.class);
        mContext.startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public ArrayList<SideMenu> generateSideMenu() {
        menuArrayList = new ArrayList<>();
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_church), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_event), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_grp), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_obituaries), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_wedd), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_acc), R.mipmap.ic_launcher, false));

        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_notify), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_pref), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_settings), R.mipmap.ic_launcher, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_logout), R.mipmap.ic_launcher, false));
        return menuArrayList;
    }

    private void setUpNavigationView() {

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the mDrawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                try {
                    Util.hideSoftKeyboard(MainActivity.this);
                } catch (Exception xc) {
                    xc.printStackTrace();
                }
                // Code here will be triggered once the mDrawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                /*InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
            }
        };
        //Setting the actionbarToggle to mDrawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mDrawerLayout.closeDrawer(containerView);
//                mDrawerLayout.isDrawerOpen(view);
                drawerLayout.openDrawer(GravityCompat.START);
                Util.hideSoftKeyboard(MainActivity.this);
            }
        });
        // Calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setToolbarTitle() {
//        toolbarTitle.setText(activityTitles[sNavItemIndex]);
    }

    private AdapterView.OnItemClickListener navBarListListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            menuSelection(position);
            switch (position) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                case 0:
                    sNavItemIndex = 0;
                    CURRENT_TAG = TAG_SEARCH_QUEUE;
                    loadFragment();
                    break;
                case 1:
                    sNavItemIndex = 1;
                    CURRENT_TAG = TAG_VIEW_QUEUE;
                    loadFragment();
                    break;
                case 2:
                    sNavItemIndex = 2;
                    CURRENT_TAG = TAG_FAVORITE;
                    loadFragment();
                    break;
                case 3:
                    sNavItemIndex = 3;
                    CURRENT_TAG = TAG_NOTIFICATION;
                    loadFragment();
                    break;
                case 4:
                    sNavItemIndex = 4;
                    CURRENT_TAG = TAG_SETTINGS;
                    loadFragment();
                    break;
                case 5:
                    sNavItemIndex = 5;
                    CURRENT_TAG = TAG_LOGOUT;
                    drawerLayout.closeDrawers();
                    break;
                default:
                    sNavItemIndex = 0;
                    loadFragment();
            }
        }
    };

    public void menuSelection(int position) {
        for (int i = 0; i < menuArrayList.size(); i++) {
            if (i == position) {
                menuArrayList.get(i).setSelected(true);

            } else {
                menuArrayList.get(i).setSelected(false);
            }
        }
        sliderCustomAdapter.notifyDataSetChanged();
        lstMenuItems.setAdapter(sliderCustomAdapter);
    }

    @OnClick(R.id.img_close_menu)
    public void onViewClicked() {
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        mOptionMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (sNavItemIndex == HOME_INDEX) {
            menu.findItem(R.id.action_home).setIcon(R.drawable.ac_home_active);
        } else if (sNavItemIndex == NOTIFICATION_INDEX) {
            menu.findItem(R.id.action_notification).setIcon(R.drawable.ac_notifications_icon_active);
        } else if (sNavItemIndex == FRIEND_REQUEST_INDEX) {
            menu.findItem(R.id.action_friend_request).setIcon(R.drawable.ac_friend_reqst_active);
        } else if (sNavItemIndex == LIST_INDEX) {
            menu.findItem(R.id.action_friend_list).setIcon(R.drawable.ac_prayerlist_active);
        } else {

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_home:
//                if (Util.checkNetworkAvailablity(this)) {
//                    if (null != mSlotDataListAdapter.getCheckedItem() && mSlotDataListAdapter.getCheckedItem().size() > 0
//                            && null != mSlotDataListAdapter.getData() && mSlotDataListAdapter.getData().size() > 0) {

//                mOptionMenu.findItem(R.id.action_home).setIcon(R.drawable.ac_home_active);
                loadHomeScreenFragments(HOME_INDEX);
//                } else {
//                    Util.showAlertDialog(this, getResources().getString(R.string.alt_checknet));
//                }
                return true;
            case R.id.action_notification:
//                if (Util.checkNetworkAvailablity(this)) {
//                mOptionMenu.findItem(R.id.action_notification).setIcon(R.drawable.ac_notifications_icon_active);
                loadHomeScreenFragments(NOTIFICATION_INDEX);

//                } else {
//                    Util.showAlertDialog(this, getResources().getString(R.string.alt_checknet));
//                }
                return true;
            case R.id.action_friend_request:
//                if (Util.checkNetworkAvailablity(this)) {

//                mOptionMenu.findItem(R.id.action_friend_request).setIcon(R.drawable.ac_friend_reqst_active);
                loadHomeScreenFragments(FRIEND_REQUEST_INDEX);
//                } else {
//                    Util.showAlertDialog(this, getResources().getString(R.string.alt_checknet));
//                }
                return true;
            case R.id.action_friend_list:

//                mOptionMenu.findItem(R.id.action_friend_list).setIcon(R.drawable.ac_prayerlist_active);
                loadHomeScreenFragments(LIST_INDEX);
                return true;


            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    /**
     * Top menu items click events Fragment commited on framelayout
     *
     * @param POSITION
     */
    private void loadHomeScreenFragments(int POSITION) {
        Fragment fragment = null;

        switch (POSITION) {
            case HOME_INDEX:
                sNavItemIndex = POSITION;
                fragment = new HomeFeedsFragment();
                break;
            case NOTIFICATION_INDEX:
                sNavItemIndex = POSITION;
                fragment = new NotificationsFragment();
                break;
            case FRIEND_REQUEST_INDEX:
                sNavItemIndex = POSITION;
                fragment = new FriendRequestFragment();
                break;
            case LIST_INDEX:
                sNavItemIndex = POSITION;
                fragment = new MyPrayerRequestFragment();
                break;
        }

        final Fragment finalFragment = fragment;
        if (fragment != null) {
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    removeAllFragmentStack();
                    // update the main content by replacing fragments
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, finalFragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        }
    }

    private Fragment navigateOnFragment() {
        switch (sNavItemIndex) {
            case 0:
                // Churches Fragment
//                return new ChurchesFragment();
                return new ChurchesFragment();
            case 1:
                // View Queue fragment
                return new EventsFragment();
            case 2:
                // View Queue fragment
                return new GroupsFragment();
            case 3:
                return new ObtiuariesFragment();
            case 4:
                return new WeddingsFragment();
            case 5:
                return new MyAccountFragment();
            case 6:
                return new NotificationsFragment();
            case 7:
                return new PrefrenceFragment();
            case 8:
                return new SettingsFragment();
            // new ForgotPasswordDialog(MainActivity.this, getResources().getString(R.string.logout), getString(R.string.str_logout_sure), MainActivity.this, Util.DIALOG_LOGOUT_ID).show();
            default:
                return new ChurchesFragment();
        }
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragment() {
        // selecting appropriate nav menu item
        // set mToolbar title
        setToolbarTitle();

       /* // if user select the current navigation menu again, don't do anything
        // just close the navigation mDrawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawer.closeDrawers();

            return;
        }*/

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                removeAllFragmentStack();

                Fragment fragment = navigateOnFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        if (sNavItemIndex != 4) {
            //Closing mDrawer on item click
            drawerLayout.closeDrawers();
        }
        // refresh mToolbar menu
        invalidateOptionsMenu();
    }

    private void removeAllFragmentStack() {
        // Remove All fragment from stack
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        sNavItemIndex = 0;
        removeAllFragmentStack();
        super.onDestroy();
    }
}
