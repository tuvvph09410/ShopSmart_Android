package com.example.shopsmart.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Activity.MainActivity;
import com.example.shopsmart.Adapter.SliderAdapterDetailProduct;
import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_Detail_Product extends Fragment {
    private CardView cvCenter, cvBottom;
    private ActionBar actionBar;
    private SliderView sv_DetailProduct;
    private List<ColorProduct> listColor;
    private SliderAdapterDetailProduct sliderAdapterDetailProduct;
    private int idProduct;

    public fragment_Detail_Product() {
        // Required empty public constructor
    }


    public static fragment_Detail_Product newInstance() {
        fragment_Detail_Product fragment = new fragment_Detail_Product();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        this.cvCenter = view.findViewById(R.id.cv_center);
        this.cvBottom = view.findViewById(R.id.cv_bottom);
        this.sv_DetailProduct = view.findViewById(R.id.sv_DetailProduct);
        this.listColor = new ArrayList<>();
        this.idProduct = getArguments().getInt("idProduct");

        ((MainActivity) getActivity()).setDrawerLocked();
        this.setBackgroundCardView();


        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.getColorByIdProduct(idProduct);
            this.slideImageProduct();
        }

        return view;
    }

    private void slideImageProduct() {

        this.sliderAdapterDetailProduct = new SliderAdapterDetailProduct(this.listColor);
        this.sv_DetailProduct.setSliderAdapter(this.sliderAdapterDetailProduct);
        this.sv_DetailProduct.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        this.sv_DetailProduct.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        this.sv_DetailProduct.startAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();

        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }
    }

    private void setBackgroundCardView() {
        this.cvCenter.setBackgroundResource(R.drawable.corner_cardview_center_product);
        this.cvBottom.setBackgroundResource(R.drawable.corner_cardview_bottom_product);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.back_menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.app_bar_back:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setDrawerUnlocked();
    }

    private void getColorByIdProduct(int idProduct) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetColorByIDProduct(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idColor = jsonObject.getInt("idColor");
                            int idProduct = jsonObject.getInt("idProduct");
                            String urlImages = jsonObject.getString("urlImage");
                            String codeColor = jsonObject.getString("codeColor");
                            ColorProduct colorProduct = new ColorProduct(idColor, idProduct, urlImages, codeColor);
                            listColor.add(colorProduct);
                            sliderAdapterDetailProduct.notifyDataSetChanged();
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
                Map<String, String> getParam = new HashMap<>();
                getParam.put("productID", String.valueOf(idProduct));
                return getParam;
            }
        };
        requestQueue.add(stringRequest);
    }
}