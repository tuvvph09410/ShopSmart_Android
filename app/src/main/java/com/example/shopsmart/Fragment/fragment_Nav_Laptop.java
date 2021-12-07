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
import com.example.shopsmart.Adapter.ProductLaptopAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Nav_Laptop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Nav_Laptop extends Fragment {
    private int position_Laptop;
    private int id = 0;
    private String name = null;
    private int idCategory = 0;
    private int price = 0;
    private String urlImage = null;
    private String description = null;
    private int active = 0;
    private List<Product> productList;
    private ViewFlipper flipperLaptop;
    private List<String> vFlipperSmartPhoneList;
    private ImageView ivFlipperLaptop;
    private ProductLaptopAdapter productLaptopAdapter;
    private RecyclerView mRecyclerViewLaptop;

    public fragment_Nav_Laptop() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_Nav_Laptop newInstance(String param1, String param2) {
        fragment_Nav_Laptop fragment = new fragment_Nav_Laptop();
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
        View view = inflater.inflate(R.layout.fragment_nav_laptop, container, false);
        Bundle bundle = getArguments();
        this.position_Laptop = bundle.getInt("positionLaptop");
        this.productList = new ArrayList<>();

        this.flipperLaptop = view.findViewById(R.id.vf_slideLaptop);
        this.mRecyclerViewLaptop = view.findViewById(R.id.rv_Laptop);
        this.productLaptopAdapter = new ProductLaptopAdapter(getContext(), this.productList);
        this.mRecyclerViewLaptop.setHasFixedSize(true);
        this.mRecyclerViewLaptop.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.mRecyclerViewLaptop.setAdapter(this.productLaptopAdapter);

        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.showViewFlipperLaptop();
            this.postDataByIDcategoryProduct(this.position_Laptop);
        }
        return view;
    }

    private void showViewFlipperLaptop() {
        this.vFlipperSmartPhoneList = new ArrayList<>();
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/msi-gaming-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/asus-b5-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/zbfl-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/12_1.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/12_1.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/_led-325-595-100-max.png");
        this.vFlipperSmartPhoneList.add("https://cdn.cellphones.com.vn/media/resized//ltsoft/promotioncategory/NOR-T10-LAPTOP-595X100.png");

        for (int i = 0; i < this.vFlipperSmartPhoneList.size(); i++) {
            this.ivFlipperLaptop = new ImageView(getContext());
            Picasso.get().load(this.vFlipperSmartPhoneList.get(i)).error(R.drawable.ic_baseline_error_24).into(this.ivFlipperLaptop);
            this.ivFlipperLaptop.setScaleType(ImageView.ScaleType.FIT_XY);
            this.flipperLaptop.addView(this.ivFlipperLaptop);
        }
        this.flipperLaptop.setFlipInterval(5000);
        this.flipperLaptop.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getContext(), R.anim.slides_in);
        Animation animation_out = AnimationUtils.loadAnimation(getContext(), R.anim.slides_out);
        this.flipperLaptop.setInAnimation(animation_in);
        this.flipperLaptop.setOutAnimation(animation_out);
    }

    private void postDataByIDcategoryProduct(int position_Laptop) {
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
                            productLaptopAdapter.notifyDataSetChanged();
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
                params.put("categoryID", String.valueOf(position_Laptop));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}