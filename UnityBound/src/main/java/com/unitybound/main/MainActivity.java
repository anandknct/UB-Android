package com.unitybound.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unitybound.R;
import com.unitybound.account.fragment.MyAccountFragment;
import com.unitybound.account.listner.NavigationFromProfileListener;
import com.unitybound.church.fragment.ChurchesFragment;
import com.unitybound.events.fragment.fragment.EventsFragment;
import com.unitybound.groups.fragment.GroupsFragment;
import com.unitybound.main.adapter.SliderCustomAdapter;
import com.unitybound.main.friendrequest.fragment.FriendRequestFragment;
import com.unitybound.main.home.fragment.HomeFeedsFragment;
import com.unitybound.main.interPhase.IUserClickFromFragmentListener;
import com.unitybound.main.interPhase.iNotificationUpdate;
import com.unitybound.main.model.SideMenu;
import com.unitybound.main.my.prayer.request.fragment.MyPrayerRequestFragment;
import com.unitybound.main.search.activity.SearchActivity;
import com.unitybound.notification.fragment.NotificationsFragment;
import com.unitybound.obtiuaries.fragment.ObituariesFragment;
import com.unitybound.settings.fragment.SettingsFragment;
import com.unitybound.utility.AppSession;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.weddings.fragment.WeddingsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        IUserClickFromFragmentListener, iNotificationUpdate, CustomDialog.IDialogListener,
        NavigationFromProfileListener {

    //Social Integrations mSocialIntegrations = null;
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
    @BindView(R.id.tv_name)
    TextView SideBarUserName;
    private ArrayList<SideMenu> menuArrayList;
    public static int sNavItemIndex = 100;
    public final int HOME_INDEX = 100;
    public final int NOTIFICATION_INDEX = 200;
    public final int FRIEND_REQUEST_INDEX = 300;
    public final int LIST_INDEX = 400;
    public final int FRIEND_SEARCH_INDEX = 500;
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
    private ImageView mHredCircle = null;
    private TextView tv_home_count = null;
    private TextView tv_noti_count = null;
    private ImageView mNredCircle = null;
    private ImageView mFredCircle = null;
    private TextView tv_frnd_req_count = null;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private String mSelectedUserProfileId = null;


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
            sNavItemIndex = 100;
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
//        initUserImage();
        setUpNavigationView();
// Load Default Fragment
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            sNavItemIndex = HOME_INDEX;
            loadHomeScreenFragments(sNavItemIndex);
        }
        lstMenuItems.setOnItemClickListener(navBarListListner);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            if (bundle.containsKey("PROFILE_ID")) {
                sNavItemIndex = 7;
                mSelectedUserProfileId = bundle.getString("PROFILE_ID");
                CURRENT_TAG = TAG_LOGOUT;
                bundle.putString("SelectedUserProfileId", mSelectedUserProfileId);
                loadFragment(bundle);
            } else if (bundle.containsKey("POSITION")) {
                sNavItemIndex = bundle.getInt("POSITION");;
                loadFragment(null);
            }
        }

        SideBarUserName.setText(Util.isNull(Util.loadPrefrence(Util.PREF_FIRST_NAME, "", this)) + " " +  Util.isNull(Util.loadPrefrence(Util.PREF_LAST_NAME, "", this)));}

    private void initUserImage() {
        String url = Util.isNull(Util.loadPrefrence(Util.PREF_USER_IMAGE, "", this));
        if (url.length() > 0)
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ubitauries_def_image)
                    .skipMemoryCache(false)
                    .into(((ImageView) findViewById(R.id.menu_icon)));
    }

    private void initAdapter() {
        sliderCustomAdapter = new SliderCustomAdapter(this, generateSideMenu());
        lstMenuItems.setAdapter(sliderCustomAdapter);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
    }

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

    public ArrayList<SideMenu> generateSideMenu() {
        menuArrayList = new ArrayList<>();
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_home), R.drawable.ic_menu_home_icon_gray, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_church), R.drawable.ac_churches_icon_gray, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_event), R.drawable.ic_menu_events_icon, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_grp), R.drawable.ic_menu_groupsicon, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_obituaries), R.drawable.ic_menu_obituaries_icon, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_wedd), R.drawable.ic_menu_weddings, false));
//        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_acc), R.drawable.ic_menu_my_account_icon, false));

//        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_notify), R.drawable.ac_notifications_icon_gray, false));
//        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_pref), R.drawable.ic_menu_preferences_icon, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_settings), R.drawable.ic_menu_settings_icon, false));
        menuArrayList.add(new SideMenu(getResources().getString(R.string.str_logout), R.drawable.ic_menu_logout_icon, false));
        return menuArrayList;
    }

    private void setUpNavigationView() {

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the mDrawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    sNavItemIndex = HOME_INDEX;
                    loadHomeScreenFragments(HOME_INDEX);
                    switchToolbarIcons(HOME_INDEX);
                    if (drawerLayout != null) {
                        //Closing mDrawer on item click
                        drawerLayout.closeDrawers();
                    }
                    // refresh mToolbar menu
                    invalidateOptionsMenu();
                    break;
                case 1:
                    sNavItemIndex = 1;
                    CURRENT_TAG = TAG_SEARCH_QUEUE;
                    loadFragment(null);
                    break;
                case 2:
                    sNavItemIndex = 2;
                    CURRENT_TAG = TAG_VIEW_QUEUE;
                    loadFragment(null);
                    break;
                case 3:
                    sNavItemIndex = 3;
                    CURRENT_TAG = TAG_FAVORITE;
                    loadFragment(null);
                    break;
                case 4:
                    sNavItemIndex = 4;
                    CURRENT_TAG = TAG_NOTIFICATION;
                    loadFragment(null);
                    break;
                case 5:
                    sNavItemIndex = 5;
                    CURRENT_TAG = TAG_SETTINGS;
                    loadFragment(null);
                    break;
                case 6:
                    sNavItemIndex = 6;
                    CURRENT_TAG = TAG_LOGOUT;
                    loadFragment(null);
                    break;
//                case 7:
//                    sNavItemIndex = 7;
//                    CURRENT_TAG = TAG_LOGOUT;
//                    loadFragment(null);
//                    break;
//                case 8:
//                    sNavItemIndex = 7;
//                    CURRENT_TAG = TAG_LOGOUT;
//                    loadFragment(null);
//                    break;
//                case 9:
//                    sNavItemIndex = 8;
//                    CURRENT_TAG = TAG_LOGOUT;
//                    loadFragment(null);
//                    break;
                case 7:
//                    sNavItemIndex = 9;
                    //Closing drawer on item click
                    drawerLayout.closeDrawers();
                    if (!Util.haveNetworkConnection(MainActivity.this)) {
                        CustomDialog customDialog1 = new CustomDialog(MainActivity.this, MainActivity.this,
                                "", getResources().getString(R.string.alt_checknet),
                                "Logout");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(MainActivity.this, MainActivity.this,
                                "Logout Alert", "Are you sure you want to logout from application ?",
                                "logout");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                    break;
                default:
                    sNavItemIndex = 0;
                    loadFragment(null);
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

    @OnClick(R.id.view_container)
    public void onViewClicked() {
        drawerLayout.closeDrawers();
        sNavItemIndex = 7;
        CURRENT_TAG = TAG_LOGOUT;
        loadFragment(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        mOptionMenu = menu;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // For home Icon & text count
        final MenuItem actionHomeMenuItem = menu.findItem(R.id.action_home);
        FrameLayout rootHView = (FrameLayout) actionHomeMenuItem.getActionView();
        mHredCircle = rootHView.findViewById(R.id.iv_home_count);
        tv_home_count = rootHView.findViewById(R.id.tv_home_count);
        rootHView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(actionHomeMenuItem);
            }
        });

        // For notification Icon & text count
        final MenuItem actionNotificationMenuItem = menu.findItem(R.id.action_notification);
        FrameLayout rootNView = (FrameLayout) actionNotificationMenuItem.getActionView();
        mNredCircle = rootNView.findViewById(R.id.iv_noti_count);
        tv_noti_count = rootNView.findViewById(R.id.tv_noti_count);
        rootNView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(actionNotificationMenuItem);
            }
        });

        // For notification Icon & text count
        final MenuItem actionFriendRequestMenuItem = menu.findItem(R.id.action_friend_request);
        FrameLayout rootFView = (FrameLayout) actionFriendRequestMenuItem.getActionView();
        mFredCircle = rootFView.findViewById(R.id.iv_frnd_req_count);
        tv_frnd_req_count = rootFView.findViewById(R.id.tv_frnd_req_count);
        rootFView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(actionFriendRequestMenuItem);
            }
        });

//        // For notification Icon & text count
//        final MenuItem actionSearchMenuItem = menu.findItem(R.id.search);
//        FrameLayout rootSView = (FrameLayout) actionSearchMenuItem.getActionView();
//        rootSView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(actionSearchMenuItem);
//            }
//        });


        if (sNavItemIndex == HOME_INDEX) {
            switchToolbarIcons(HOME_INDEX);
            menu.findItem(R.id.action_home).setIcon(R.drawable.ac_home_active);
        } else if (sNavItemIndex == NOTIFICATION_INDEX) {
            switchToolbarIcons(NOTIFICATION_INDEX);
            menu.findItem(R.id.action_notification).setIcon(R.drawable.ac_notifications_icon_active);
        } else if (sNavItemIndex == FRIEND_REQUEST_INDEX) {
            switchToolbarIcons(FRIEND_REQUEST_INDEX);
            menu.findItem(R.id.action_friend_request).setIcon(R.drawable.ac_friend_reqst_active);
        } else if (sNavItemIndex == LIST_INDEX) {
            menu.findItem(R.id.action_friend_list).setIcon(R.drawable.ac_prayerlist_active);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_home:

                loadHomeScreenFragments(HOME_INDEX);
                return true;
            case R.id.action_notification:

                loadHomeScreenFragments(NOTIFICATION_INDEX);
                return true;
            case R.id.action_friend_request:

                loadHomeScreenFragments(FRIEND_REQUEST_INDEX);
                return true;
            case R.id.action_friend_list:

                loadHomeScreenFragments(LIST_INDEX);
                return true;
            case R.id.search:
//                loadHomeScreenFragments(FRIEND_SEARCH_INDEX);
//                Toast.makeText(this, "Search...In Progress", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
            case FRIEND_SEARCH_INDEX:
//                sNavItemIndex = POSITION;
//                fragment = new MyPrayerRequestFragment();
                Toast.makeText(this, getString(R.string.str_under_progress), Toast.LENGTH_SHORT).show();
                break;
        }

        final Fragment finalFragment = fragment;
        if (fragment != null) {
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
//                    removeAllFragmentStack();
                    // update the main content by replacing fragments
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.frame, finalFragment, CURRENT_TAG);
                    fragmentTransaction.commit();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        }
    }

    private Fragment navigateOnFragment(Bundle data) {
        switch (sNavItemIndex) {
            case 1:
                // Churches Fragment
//                return new ChurchesFragment();
                ChurchesFragment churchesFragment = new ChurchesFragment();
                if (data != null) {
                    churchesFragment.setArguments(data);
                }
                return churchesFragment;
            case 2:
                // View Queue fragment
                EventsFragment eventsFragment = new EventsFragment();
                if (data != null) {
                    eventsFragment.setArguments(data);
                }
                return eventsFragment;
            case 3:
                // View Queue fragment
                GroupsFragment groupsFragment = new GroupsFragment();
                if (data != null) {
                    groupsFragment.setArguments(data);
                }
                return groupsFragment;
            case 4:
                ObituariesFragment obituariesFragment = new ObituariesFragment();
                if (data != null) {
                    obituariesFragment.setArguments(data);
                }
                return obituariesFragment;
            case 5:
                WeddingsFragment weddingsFragment = new WeddingsFragment();
                if (data != null) {
                    weddingsFragment.setArguments(data);
                }
                return weddingsFragment;
            case 7:
                MyAccountFragment myAccountFragment = new MyAccountFragment();
                if (data != null) {
                    myAccountFragment.setArguments(data);
                }
                return myAccountFragment;
//            case 6:
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                if (data!=null){
//                    notificationsFragment.setArguments(data);
//                }
//                return notificationsFragment;
//            case 7:
//                PrefrenceFragment prefrenceFragment = new PrefrenceFragment();
//                if (data!=null){
//                    prefrenceFragment.setArguments(data);
//                }
//                return prefrenceFragment;
            case 6:
                SettingsFragment settingsFragment = new SettingsFragment();
                if (data != null) {
                    settingsFragment.setArguments(data);
                }
                return settingsFragment;
            // new ForgotPasswordDialog(MainActivity.this, getResources().getString(R.string.logout), getString(R.string.str_logout_sure), MainActivity.this, Util.DIALOG_LOGOUT_ID).show();
            default:
                return new ChurchesFragment();
        }
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     * @param data
     */
    private void loadFragment(Bundle data) {
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
//                removeAllFragmentStack();

                Fragment fragment = navigateOnFragment(data);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        if (drawerLayout != null) {
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                    removeAllFragmentStack();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Press once again to exit!",
                            Toast.LENGTH_SHORT).show();
                }
                back_pressed = System.currentTimeMillis();
            } else {
                removeAllFragmentStack();
                finish();
            }
        }

/*        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        } else {
            getFragmentManager().popBackStack();
        }*/
    }

    @Override
    protected void onStop() {
        sNavItemIndex = 0;
//        removeAllFragmentStack();
        super.onStop();
    }

    @Override
    public void onUserClickListener(String userId, int position) {
        sNavItemIndex = 7;
        mSelectedUserProfileId = userId;
        CURRENT_TAG = TAG_LOGOUT;
        Bundle bundle = new Bundle();
        bundle.putString("SelectedUserProfileId", mSelectedUserProfileId);
        loadFragment(bundle);
    }


    /**
     * To Update All notification count on Toolbar menu
     *
     * @param allCount
     * @param friendReqCount
     */
    @Override
    public void updateAllNotificationCount(int allCount, int friendReqCount) {
        if (allCount != -1) {
            if (allCount > 0) {
                tv_noti_count.setVisibility(View.VISIBLE);
                tv_noti_count.setText(allCount + "");
            } else {
                tv_noti_count.setVisibility(View.GONE);
            }
        }

        if (friendReqCount != -1) {
            if (friendReqCount > 0) {
                tv_frnd_req_count.setVisibility(View.VISIBLE);
                tv_frnd_req_count.setText(allCount + "");
            } else {
                tv_frnd_req_count.setVisibility(View.GONE);
            }
        }
    }

    private void switchToolbarIcons(int ICON_INDEX) {
        switch (ICON_INDEX) {
            case HOME_INDEX:
                if (mOptionMenu != null) {
                    mOptionMenu.findItem(R.id.action_home).setIcon(R.drawable.ac_home_active);
                }
                mHredCircle.setImageResource(R.drawable.ac_home_active);
                break;
            case NOTIFICATION_INDEX:
                if (mOptionMenu != null) {
                    mOptionMenu.findItem(R.id.action_notification).setIcon(R.drawable.notifications_icon_active);
                }
                mNredCircle.setImageResource(R.drawable.ac_notifications_icon_active);
                break;
            case FRIEND_REQUEST_INDEX:
                if (mOptionMenu != null) {
                    mOptionMenu.findItem(R.id.action_friend_request).setIcon(R.drawable.ac_friend_reqst_active);
                }
                mFredCircle.setImageResource(R.drawable.ac_friend_reqst_active);
                break;
            case LIST_INDEX:
                if (mOptionMenu != null)
                    mOptionMenu.findItem(R.id.action_friend_list).setIcon(R.drawable.ac_prayerlist_active);
                break;

        }
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("logout")) {
            Util.unAuthorisedRedirectToLogin(MainActivity.this);
        }
    }


    /**
     * Called from myprofile fragment to navigate user on his desired screen.
     *
     * @param position
     * @param data
     */
    @Override
    public void onNavigateToScreenFromProfile(int position, Bundle data) {
        sNavItemIndex = position;
        loadFragment(data);
//        navigateOnFragment(data);
    }
}
