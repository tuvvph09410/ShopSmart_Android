package com.example.shopsmart.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
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
import com.example.shopsmart.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

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

    private CategoryMenuStartAdapter adapterCategoryMenu;
    private List<Category> categoryList;
    private loadingDialog_ProgressBar dialog_progressBar;
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        // init loading
        this.initLoading();

        //init loading listview drawer
        this.initItemDrawer();

        //init toolbar
        this.initToolbar();

        //init bottom bar;

        this.initBottomBar();

        //init drawer layout;
        this.initDrawerLayout();

        //click view
        this.clickButtonView();

    }

    private void clickButtonView() {
        binding.frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.toolbar.svSearch.setIconified(true);

            }
        });
    }


    private void initToolbar() {
        binding.toolbar.ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        binding.toolbar.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                toolbarBack(false);
            }
        });

        // init search
        this.initSearch();
    }

    private void initSearch() {


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.toolbar.svSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.toolbar.svSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.hasFocus() || binding.toolbar.svSearch.getQuery().length() > 0) {
                    binding.toolbar.tvAppName.setVisibility(View.GONE);
                    binding.toolbar.ibtnNotification.setVisibility(View.GONE);
                } else {
                    binding.toolbar.tvAppName.setVisibility(View.VISIBLE);
                    binding.toolbar.ibtnNotification.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.toolbar.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplication(), "search", Toast.LENGTH_SHORT).show();
                binding.toolbar.tvAppName.setVisibility(View.VISIBLE);
                binding.toolbar.svSearch.setVisibility(View.GONE);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplication(), "search2", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }

    public void toolbarBack(boolean isEnable) {
        binding.toolbar.ibtnBack.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        binding.toolbar.ibtnMenu.setVisibility(isEnable ? View.GONE : View.VISIBLE);
        binding.toolbar.svSearch.setVisibility(isEnable ? View.GONE : View.VISIBLE);
        binding.toolbar.svSearch.setIconified(true);
    }


    private void initItemDrawer() {
        this.categoryList = new ArrayList<>();
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
        }
        this.adapterCategoryMenu = new CategoryMenuStartAdapter(categoryList, getApplicationContext());
        this.binding.lvMenuItem.setAdapter(this.adapterCategoryMenu);

    }

    private void initLoading() {
        this.dialog_progressBar = new loadingDialog_ProgressBar(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        return true;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void initFragment(Fragment fragment) {
        currentFragment = FRAGMENT_HOME;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void openFragment(int currentFragment, Fragment fragment) {
        if (this.currentFragment != currentFragment) {
            Bundle bundle = new Bundle();
            if (currentFragment == FRAGMENT_NAV_PHONE) {
                bundle.putInt("positionPhone", 1);
            }
            if (currentFragment == FRAGMENT_NAV_TABLE) {
                bundle.putInt("positionIpad", 2);
            }
            if (currentFragment == FRAGMENT_NAV_LAPTOP) {
                bundle.putInt("positionLaptop", 3);
            }
            if (currentFragment == FRAGMENT_NAV_ACCESSORIES) {
                bundle.putInt("positionAccessories", 4);
            }
            if (currentFragment == FRAGMENT_NAV_EARPHONE) {
                bundle.putInt("positionEarphone", 5);
            }
            if (currentFragment == FRAGMENT_NAV_APPLECARE) {
                bundle.putInt("positionApplecare", 8);
            }
            if (currentFragment == FRAGMENT_NAV_WATCH) {
                bundle.putInt("positionWatch", 9);
            }
            fragment.setArguments(bundle);
            loadFragment(fragment);
            this.currentFragment = currentFragment;
        }
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
                            int id = jsonObject.getInt("idCategory");
                            String titleCategory = jsonObject.getString("title");
                            String urlCategory = jsonObject.getString("urlImage");
                            String descriptionCategory = jsonObject.getString("description");
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

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                openFragment(FRAGMENT_HOME, new fragment_Home());
                checkIdItemNav(R.id.nav_home);
                break;
            case R.id.nav_local_store:
                openFragment(FRAGMENT_STORE, new fragment_Store());
                checkIdItemNav(R.id.nav_local_store);
                break;
            case R.id.nav_payment:
                openFragment(FRAGMENT_PAYMENT, new fragment_Payment());
                checkIdItemNav(R.id.nav_payment);
                break;
            case R.id.nav_account:
                openFragment(FRAGMENT_ACCOUNT, new fragment_Account());
                checkIdItemNav(R.id.nav_account);
                break;
        }
        return true;
    }

    private void initDrawerLayout() {
        this.binding.lvMenuItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    for (int i = 0; i <= binding.lvMenuItem.getChildCount(); i++) {
                        if (position == i) {
                            binding.lvMenuItem.getChildAt(i).setBackgroundColor(Color.parseColor("#767dff"));
                        } else {
                            binding.lvMenuItem.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (position) {
                    case 0:
                        openFragment(FRAGMENT_HOME, new fragment_Home());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 1:
                        openFragment(FRAGMENT_NAV_PHONE, new fragment_Nav_Iphone());
                        binding.navigationView.setCheckedItem(position);
                        setBottomBarAndDrawerLayout();
                        break;
                    case 2:
                        openFragment(FRAGMENT_NAV_TABLE, new fragment_Nav_Ipad());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 3:
                        openFragment(FRAGMENT_NAV_LAPTOP, new fragment_Nav_Laptop());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 4:
                        openFragment(FRAGMENT_NAV_ACCESSORIES, new fragment_Nav_Accessories());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 5:
                        openFragment(FRAGMENT_NAV_EARPHONE, new fragment_Nav_Earphone());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 6:
                        openFragment(FRAGMENT_NAV_APPLECARE, new fragment_Nav_Applecare());
                        setBottomBarAndDrawerLayout();
                        break;
                    case 7:
                        openFragment(FRAGMENT_NAV_WATCH, new fragment_Nav_Watch());
                        setBottomBarAndDrawerLayout();
                        break;
                }
            }
        });
    }

    private void setBottomBarAndDrawerLayout() {
        binding.bottomNavigaitonView.getMenu().findItem(R.id.nav_home).setChecked(true);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        this.toolbarBack(false);
    }

    private void checkIdItemNav(int id_item) {
        binding.bottomNavigaitonView.getMenu().findItem(id_item).setChecked(true);
        toolbarBack(false);
    }


    private void initBottomBar() {
        binding.bottomNavigaitonView.setOnNavigationItemSelectedListener(MainActivity.this);
        binding.bottomNavigaitonView.getMenu().findItem(R.id.nav_home).setChecked(true);
        initFragment(new fragment_Home());
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}