package com.example.shopsmart.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Adapter.ProductSmartPhoneAdapter;
import com.example.shopsmart.Dialog.loadingDialog_ProgressBar;
import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Nav_Iphone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Nav_Iphone extends Fragment {
    private int position_Phone;

    private List<Product> productList;
    private ViewFlipper flipperSmartPhone;
    private List<String> vFlipperSmartPhoneList;
    private ImageView ivFlipperSmartPhone;
    private ProductSmartPhoneAdapter productSmartPhoneAdapter;
    private RecyclerView mRecyclerViewSmartPhone;
    private MaterialButton mbtn_navIphone_SmartPhone, mbtn_navSamsung_SmartPhone, mbtn_navXiaomi_SmartPhone, mbtn_navAll_SmartPhone;
    private int position_Manufacturer_Iphone = 1;
    private int position_Manufacturer_Samsung = 2;
    private int position_Manufacturer_Xiaomi = 3;
    private loadingDialog_ProgressBar dialog_progressBar;
    private ImageView ivSmartPhoneNotifyEmpty;
    private TextView tvSmartPhoneNotifyEmpty;

    public fragment_Nav_Iphone() {
        // Required empty public constructor
    }


    public static fragment_Nav_Iphone newInstance() {
        fragment_Nav_Iphone fragment = new fragment_Nav_Iphone();
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
        View view = inflater.inflate(R.layout.fragment_nav_iphone, container, false);
        Bundle bundle = getArguments();
        this.position_Phone = bundle.getInt("positionPhone");
        this.productList = new ArrayList<>();

        this.flipperSmartPhone = view.findViewById(R.id.vf_slideSmartPhone);
        this.mRecyclerViewSmartPhone = view.findViewById(R.id.rv_smartPhone);
        this.mbtn_navIphone_SmartPhone = view.findViewById(R.id.btn_smartPhoneIphone);
        this.mbtn_navSamsung_SmartPhone = view.findViewById(R.id.btn_smartPhoneSamsung);
        this.mbtn_navXiaomi_SmartPhone = view.findViewById(R.id.btn_smartPhoneXiaomi);
        this.mbtn_navAll_SmartPhone = view.findViewById(R.id.btn_allSmartPhone);
        this.ivSmartPhoneNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvSmartPhoneNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);

        this.dialog_progressBar = new loadingDialog_ProgressBar(getContext());

        this.productSmartPhoneAdapter = new ProductSmartPhoneAdapter(getContext(), this.productList);
        this.mRecyclerViewSmartPhone.setHasFixedSize(true);
        this.mRecyclerViewSmartPhone.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewSmartPhone.setAdapter(productSmartPhoneAdapter);

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
            this.showViewFlipperSmartPhone();
            this.postDataByIDcategoryProduct(this.position_Phone);

            this.mbtn_navIphone_SmartPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufactuer(position_Phone, position_Manufacturer_Iphone);
                }
            });
            this.mbtn_navSamsung_SmartPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufactuer(position_Phone, position_Manufacturer_Samsung);
                }
            });
            this.mbtn_navXiaomi_SmartPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufactuer(position_Phone, position_Manufacturer_Xiaomi);
                }
            });
            this.mbtn_navAll_SmartPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Phone);
                }
            });
        }
        return view;
    }

    private void showViewFlipperSmartPhone() {
        this.vFlipperSmartPhoneList = new ArrayList<>();
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/g50-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/zs-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/ip13-xx-595x100_9_.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/11t-595x100_14_.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/sw-595-100-max.png");
        for (int i = 0; i < this.vFlipperSmartPhoneList.size(); i++) {
            this.ivFlipperSmartPhone = new ImageView(getContext());
            Picasso.get().load(this.vFlipperSmartPhoneList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperSmartPhone);
            this.ivFlipperSmartPhone.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperSmartPhone.addView(this.ivFlipperSmartPhone);
        }
        this.flipperSmartPhone.setFlipInterval(5000);
        this.flipperSmartPhone.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperSmartPhone.setInAnimation(animation_in);
        this.flipperSmartPhone.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivSmartPhoneNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewSmartPhone.setVisibility(View.VISIBLE);
                    tvSmartPhoneNotifyEmpty.setVisibility(View.GONE);
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
                            productSmartPhoneAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ivSmartPhoneNotifyEmpty.setVisibility(View.VISIBLE);
                    tvSmartPhoneNotifyEmpty.setVisibility(View.VISIBLE);
                    tvSmartPhoneNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewSmartPhone.setVisibility(View.GONE);
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
                params.put("categoryID", String.valueOf(position_Phone));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getDataByIDCategoryANDManufactuer(int position_Phone, int position_Manufacturer_Phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivSmartPhoneNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewSmartPhone.setVisibility(View.VISIBLE);
                        tvSmartPhoneNotifyEmpty.setVisibility(View.GONE);
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
                        productSmartPhoneAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnected.ShowToastLong(getContext(), error.toString());
                ivSmartPhoneNotifyEmpty.setVisibility(View.VISIBLE);
                tvSmartPhoneNotifyEmpty.setVisibility(View.VISIBLE);
                if (position_Manufacturer_Phone == 1) {
                    tvSmartPhoneNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                } else if (position_Manufacturer_Phone == 2) {
                    tvSmartPhoneNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                } else {
                    tvSmartPhoneNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                }
                mRecyclerViewSmartPhone.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> getParam = new HashMap<String, String>();
                getParam.put("categoryID", String.valueOf(position_Phone));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer_Phone));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }
}