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
import com.example.shopsmart.Adapter.ProductEarphoneAdapter;
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


public class fragment_Nav_Earphone extends Fragment {
    private int position_Earphone;

    private List<Product> productList;
    private ViewFlipper flipperEarphone;
    private List<String> vFlipperEarphoneList;
    private ImageView ivFlipperEarphone;
    private ProductEarphoneAdapter productEarphoneAdapter;
    private RecyclerView mRecyclerViewEarphone;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnEarphoneIphone, mbtnEarphoneSamsung, mbtnEarphoneXiaomi, mbtnEarphoneAll;
    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private int position_Manufacturer_All = 0;
    private ImageView ivEarphoneNotifyEmpty;
    private TextView tvEarphoneNotifyEmpty;

    public fragment_Nav_Earphone() {
        // Required empty public constructor
    }


    public static fragment_Nav_Earphone newInstance() {
        fragment_Nav_Earphone fragment = new fragment_Nav_Earphone();
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
        View view = inflater.inflate(R.layout.fragment_nav_earphone, container, false);
        Bundle bundle = getArguments();
        this.position_Earphone = bundle.getInt("positionEarphone");
        this.productList = new ArrayList<>();

        this.flipperEarphone = view.findViewById(R.id.vf_slideEarphone);
        this.mRecyclerViewEarphone = view.findViewById(R.id.rv_Earphone);
        this.mbtnEarphoneAll = view.findViewById(R.id.btn_EarphoneAll);
        this.mbtnEarphoneIphone = view.findViewById(R.id.btn_EarphoneIphone);
        this.mbtnEarphoneSamsung = view.findViewById(R.id.btn_EarphoneSamsung);
        this.mbtnEarphoneXiaomi = view.findViewById(R.id.btn_EarphoneXiaomi);
        this.ivEarphoneNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvEarphoneNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);


        this.productEarphoneAdapter = new ProductEarphoneAdapter(getContext(), this.productList);
        this.mRecyclerViewEarphone.setHasFixedSize(true);
        this.mRecyclerViewEarphone.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewEarphone.setAdapter(this.productEarphoneAdapter);
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
            this.showViewFlipperEarphone();
            this.postDataByIDcategoryProduct(this.position_Earphone);
            checkPositionButton(position_Earphone, position_Manufacturer_All);
            this.mbtnEarphoneAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Earphone);
                    checkPositionButton(position_Earphone, position_Manufacturer_All);
                }
            });
            this.mbtnEarphoneIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Earphone, position_ManufacturerIphone);
                    checkPositionButton(position_Earphone, position_ManufacturerIphone);
                }
            });
            this.mbtnEarphoneSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Earphone, position_ManufacturerSamsung);
                    checkPositionButton(position_Earphone, position_ManufacturerSamsung);
                }
            });
            this.mbtnEarphoneXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Earphone, positon_ManufacturerXiaomi);
                    checkPositionButton(position_Earphone, positon_ManufacturerXiaomi);

                }
            });
        }
        return view;
    }

    private void showViewFlipperEarphone() {
        this.vFlipperEarphoneList = new ArrayList<>();
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/Cate-595x100-03ms1.png");
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/Cate-595x100-05hk-1.png");
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/lg-2021-595-100-max.png");
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/c500595-100-max.png");
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/airpods-3-595-100-max.png");
        this.vFlipperEarphoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/sennheiser-595-100-max.png");
        for (int i = 0; i < this.vFlipperEarphoneList.size(); i++) {
            this.ivFlipperEarphone = new ImageView(getContext());
            Picasso.get().load(this.vFlipperEarphoneList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperEarphone);
            this.ivFlipperEarphone.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperEarphone.addView(this.ivFlipperEarphone);
        }
        this.flipperEarphone.setFlipInterval(5000);
        this.flipperEarphone.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperEarphone.setInAnimation(animation_in);
        this.flipperEarphone.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Earphone) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            ivEarphoneNotifyEmpty.setVisibility(View.GONE);
                            mRecyclerViewEarphone.setVisibility(View.VISIBLE);
                            tvEarphoneNotifyEmpty.setVisibility(View.GONE);
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
                            productEarphoneAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        ivEarphoneNotifyEmpty.setVisibility(View.VISIBLE);
                        tvEarphoneNotifyEmpty.setVisibility(View.VISIBLE);

                        tvEarphoneNotifyEmpty.setText("Chưa có sản phẩm");

                        mRecyclerViewEarphone.setVisibility(View.GONE);
                    }
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
                params.put("categoryID", String.valueOf(position_Earphone));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getDataByIDCategoryANDManufacturerProduct(int position_Earphone, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivEarphoneNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewEarphone.setVisibility(View.VISIBLE);
                        tvEarphoneNotifyEmpty.setVisibility(View.GONE);
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
                        productEarphoneAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivEarphoneNotifyEmpty.setVisibility(View.VISIBLE);
                    tvEarphoneNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvEarphoneNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvEarphoneNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvEarphoneNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewEarphone.setVisibility(View.GONE);
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
                getParam.put("categoryID", String.valueOf(position_Earphone));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void checkPositionButton(int position_Earphone, int position_Manufacturer_Earphone) {
        Log.e("position_Earphone",String.valueOf(position_Earphone));
        if (position_Earphone == 5 && position_Manufacturer_Earphone == 0) {
            this.mbtnEarphoneAll.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnEarphoneAll.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Earphone == 5 && position_Manufacturer_Earphone == 1) {
            this.mbtnEarphoneIphone.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnEarphoneIphone.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Earphone == 5 && position_Manufacturer_Earphone == 2) {
            this.mbtnEarphoneSamsung.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnEarphoneSamsung.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Earphone == 5 && position_Manufacturer_Earphone == 3) {
            this.mbtnEarphoneXiaomi.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnEarphoneXiaomi.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}