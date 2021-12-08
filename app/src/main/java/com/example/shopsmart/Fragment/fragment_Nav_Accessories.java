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
import com.example.shopsmart.Adapter.ProductAccessoriesAdapter;
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


public class fragment_Nav_Accessories extends Fragment {
    private int position_Accessories;
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ViewFlipper flipperAccessories;
    private List<String> vFlipperAccessoriesList;
    private ImageView ivFlipperAccessories;
    private ProductAccessoriesAdapter productAccessoriesAdapter;
    private RecyclerView mRecyclerViewAccessories;

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
        this.productAccessoriesAdapter = new ProductAccessoriesAdapter(getContext(), this.productList);
        this.mRecyclerViewAccessories.setHasFixedSize(true);
        this.mRecyclerViewAccessories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewAccessories.setAdapter(this.productAccessoriesAdapter);

        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.showViewFlipperAccessories();
            this.postDataByIDcategoryProduct(this.position_Accessories);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetProDuctByIDCateGory(), new Response.Listener<String>() {
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
                            productAccessoriesAdapter.notifyDataSetChanged();
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
                params.put("categoryID", String.valueOf(position_Accessories));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}