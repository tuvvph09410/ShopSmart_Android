package com.example.shopsmart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Adapter.CategoryMenuStartAdapter;
import com.example.shopsmart.Dialog.loadingDialog_ProgressBar;
import com.example.shopsmart.Entity.Category;
import com.example.shopsmart.Fragment.fragment_Account;
import com.example.shopsmart.Fragment.fragment_Home;
import com.example.shopsmart.Fragment.fragment_Nav_Accessories;
import com.example.shopsmart.Fragment.fragment_Nav_Applecare;
import com.example.shopsmart.Fragment.fragment_Nav_Watch;
import com.example.shopsmart.Fragment.fragment_Nav_Earphone;
import com.example.shopsmart.Fragment.fragment_Nav_Ipad;
import com.example.shopsmart.Fragment.fragment_Nav_Iphone;
import com.example.shopsmart.Fragment.fragment_Nav_Laptop;
import com.example.shopsmart.Fragment.fragment_Payment;
import com.example.shopsmart.Fragment.fragment_Store;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_STORE = 2;
    private static final int FRAGMENT_PAYMENT = 3;
    private static final int FRAGMENT_ACCOUNT = 4;
    private static final int FRAGMENT_NAV_PHONE = 5;
    private static final int FRAGMENT_NAV_TABLE = 6;
    private static final int FRAGMENT_NAV_LAPTOP = 7;
    private static final int FRAGMENT_NAV_ACCESSORIES = 8;
    private static final int FRAGMENT_NAV_EARPHONE = 9;
    private static final int FRAGMENT_NAV_APPLECARE = 10;
    private static final int FRAGMENT_NAV_WATCH = 11;
   
    private int currentFragment = FRAGMENT_HOME;


    private BottomNavigationView bottomNavigationView;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private CategoryMenuStartAdapter adapterCategoryMenu;
    private List<Category> categoryList;
    private int id = 0;
    private String titleCategory = null;
    private String urlCategory = null;
    private String descriptionCategory = null;
    private ListView lv_menuItem;
    private loadingDialog_ProgressBar dialog_progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.navigation_view);
        this.bottomNavigationView = findViewById(R.id.bottom_navigaiton_view);
        this.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        this.lv_menuItem = findViewById(R.id.lv_menuItem);
        this.categoryList = new ArrayList<>();

        //loading dialog_progressBar
        this.dialog_progressBar = new loadingDialog_ProgressBar(MainActivity.this);

        this.actionToolBar();

        this.adapterCategoryMenu = new CategoryMenuStartAdapter(categoryList, getApplicationContext());

        this.lv_menuItem.setAdapter(this.adapterCategoryMenu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, this.drawerLayout, this.toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.navigationView.setNavigationItemSelectedListener(MainActivity.this);
        if (CheckConnected.haveNetworkConnection(getApplicationContext())) {
            this.dialog_progressBar.startLoading_DialogProgressBar();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog_progressBar.dismissLoading_DialogProgressBar();
                }
            }, 3000);
            this.getDataCategoryJson();
            this.clickItemListView();
        }

        this.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new fragment_Home());

        this.navigationView.setCheckedItem(R.id.nav_home);
        this.bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

    }

    private void actionToolBar() {
        setSupportActionBar(this.toolbar);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this, "Search Expand", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(MainActivity.this, "Search Collapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.app_bar_search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Tìm kiếm sản phẩm");
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    openHomeFragment();
                    bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

                    break;
                case R.id.nav_local_store:
                    openStoreFragment();
                    bottomNavigationView.getMenu().findItem(R.id.nav_local_store).setChecked(true);

                    break;
                case R.id.nav_payment:
                    openPaymentFragment();
                    bottomNavigationView.getMenu().findItem(R.id.nav_payment).setChecked(true);

                    break;
                case R.id.nav_account:
                    openAccountFragment();
                    bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);

                    break;
            }
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    private void getDataCategoryJson() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.getUrlGetCategory(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("idCategory");
                            titleCategory = jsonObject.getString("title");
                            urlCategory = jsonObject.getString("urlImage");
                            descriptionCategory = jsonObject.getString("description");
                            Category category = new Category(id, titleCategory, urlCategory, descriptionCategory);
                            categoryList.add(category);
                            adapterCategoryMenu.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    categoryList.add(0, new Category(0, "Trang chủ", "https://img.icons8.com/ios/50/000000/home-page.png", "Sale sập sàn"));
                    categoryList.add(8, new Category(9, "Thông tin", "https://img.icons8.com/ios/50/000000/device-information.png", "Vũ Văn Tú - PH09410"));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnected.ShowToastLong(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void clickItemListView() {
        this.lv_menuItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {


                    for (int i = 0; i <= lv_menuItem.getChildCount(); i++) {
                        if (position == i) {
                            lv_menuItem.getChildAt(i).setBackgroundColor(Color.parseColor("#ecc8ff"));
                        } else {
                            lv_menuItem.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (position) {
                    case 0:
                        openHomeFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        openPhoneFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        navigationView.setCheckedItem(position);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        openIpadFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        openLaptopFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        openAccessoriesFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        openEarphoneFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 6:
                        openApplecareFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 7:
                        openWatchFragment();
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    private void openHomeFragment() {
        if (this.currentFragment != this.FRAGMENT_HOME) {
            this.loadFragment(new fragment_Home());
            this.currentFragment = this.FRAGMENT_HOME;

        }
    }

    private void openStoreFragment() {
        if (this.currentFragment != this.FRAGMENT_STORE) {
            this.loadFragment(new fragment_Store());
            this.currentFragment = this.FRAGMENT_STORE;
        }
    }

    private void openPaymentFragment() {
        if (this.currentFragment != this.FRAGMENT_PAYMENT) {
            this.loadFragment(new fragment_Payment());
            this.currentFragment = this.FRAGMENT_PAYMENT;
        }
    }

    private void openAccountFragment() {
        if (this.currentFragment != this.FRAGMENT_ACCOUNT) {
            this.loadFragment(new fragment_Account());
            this.currentFragment = this.FRAGMENT_ACCOUNT;
        }
    }

    private void openPhoneFragment() {
        if (currentFragment != FRAGMENT_NAV_PHONE) {
            fragment_Nav_Iphone fragmentIphone = new fragment_Nav_Iphone();
            Bundle bundleIphone = new Bundle();
            bundleIphone.putInt("positionPhone", 1);
            fragmentIphone.setArguments(bundleIphone);
            loadFragment(fragmentIphone);
            currentFragment = FRAGMENT_NAV_PHONE;
        }
    }

    private void openIpadFragment() {
        if (currentFragment != FRAGMENT_NAV_TABLE) {
            fragment_Nav_Ipad fragmentNavIpad = new fragment_Nav_Ipad();
            Bundle bundleIpad = new Bundle();
            bundleIpad.putInt("positionIpad", 2);
            fragmentNavIpad.setArguments(bundleIpad);
            loadFragment(fragmentNavIpad);
            currentFragment = FRAGMENT_NAV_TABLE;
        }
    }

    private void openLaptopFragment() {
        if (currentFragment != FRAGMENT_NAV_LAPTOP) {
            fragment_Nav_Laptop fragmentNavLaptop = new fragment_Nav_Laptop();
            Bundle bundleLaptop = new Bundle();
            bundleLaptop.putInt("positionLaptop", 3);
            fragmentNavLaptop.setArguments(bundleLaptop);
            loadFragment(fragmentNavLaptop);
            currentFragment = FRAGMENT_NAV_LAPTOP;
        }

    }

    private void openAccessoriesFragment() {
        if (currentFragment != FRAGMENT_NAV_ACCESSORIES) {
            fragment_Nav_Accessories fragmentNavAccessories = new fragment_Nav_Accessories();
            Bundle bundleAccessories = new Bundle();
            bundleAccessories.putInt("positionAccessories", 4);
            fragmentNavAccessories.setArguments(bundleAccessories);

            loadFragment(fragmentNavAccessories);
            currentFragment = FRAGMENT_NAV_ACCESSORIES;
        }
    }

    private void openEarphoneFragment() {
        if (currentFragment != FRAGMENT_NAV_EARPHONE) {
            fragment_Nav_Earphone fragmentNavEarphone = new fragment_Nav_Earphone();
            Bundle bundleEarphone = new Bundle();
            bundleEarphone.putInt("positionEarphone", 5);
            fragmentNavEarphone.setArguments(bundleEarphone);
            loadFragment(fragmentNavEarphone);
            currentFragment = FRAGMENT_NAV_EARPHONE;
        }
    }

    private void openApplecareFragment() {
        if (currentFragment != FRAGMENT_NAV_APPLECARE) {
            fragment_Nav_Applecare fragmentNavApplecare = new fragment_Nav_Applecare();
            Bundle bundleApplecare = new Bundle();
            bundleApplecare.putInt("positionApplecare", 8);
            fragmentNavApplecare.setArguments(bundleApplecare);
            loadFragment(fragmentNavApplecare);
            currentFragment = FRAGMENT_NAV_APPLECARE;
        }
    }

    private void openWatchFragment() {
        if (currentFragment != FRAGMENT_NAV_WATCH) {
            fragment_Nav_Watch fragmentNavWatch = new fragment_Nav_Watch();
            Bundle bundleWatch = new Bundle();
            bundleWatch.putInt("positionWatch", 9);
            fragmentNavWatch.setArguments(bundleWatch);
            loadFragment(fragmentNavWatch);
            currentFragment = FRAGMENT_NAV_WATCH;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}