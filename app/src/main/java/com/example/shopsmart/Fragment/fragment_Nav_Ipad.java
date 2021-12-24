package com.example.shopsmart.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.shopsmart.Adapter.ProductIpadAdapter;
import com.example.shopsmart.Adapter.ProductIpadAdapter;
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


public class fragment_Nav_Ipad extends Fragment {
    private int position_Ipad;
    private List<Product> productList;
    private ViewFlipper flipperIpad;
    private List<String> vFlipperIpadList;
    private ImageView ivFlipperIpad;
    private ProductIpadAdapter productIpadAdapter;
    private RecyclerView mRecyclerViewIpad;
    private loadingDialog_ProgressBar dialog_progressBar;
    private MaterialButton mbtnIpadIphone, mbtnIpadSamsung, mbtnIpadXiaomi, mbtnIpadAll;
    private int position_ManufacturerIphone = 1;
    private int position_ManufacturerSamsung = 2;
    private int positon_ManufacturerXiaomi = 3;
    private int position_Manufacturer_All = 0;
    private ImageView ivIpadNotifyEmpty;
    private TextView tvIpadNotifyEmpty;

    public fragment_Nav_Ipad() {
        // Required empty public constructor
    }


    public static fragment_Nav_Ipad newInstance() {
        fragment_Nav_Ipad fragment = new fragment_Nav_Ipad();
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
        View view = inflater.inflate(R.layout.fragment_nav_ipad, container, false);
        Bundle bundle = getArguments();
        this.position_Ipad = bundle.getInt("positionIpad");
        this.productList = new ArrayList<>();

        this.flipperIpad = view.findViewById(R.id.vf_slideIpad);
        this.mRecyclerViewIpad = view.findViewById(R.id.rv_Ipad);
        this.mbtnIpadAll = view.findViewById(R.id.btn_IpadAll);
        this.mbtnIpadIphone = view.findViewById(R.id.btn_IpadIphone);
        this.mbtnIpadSamsung = view.findViewById(R.id.btn_IpadSamsung);
        this.mbtnIpadXiaomi = view.findViewById(R.id.btn_IpadXiaomi);
        this.ivIpadNotifyEmpty = view.findViewById(R.id.iv_NotifyEmpty);
        this.tvIpadNotifyEmpty = view.findViewById(R.id.tv_NotifyEmpty);

        this.productIpadAdapter = new ProductIpadAdapter(getContext(), this.productList);
        this.mRecyclerViewIpad.setHasFixedSize(true);
        this.mRecyclerViewIpad.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewIpad.setAdapter(this.productIpadAdapter);
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
            this.showViewFlipperIpad();
            this.postDataByIDcategoryProduct(this.position_Ipad);
            checkPositionButton(position_Ipad, position_Manufacturer_All);
            this.mbtnIpadAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    postDataByIDcategoryProduct(position_Ipad);
                    checkPositionButton(position_Ipad, position_Manufacturer_All);
                }
            });
            this.mbtnIpadIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Ipad, position_ManufacturerIphone);
                    checkPositionButton(position_Ipad, position_ManufacturerIphone);
                }
            });
            this.mbtnIpadSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Ipad, position_ManufacturerSamsung);
                    checkPositionButton(position_Ipad, position_ManufacturerSamsung);
                }
            });
            this.mbtnIpadXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataByIDCategoryANDManufacturerProduct(position_Ipad, positon_ManufacturerXiaomi);
                    checkPositionButton(position_Ipad, positon_ManufacturerXiaomi);

                }
            });
        }
        return view;
    }

    private void showViewFlipperIpad() {
        this.vFlipperIpadList = new ArrayList<>();
        this.vFlipperIpadList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/ts7fe-595-100-max.png");
        this.vFlipperIpadList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/gen9-mini-6-595x100_10_.png");
        this.vFlipperIpadList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/download023.png");
        for (int i = 0; i < this.vFlipperIpadList.size(); i++) {
            this.ivFlipperIpad = new ImageView(getContext());
            Picasso.get().load(this.vFlipperIpadList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperIpad);
            this.ivFlipperIpad.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperIpad.addView(this.ivFlipperIpad);
        }
        this.flipperIpad.setFlipInterval(5000);
        this.flipperIpad.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperIpad.setInAnimation(animation_in);
        this.flipperIpad.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Ipad) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDCateGory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ivIpadNotifyEmpty.setVisibility(View.GONE);
                    mRecyclerViewIpad.setVisibility(View.VISIBLE);
                    tvIpadNotifyEmpty.setVisibility(View.GONE);
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
                            productIpadAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ivIpadNotifyEmpty.setVisibility(View.VISIBLE);
                    tvIpadNotifyEmpty.setVisibility(View.VISIBLE);
                    tvIpadNotifyEmpty.setText("Chưa có sản phẩm");
                    mRecyclerViewIpad.setVisibility(View.GONE);
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
                params.put("categoryID", String.valueOf(position_Ipad));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getDataByIDCategoryANDManufacturerProduct(int position_Ipad, int position_Manufacturer) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturerAndCategory(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        ivIpadNotifyEmpty.setVisibility(View.GONE);
                        mRecyclerViewIpad.setVisibility(View.VISIBLE);
                        tvIpadNotifyEmpty.setVisibility(View.GONE);
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
                        productIpadAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivIpadNotifyEmpty.setVisibility(View.VISIBLE);
                    tvIpadNotifyEmpty.setVisibility(View.VISIBLE);
                    if (position_Manufacturer == 1) {
                        tvIpadNotifyEmpty.setText("Thương hiệu iphone chưa có sản phẩm");
                    } else if (position_Manufacturer == 2) {
                        tvIpadNotifyEmpty.setText("Thương hiệu samsung chưa có sản phẩm");
                    } else {
                        tvIpadNotifyEmpty.setText("Thương hiệu Xiaomi chưa có sản phẩm");
                    }
                    mRecyclerViewIpad.setVisibility(View.GONE);
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
                getParam.put("categoryID", String.valueOf(position_Ipad));
                getParam.put("manufacturerID", String.valueOf(position_Manufacturer));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void checkPositionButton(int position_Ipad, int position_Manufacturer_Ipad) {
        Log.e("position_Ipad",String.valueOf(position_Ipad));
        if (position_Ipad == 2 && position_Manufacturer_Ipad == 0) {
            this.mbtnIpadAll.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnIpadAll.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Ipad == 2 && position_Manufacturer_Ipad == 1) {
            this.mbtnIpadIphone.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnIpadIphone.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Ipad == 2 && position_Manufacturer_Ipad == 2) {
            this.mbtnIpadSamsung.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnIpadSamsung.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Ipad == 2 && position_Manufacturer_Ipad == 3) {
            this.mbtnIpadXiaomi.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            this.mbtnIpadXiaomi.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}