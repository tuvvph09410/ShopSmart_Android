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
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Adapter.ProductAdapter;
import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class fragment_Home extends Fragment {
    private ViewFlipper viewFlipper;
    private List<String> listViewFlopper;
    private ImageView imageViewFlopper;
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private RecyclerView rv_product;

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
        this.productList = new ArrayList<>();
        this.productAdapter = new ProductAdapter(getContext(), this.productList);
        this.rv_product.setHasFixedSize(true);
        this.rv_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.rv_product.setAdapter(this.productAdapter);
        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.showViewFipper();
            this.getDataProductJson();
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
        Context context;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.getUrlGetProduct(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("idProduct");
                            name = jsonObject.getString("name");
                            idCategory = jsonObject.getInt("idCategory");
                            price = jsonObject.getInt("price");
                            urlImage = jsonObject.getString("urlImage");
                            description = jsonObject.getString("description");
                            active = jsonObject.getInt("active");
                            Product product = new Product(id, name, idCategory, price, urlImage, description, active);
                            productList.add(product);
                            productAdapter.notifyDataSetChanged();
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
}