package com.example.shopsmart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.shopsmart.Entity.Category;
import com.example.shopsmart.Fragment.fragment_Account;
import com.example.shopsmart.Fragment.fragment_Home;
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

public class MainActivity extends AppCompatActivity {
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

        this.actionToolBar();

        this.adapterCategoryMenu = new CategoryMenuStartAdapter(categoryList, getApplicationContext());
        this.lv_menuItem.setAdapter(this.adapterCategoryMenu);
        if (CheckConnected.haveNetworkConnection(getApplicationContext())) {

            this.getDataCategoryJson();
            this.clickItemListView();
        }

        this.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.actionBar = getSupportActionBar();
        loadFragment(new fragment_Home());

    }

    private void actionToolBar() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    fragment = new fragment_Home();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_local_store:
                    fragment = new fragment_Store();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_payment:
                    fragment = new fragment_Payment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_account:
                    fragment = new fragment_Account();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    private void getDataCategoryJson() {
        Context context;
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
                    categoryList.add(0, new Category(0, "Trang Chủ", "https://cdn-icons.flaticon.com/png/512/3040/premium/3040144.png?token=exp=1638780109~hmac=423b8d2156688c28445fbcb872c4aa47", "Sale sập sàn"));
                    categoryList.add(7, new Category(9, "Thông Tin", "https://cdn-icons.flaticon.com/png/512/3281/premium/3281312.png?token=exp=1638780378~hmac=f96c64aa877f3702db6aeececae28223", "Vũ Văn Tú - PH09410"));
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
    private void clickItemListView(){
        this.lv_menuItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                switch (position){
                    case 0:
                        if (CheckConnected.haveNetworkConnection(getApplicationContext())){
                            fragment=new fragment_Home();
                            loadFragment(fragment);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }
}