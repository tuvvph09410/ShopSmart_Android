package com.example.shopsmart.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.shopsmart.Adapter.ProductAdapter;
import com.example.shopsmart.Dialog.loadingDialog_ProgressBar;
import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_Home extends Fragment {
    private ViewFlipper viewFlipper;
    private List<String> listViewFlopper;
    private ImageView imageViewFlopper;

    private List<Product> productList;
    private ProductAdapter productAdapter;
    private RecyclerView rv_product;
    private Button btn_homeIphone;
    private Button btn_homeSamsung;
    private Button btn_homeXiaomi;
    private int positionIphone_Manufacturer = 1;
    private int positionSamsung_Manufacturer = 2;
    private int positionXiaomi_Manufacturer = 3;
    private loadingDialog_ProgressBar dialog_progressBar;

    public fragment_Home() {
        // Required empty public constructor
    }

    public static fragment_Home newInstance() {
        fragment_Home fragment = new fragment_Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.viewFlipper = view.findViewById(R.id.vf_slidesShow);
        this.rv_product = view.findViewById(R.id.rv_product);
        this.btn_homeIphone = view.findViewById(R.id.btn_homeIphone);
        this.btn_homeSamsung = view.findViewById(R.id.btn_homeSamsung);
        this.btn_homeXiaomi = view.findViewById(R.id.btn_homeXiaomi);

        this.dialog_progressBar = new loadingDialog_ProgressBar(getContext());

        this.productList = new ArrayList<>();
        this.productAdapter = new ProductAdapter(getContext(), this.productList);
        this.rv_product.setHasFixedSize(true);
        this.rv_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.rv_product.setAdapter(this.productAdapter);

        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.dialog_progressBar.startLoading_DialogProgressBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        dialog_progressBar.dismissLoading_DialogProgressBar();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            this.showViewFipper();
            this.getDataProductJson();
            this.btn_homeIphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataManufacturerProduct(positionIphone_Manufacturer);
                }
            });
            this.btn_homeSamsung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataManufacturerProduct(positionSamsung_Manufacturer);
                }
            });
            this.btn_homeXiaomi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.clear();
                    getDataManufacturerProduct(positionXiaomi_Manufacturer);
                }
            });
        }


        return view;
    }

    private void showViewFipper() {
        this.listViewFlopper = new ArrayList<>();
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/oled-flip-690-300-max.png");
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/gt3-690-300-max.png");
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/fold-3-rr-690-300-max_1.png");
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/macbook-pro-2021-690-300-max.png");
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/g50-690-300-max.png");
        this.listViewFlopper.add("https://cdn.cellphones.com.vn/media/ltsoft/promotion/vung-tau-690-300-max.png");
        for (int i = 0; i < this.listViewFlopper.size(); i++) {
            this.imageViewFlopper = new ImageView(getContext());
            Picasso.get().load(this.listViewFlopper.get(i)).error(R.drawable.ic_baseline_error_24).into(this.imageViewFlopper);
            this.imageViewFlopper.setScaleType(ImageView.ScaleType.FIT_XY);
            this.viewFlipper.addView(this.imageViewFlopper);
        }
        this.viewFlipper.setFlipInterval(5000);
        this.viewFlipper.setAutoStart(true);


        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);

        this.viewFlipper.setInAnimation(animation_in);
        this.viewFlipper.setOutAnimation(animation_out);


    }

    private void getDataProductJson() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.getUrlGetProduct(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("idProduct");
                            String name = jsonObject.getString("name");
                            int idCategory = jsonObject.getInt("idCategory");
                            int price = jsonObject.getInt("price");
                            String urlImage = jsonObject.getString("urlImage");
                            String description = jsonObject.getString("description");
                            int active = jsonObject.getInt("active");
                            if (price > 10000000) {
                                Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                                productList.add(product);
                                productAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnected.ShowToastLong(getContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getDataManufacturerProduct(int position_Manufacturer) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProductByIDmanufacturer(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
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
                            if (price > 10000000) {
                                Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                                productList.add(product);
                                productAdapter.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("manufacturerID", String.valueOf(position_Manufacturer));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}