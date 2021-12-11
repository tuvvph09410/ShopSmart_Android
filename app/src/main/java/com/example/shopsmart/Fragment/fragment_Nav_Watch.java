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
import com.example.shopsmart.Adapter.ProductWatchAdapter;
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


public class fragment_Nav_Watch extends Fragment {
    private int position_Watch;
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ViewFlipper flipperWatch;
    private List<String> vFlipperWatchList;
    private ImageView ivFlipperWatch;
    private ProductWatchAdapter productWatchAdapter;
    private RecyclerView mRecyclerViewWatch;
    private loadingDialog_ProgressBar dialog_progressBar;
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
                            productWatchAdapter.notifyDataSetChanged();
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
                params.put("categoryID", String.valueOf(position_Watch));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}