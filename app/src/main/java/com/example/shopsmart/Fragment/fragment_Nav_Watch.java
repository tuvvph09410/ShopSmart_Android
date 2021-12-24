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
import com.example.shopsmart.Adapter.ProductWatchAdapter;
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


public class fragment_Nav_Watch extends Fragment {
    private int position_Watch;
    private List<Product> productList;
    private ViewFlipper flipperWatch;
    private List<String> vFlipperWatchList;
    private ImageView ivFlipperWatch;
    private ProductWatchAdapter productWatchAdapter;
    private RecyclerView mRecyclerViewWatch;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnWatchIphone, mbtnWatchSamsung, mbtnWatchXiaomi, mbtnWatchAll;
    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private int position_Manufacturer_All = 0;
    private ImageView ivWatchNotifyEmpty;
    private TextView tvWatchNotifyEmpty;

    public fragment_Nav_Watch() {
        // Required empty public constructor
    }


    public static fragment_Nav_Watch newInstance() {
        fragment_Nav_Watch fragment = new fragment_Nav_Watch();
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
        View view = inflater.inflate(R.layout.fragment_nav_watch, container, false);
        Bundle bundle = getArguments();
        this.position_Watch = bundle.getInt("positionWatch");
        this.productList = new ArrayList<>();

        this.flipperWatch = view.findViewById(R.id.vf_slideWatch);
        this.mRecyclerViewWatch = view.findViewById(R.id.rv_Watch);
        this.mbtnWatchAll = view.findViewById(R.id.btn_WatchAll);
        this.mbtnWatchIphone = view.findViewById(R.id.btn_WatchIphone);
        this.mbtnWatchSamsung = view.findViewById(R.id.btn_WatchSamsung);
        this.mbtnWatchXiaomi = view.findViewById(R.id.btn_WatchXiaomi);
        this.ivWatchNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvWatchNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);

        this.productWatchAdapter = new ProductWatchAdapter(getContext(), this.productList);
        this.mRecyclerViewWatch.setHasFixedSize(true);
        this.mRecyclerViewWatch.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewWatch.setAdapter(this.productWatchAdapter);
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
            this.showViewFlipperWatch();
            this.postDataByIDcategoryProduct(this.position_Watch);
            checkPositionButton(position_Watch, position_Manufacturer_All);
            this.mbtnWatchAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Watch);
                    checkPositionButton(position_Watch, position_Manufacturer_All);
                }
            });
            this.mbtnWatchIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Watch, position_ManufacturerIphone);
                    checkPositionButton(position_Watch, position_ManufacturerIphone);
                }
            });
            this.mbtnWatchSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Watch, position_ManufacturerSamsung);
                    checkPositionButton(position_Watch, position_ManufacturerSamsung);
                }
            });
            this.mbtnWatchXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Watch, positon_ManufacturerXiaomi);
                    checkPositionButton(position_Watch, positon_ManufacturerXiaomi);

                }
            });
        }
        return view;
    }

    private void showViewFlipperWatch() {
        this.vFlipperWatchList = new ArrayList<>();
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/gt3-cate-595-100-max.png");
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/apple-watch-001-595-100-max-v1a.png");
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/aw-ch-595-100-max.png");
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/series-7-595-100-max.png");
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/huawei.png");
        this.vFlipperWatchList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/Cate-1190x200awesimviettel1.png");
        for (int i = 0; i < this.vFlipperWatchList.size(); i++) {
            this.ivFlipperWatch = new ImageView(getContext());
            Picasso.get().load(this.vFlipperWatchList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperWatch);
            this.ivFlipperWatch.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperWatch.addView(this.ivFlipperWatch);
        }
        this.flipperWatch.setFlipInterval(5000);
        this.flipperWatch.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperWatch.setInAnimation(animation_in);
        this.flipperWatch.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Watch) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivWatchNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewWatch.setVisibility(View.VISIBLE);
                    tvWatchNotifyEmpty.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("idProduct");
                            String name = jsonObject.getString("name");
                            int idCategory = jsonObject.getInt("idCategory");
                            int toPrice = jsonObject.getInt("toPrice");
                            int fromPrice = jsonObject.getInt("fromPrice");
                            String urlImage = jsonObject.getString("urlImageSimple");
                            String description = jsonObject.getString("description");
                            int active = jsonObject.getInt("active");
                            Product product = new Product(id, name, idCategory, toPrice,fromPrice, urlImage, description, active);
                            productList.add(product);
                            productWatchAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } else {
                    ivWatchNotifyEmpty.setVisibility(View.VISIBLE);
                    tvWatchNotifyEmpty.setVisibility(View.VISIBLE);
                    tvWatchNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewWatch.setVisibility(View.GONE);
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
                params.put("categoryID", String.valueOf(position_Watch));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getDataByIDCategoryANDManufacturerProduct(int position_Watch, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivWatchNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewWatch.setVisibility(View.VISIBLE);
                        tvWatchNotifyEmpty.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("idProduct");
                        String name = jsonObject.getString("name");
                        int idCategory = jsonObject.getInt("idCategory");
                        int toPrice = jsonObject.getInt("toPrice");
                        int fromPrice = jsonObject.getInt("fromPrice");
                        String urlImage = jsonObject.getString("urlImageSimple");
                        String description = jsonObject.getString("description");
                        int active = jsonObject.getInt("active");
                        Product product = new Product(id, name, idCategory, toPrice,fromPrice, urlImage, description, active);
                        productList.add(product);
                        productWatchAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivWatchNotifyEmpty.setVisibility(View.VISIBLE);
                    tvWatchNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvWatchNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvWatchNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvWatchNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewWatch.setVisibility(View.GONE);
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
                getParam.put("categoryID", String.valueOf(position_Watch));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void checkPositionButton(int position_Watch, int position_Manufacturer_Watch) {
        Log.e("position_Watch", String.valueOf(position_Watch));
        if (position_Watch == 9 && position_Manufacturer_Watch == 0) {
            this.mbtnWatchAll.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnWatchAll.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Watch == 9 && position_Manufacturer_Watch == 1) {
            this.mbtnWatchIphone.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnWatchIphone.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Watch == 9 && position_Manufacturer_Watch == 2) {
            this.mbtnWatchSamsung.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnWatchSamsung.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Watch == 9 && position_Manufacturer_Watch == 3) {
            this.mbtnWatchXiaomi.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnWatchXiaomi.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}