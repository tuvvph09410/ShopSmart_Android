package com.example.shopsmart.Fragment;

import android.graphics.Color;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Adapter.ProductAccessoriesAdapter;
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
import java.util.List;
import java.util.Map;


public class fragment_Nav_Accessories extends Fragment {
    private int position_Accessories;
    private List<Product> productList;
    private ViewFlipper flipperAccessories;
    private List<String> vFlipperAccessoriesList;
    private ImageView ivFlipperAccessories;
    private ProductAccessoriesAdapter productAccessoriesAdapter;
    private RecyclerView mRecyclerViewAccessories;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnAccessoriesIphone, mbtnAccessoriesSamsung, mbtnAccessoriesXiaomi, mbtnAccessoriesAll;
    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private int position_Manufacturer_All = 0;
    private ImageView ivAccessoriesNotifyEmpty;
    private TextView tvAccessoriesNotifyEmpty;


    public fragment_Nav_Accessories() {
        // Required empty public constructor
    }


    public static fragment_Nav_Accessories newInstance() {
        fragment_Nav_Accessories fragment = new fragment_Nav_Accessories();
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
        View view = inflater.inflate(R.layout.fragment_nav_accessories, container, false);
        Bundle bundle = getArguments();
        this.position_Accessories = bundle.getInt("positionAccessories");
        this.productList = new ArrayList<>();

        this.flipperAccessories = view.findViewById(R.id.vf_slideAccessories);
        this.mRecyclerViewAccessories = view.findViewById(R.id.rv_Accessories);
        this.mbtnAccessoriesAll = view.findViewById(R.id.btn_AccessoriesAll);
        this.mbtnAccessoriesIphone = view.findViewById(R.id.btn_AccessoriesIphone);
        this.mbtnAccessoriesSamsung = view.findViewById(R.id.btn_AccessoriesSamsung);
        this.mbtnAccessoriesXiaomi = view.findViewById(R.id.btn_AccessoriesXiaomi);
        this.ivAccessoriesNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvAccessoriesNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);

        this.productAccessoriesAdapter = new ProductAccessoriesAdapter(getContext(), this.productList);
        this.mRecyclerViewAccessories.setHasFixedSize(true);
        this.mRecyclerViewAccessories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewAccessories.setAdapter(this.productAccessoriesAdapter);

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
            this.showViewFlipperAccessories();
            this.postDataByIDcategoryProduct(this.position_Accessories);
            checkPositionButton(position_Accessories, position_Manufacturer_All);
            this.mbtnAccessoriesAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Accessories);
                    checkPositionButton(position_Accessories, position_Manufacturer_All);

                }
            });
            this.mbtnAccessoriesIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Accessories, position_ManufacturerIphone);
                    checkPositionButton(position_Accessories, position_ManufacturerIphone);

                }
            });
            this.mbtnAccessoriesSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Accessories, position_ManufacturerSamsung);
                    checkPositionButton(position_Accessories, position_ManufacturerSamsung);
                }
            });
            this.mbtnAccessoriesXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Accessories, positon_ManufacturerXiaomi);
                    checkPositionButton(position_Accessories, positon_ManufacturerXiaomi);
                }
            });
        }
        return view;
    }

    private void showViewFlipperAccessories() {
        this.vFlipperAccessoriesList = new ArrayList<>();
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/dad595x100_1.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/Cate_595x100_Anker-1.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/banner_km_dan_mh_zclip3_zfold3.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/sdp-s-595-100-max.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/595-100-max_2_.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/uag_samsung.png");
        this.vFlipperAccessoriesList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/banner_km_op_lung_samsung.png");
        for (int i = 0; i < this.vFlipperAccessoriesList.size(); i++) {
            this.ivFlipperAccessories = new ImageView(getContext());
            Picasso.get().load(this.vFlipperAccessoriesList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperAccessories);
            this.ivFlipperAccessories.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperAccessories.addView(this.ivFlipperAccessories);
        }
        this.flipperAccessories.setFlipInterval(5000);
        this.flipperAccessories.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperAccessories.setInAnimation(animation_in);
        this.flipperAccessories.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Accessories) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivAccessoriesNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewAccessories.setVisibility(View.VISIBLE);
                    tvAccessoriesNotifyEmpty.setVisibility(View.GONE);
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
                            productAccessoriesAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ivAccessoriesNotifyEmpty.setVisibility(View.VISIBLE);
                    tvAccessoriesNotifyEmpty.setVisibility(View.VISIBLE);
                    tvAccessoriesNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewAccessories.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryID", String.valueOf(position_Accessories));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getDataByIDCategoryANDManufacturerProduct(int position_Accessories, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivAccessoriesNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewAccessories.setVisibility(View.VISIBLE);
                        tvAccessoriesNotifyEmpty.setVisibility(View.GONE);
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
                        productAccessoriesAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivAccessoriesNotifyEmpty.setVisibility(View.VISIBLE);
                    tvAccessoriesNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvAccessoriesNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvAccessoriesNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvAccessoriesNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewAccessories.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> getParam = new HashMap<>();
                getParam.put("categoryID", String.valueOf(position_Accessories));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void checkPositionButton(int position_Accessories, int position_Manufacturer_Accessories) {
        Log.e("position_Accessories", String.valueOf(position_Accessories));
        if (position_Accessories == 4 && position_Manufacturer_Accessories == 0) {
            this.mbtnAccessoriesAll.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnAccessoriesAll.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Accessories == 4 && position_Manufacturer_Accessories == 1) {
            this.mbtnAccessoriesIphone.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnAccessoriesIphone.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Accessories == 4 && position_Manufacturer_Accessories == 2) {
            this.mbtnAccessoriesSamsung.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnAccessoriesSamsung.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Accessories == 4 && position_Manufacturer_Accessories == 3) {
            this.mbtnAccessoriesXiaomi.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnAccessoriesXiaomi.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}