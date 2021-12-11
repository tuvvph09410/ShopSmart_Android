package com.example.shopsmart.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ViewFlipper flipperEarphone;
    private List<String> vFlipperEarphoneList;
    private ImageView ivFlipperEarphone;
    private ProductEarphoneAdapter productEarphoneAdapter;
    private RecyclerView mRecyclerViewEarphone;
    private loadingDialog_ProgressBar dialog_progressBar;
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("idProduct");
                            name = jsonObject.getString("name");
                            idCategory = jsonObject.getInt("idCategory");
                            price = jsonObject.getInt("price");
                            urlImage = jsonObject.getString("urlImage");
                            description = jsonObject.getString("description");
                            active = jsonObject.getInt("active");
                            Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                            productList.add(product);
                            productEarphoneAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
}