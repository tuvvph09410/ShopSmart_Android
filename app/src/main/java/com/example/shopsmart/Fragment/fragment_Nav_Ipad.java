package com.example.shopsmart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ViewFlipper flipperIpad;
    private List<String> vFlipperIpadList;
    private ImageView ivFlipperIpad;
    private ProductIpadAdapter productIpadAdapter;
    private RecyclerView mRecyclerViewIpad;
    private loadingDialog_ProgressBar dialog_progressBar;
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
                            productIpadAdapter.notifyDataSetChanged();
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
                params.put("categoryID", String.valueOf(position_Ipad));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}