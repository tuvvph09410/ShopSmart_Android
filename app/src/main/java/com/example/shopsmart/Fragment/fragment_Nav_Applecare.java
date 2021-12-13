package com.example.shopsmart.Fragment;

import android.os.Bundle;
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
import com.example.shopsmart.Adapter.ProductApplecareAdapter;
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


public class fragment_Nav_Applecare extends Fragment {
    private int position_Applecare;

    private List<Product> productList;
    private ViewFlipper flipperApplecare;
    private List<String> vFlipperApplecareList;
    private ImageView ivFlipperApplecare;
    private ProductApplecareAdapter productApplecareAdapter;
    private RecyclerView mRecyclerViewApplecare;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnApplecareIphone, mbtnApplecareSamsung, mbtnApplecareXiaomi, mbtnApplecareAll;

    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private ImageView ivApplecareNotifyEmpty;
    private TextView tvApplecareNotifyEmpty;
    public fragment_Nav_Applecare() {
        // Required empty public constructor
    }


    public static fragment_Nav_Applecare newInstance() {
        fragment_Nav_Applecare fragment = new fragment_Nav_Applecare();
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
        View view = inflater.inflate(R.layout.fragment_nav_applecare, container, false);
        Bundle bundle = getArguments();
        this.position_Applecare = bundle.getInt("positionApplecare");
        this.productList = new ArrayList<>();

        this.flipperApplecare = view.findViewById(R.id.vf_slideApplecare);
        this.mRecyclerViewApplecare = view.findViewById(R.id.rv_Applecare);
        this.mbtnApplecareAll = view.findViewById(R.id.btn_ApplecareAll);
        this.mbtnApplecareIphone = view.findViewById(R.id.btn_ApplecareIphone);
        this.mbtnApplecareSamsung = view.findViewById(R.id.btn_ApplecareSamsung);
        this.mbtnApplecareXiaomi = view.findViewById(R.id.btn_ApplecareXiaomi);
        this.ivApplecareNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvApplecareNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);

        this.productApplecareAdapter = new ProductApplecareAdapter(getContext(), this.productList);
        this.mRecyclerViewApplecare.setHasFixedSize(true);
        this.mRecyclerViewApplecare.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewApplecare.setAdapter(this.productApplecareAdapter);

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
            this.showViewFlipperApplecare();
            this.postDataByIDcategoryProduct(this.position_Applecare);
            this.mbtnApplecareAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Applecare);
                }
            });
            this.mbtnApplecareIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Applecare, position_ManufacturerIphone);
                }
            });
            this.mbtnApplecareSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Applecare, position_ManufacturerSamsung);
                }
            });
            this.mbtnApplecareXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Applecare, positon_ManufacturerXiaomi);

                }
            });
        }
        return view;
    }

    private void showViewFlipperApplecare() {
        this.vFlipperApplecareList = new ArrayList<>();
        this.vFlipperApplecareList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/ts7fe-595-100-max.png");
        this.vFlipperApplecareList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/gen9-mini-6-595x100_10_.png");
        this.vFlipperApplecareList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/download023.png");
        for (int i = 0; i < this.vFlipperApplecareList.size(); i++) {
            this.ivFlipperApplecare = new ImageView(getContext());
            Picasso.get().load(this.vFlipperApplecareList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperApplecare);
            this.ivFlipperApplecare.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperApplecare.addView(this.ivFlipperApplecare);
        }
        this.flipperApplecare.setFlipInterval(5000);
        this.flipperApplecare.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperApplecare.setInAnimation(animation_in);
        this.flipperApplecare.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Applecare) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivApplecareNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewApplecare.setVisibility(View.VISIBLE);
                    tvApplecareNotifyEmpty.setVisibility(View.GONE);
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
                            productApplecareAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    ivApplecareNotifyEmpty.setVisibility(View.VISIBLE);
                    tvApplecareNotifyEmpty.setVisibility(View.VISIBLE);
                    tvApplecareNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewApplecare.setVisibility(View.GONE);
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
                params.put("categoryID", String.valueOf(position_Applecare));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
    private void getDataByIDCategoryANDManufacturerProduct(int position_Applecare, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivApplecareNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewApplecare.setVisibility(View.VISIBLE);
                        tvApplecareNotifyEmpty.setVisibility(View.GONE);
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
                        productApplecareAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivApplecareNotifyEmpty.setVisibility(View.VISIBLE);
                    tvApplecareNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvApplecareNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvApplecareNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvApplecareNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewApplecare.setVisibility(View.GONE);
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
                getParam.put("categoryID", String.valueOf(position_Applecare));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }
}