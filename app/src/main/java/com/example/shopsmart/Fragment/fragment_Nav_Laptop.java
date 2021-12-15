package com.example.shopsmart.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Adapter.ProductLaptopAdapter;
import com.example.shopsmart.Adapter.ProductLaptopAdapter;
import com.example.shopsmart.Dialog.loadingDialog_ProgressBar;
import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Nav_Laptop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Nav_Laptop extends Fragment {
    private int position_Laptop;
    private List<Product> productList;
    private ViewFlipper flipperLaptop;
    private List<String> vFlipperLaptopList;
    private ImageView ivFlipperLaptop;
    private ProductLaptopAdapter productLaptopAdapter;
    private RecyclerView mRecyclerViewLaptop;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnLaptopIphone, mbtnLaptopSamsung, mbtnLaptopXiaomi, mbtnLaptopAll;
    private ImageView ivLaptopNotifyEmpty;
    private TextView tvLaptopNotifyEmpty;
    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private int position_Manufacturer_All = 0;

    public fragment_Nav_Laptop() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_Nav_Laptop newInstance(String param1, String param2) {
        fragment_Nav_Laptop fragment = new fragment_Nav_Laptop();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_laptop, container, false);
        Bundle bundle = getArguments();
        this.position_Laptop = bundle.getInt("positionLaptop");

        this.productList = new ArrayList<>();


        this.flipperLaptop = view.findViewById(R.id.vf_slideLaptop);
        this.mRecyclerViewLaptop = view.findViewById(R.id.rv_Laptop);
        this.mbtnLaptopAll = view.findViewById(R.id.btn_LaptopAll);
        this.mbtnLaptopIphone = view.findViewById(R.id.btn_LaptopIphone);
        this.mbtnLaptopSamsung = view.findViewById(R.id.btn_LaptopSamsung);
        this.mbtnLaptopXiaomi = view.findViewById(R.id.btn_LaptopXiaomi);
        this.ivLaptopNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvLaptopNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);


        this.productLaptopAdapter = new ProductLaptopAdapter(getContext(), this.productList);
        this.mRecyclerViewLaptop.setHasFixedSize(true);
        this.mRecyclerViewLaptop.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewLaptop.setAdapter(this.productLaptopAdapter);
        this.dialog_progressBar = new loadingDialog_ProgressBar(getContext());

        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.dialog_progressBar.startLoading_DialogProgressBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        dialog_progressBar.dismissLoading_DialogProgressBar();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            this.showViewFlipperLaptop();
            this.postDataByIDcategoryProduct(this.position_Laptop);
            this.checkPositionButton(position_Laptop, position_Manufacturer_All);
            this.mbtnLaptopAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Laptop);
                    checkPositionButton(position_Laptop, position_Manufacturer_All);
                }
            });
            this.mbtnLaptopIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Laptop, position_ManufacturerIphone);
                    checkPositionButton(position_Laptop, position_ManufacturerIphone);
                }
            });
            this.mbtnLaptopSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Laptop, position_ManufacturerSamsung);
                    checkPositionButton(position_Laptop, position_ManufacturerSamsung);
                }
            });
            this.mbtnLaptopXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Laptop, positon_ManufacturerXiaomi);
                    checkPositionButton(position_Laptop, positon_ManufacturerXiaomi);
                }
            });
        }
        return view;
    }

    private void showViewFlipperLaptop() {
        this.vFlipperLaptopList = new ArrayList<>();
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/msi-gaming-595-100-max.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/asus-b5-595-100-max.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/zbfl-595-100-max.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/12_1.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/12_1.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/_led-325-595-100-max.png");
        this.vFlipperLaptopList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/NOR-T10-LAPTOP-595X100.png");

        for (int i = 0; i < this.vFlipperLaptopList.size(); i++) {
            this.ivFlipperLaptop = new ImageView(getContext());
            Picasso.get().load(this.vFlipperLaptopList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperLaptop);
            this.ivFlipperLaptop.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperLaptop.addView(this.ivFlipperLaptop);
        }
        this.flipperLaptop.setFlipInterval(5000);
        this.flipperLaptop.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperLaptop.setInAnimation(animation_in);
        this.flipperLaptop.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Laptop) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivLaptopNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewLaptop.setVisibility(View.VISIBLE);
                    tvLaptopNotifyEmpty.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("idProduct");
                            String name = jsonObject.getString("name");
                            int idCategory = jsonObject.getInt("idCategory");
                            int price = jsonObject.getInt("price");
                            String urlImage = jsonObject.getString("urlImage");
                            String description = jsonObject.getString("description");
                            int active = jsonObject.getInt("active");
                            Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                            productList.add(product);
                            productLaptopAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ivLaptopNotifyEmpty.setVisibility(View.VISIBLE);
                    tvLaptopNotifyEmpty.setVisibility(View.VISIBLE);
                    tvLaptopNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewLaptop.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnected.ShowToastLong(getContext(), error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryID", String.valueOf(position_Laptop));
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void getDataByIDCategoryANDManufacturerProduct(int position_Laptop, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.e("arrayProduct", String.valueOf(jsonArray.length()));
                    if (jsonArray.length() > 0) {
                        ivLaptopNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewLaptop.setVisibility(View.VISIBLE);
                        tvLaptopNotifyEmpty.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("idProduct");
                        String name = jsonObject.getString("name");
                        int idCategory = jsonObject.getInt("idCategory");
                        int price = jsonObject.getInt("price");
                        String urlImage = jsonObject.getString("urlImage");
                        String description = jsonObject.getString("description");
                        int active = jsonObject.getInt("active");
                        Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                        productList.add(product);
                        productLaptopAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivLaptopNotifyEmpty.setVisibility(View.VISIBLE);
                    tvLaptopNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvLaptopNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvLaptopNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvLaptopNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewLaptop.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnected.ShowToastLong(getContext(), error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> getParam = new HashMap<>();
                getParam.put("categoryID", String.valueOf(position_Laptop));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void checkPositionButton(int position_Laptop, int position_Manufacturer_Laptop) {
        Log.e("position_Laptop", String.valueOf(position_Laptop));
        if (position_Laptop == 3 && position_Manufacturer_Laptop == 0) {
            this.mbtnLaptopAll.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnLaptopAll.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Laptop == 3 && position_Manufacturer_Laptop == 1) {
            this.mbtnLaptopIphone.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnLaptopIphone.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Laptop == 3 && position_Manufacturer_Laptop == 2) {
            this.mbtnLaptopSamsung.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnLaptopSamsung.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Laptop == 3 && position_Manufacturer_Laptop == 3) {
            this.mbtnLaptopXiaomi.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnLaptopXiaomi.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}