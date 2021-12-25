package com.example.shopsmart.Fragment;

import android.content.Context;
import android.graphics.Color;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Activity.MainActivity;
import com.example.shopsmart.Adapter.SliderAdapterDetailProduct;
import com.example.shopsmart.Entity.CapacityProduct;
import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
import com.google.android.material.imageview.ShapeableImageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_Detail_Product extends Fragment implements View.OnClickListener {
    private CardView cvCenter, cvBottom;
    private SliderView sv_DetailProduct;
    private TextView tvTitleDetailProduct, tvDescriptionProduct, tvPriceProduct;
    private ImageView ivEvaluateProduct, iv_DetailProduct;
    private Button mbtn_detail1, mbtn_detail2, mbtn_detail3, mbtn_detail4, mbtn_detail5, mbtn_detail6;
    private ShapeableImageView siColor1, siColor2, siColor3, siColor4, siColor5, siColor6;
    private List<ColorProduct> listColor;
    private List<CapacityProduct> listCapacity;
    private SliderAdapterDetailProduct sliderAdapterDetailProduct;
    private int idProduct;
    private String nameProduct;
    private String descriptionProduct;
    private int priceProduct;
    private String urlImageSimple;
    private LinearLayout llCapacity;

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
        // /khi cần bật nút back thì gọi hàm này true là bật false là tắt
        ((MainActivity) requireActivity()).toolbarBack(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);

        this.createComponent(view);

        this.getArgument();

        this.setBackgroundCardView();

        this.loadDetailProduct();

        if (CheckConnected.haveNetworkConnection(getContext())) {
            this.getColorByIdProduct(idProduct);
            this.slideImageProduct();
            this.loadImage();
            this.getDataCapacityByIDProduct(idProduct);
            this.setOnClickListener();

        }
        return view;
    }

    private void setOnClickListener() {
        this.mbtn_detail1.setOnClickListener(this);
        this.mbtn_detail2.setOnClickListener(this);
        this.mbtn_detail3.setOnClickListener(this);
        this.mbtn_detail4.setOnClickListener(this);
        this.mbtn_detail5.setOnClickListener(this);
        this.mbtn_detail6.setOnClickListener(this);
        this.siColor1.setOnClickListener(this);
        this.siColor2.setOnClickListener(this);
        this.siColor3.setOnClickListener(this);
        this.siColor4.setOnClickListener(this);
        this.siColor5.setOnClickListener(this);
        this.siColor6.setOnClickListener(this);
    }

    private void createComponent(View view) {
        this.cvCenter = view.findViewById(R.id.cv_center);
        this.cvBottom = view.findViewById(R.id.cv_bottom);
        this.sv_DetailProduct = view.findViewById(R.id.sv_DetailProduct);
        this.tvTitleDetailProduct = view.findViewById(R.id.tv_nameDetailProduct);
        this.ivEvaluateProduct = view.findViewById(R.id.iv_evaluateProduct);
        this.tvDescriptionProduct = view.findViewById(R.id.tv_informationProduct);
        this.tvPriceProduct = view.findViewById(R.id.tvPriceProduct);
        this.iv_DetailProduct = view.findViewById(R.id.iv_svDetailProduct);
        this.mbtn_detail1 = view.findViewById(R.id.mbtn_detail1);
        this.mbtn_detail2 = view.findViewById(R.id.mbtn_detail2);
        this.mbtn_detail3 = view.findViewById(R.id.mbtn_detail3);
        this.mbtn_detail4 = view.findViewById(R.id.mbtn_detail4);
        this.mbtn_detail5 = view.findViewById(R.id.mbtn_detail5);
        this.mbtn_detail6 = view.findViewById(R.id.mbtn_detail6);
        this.llCapacity = view.findViewById(R.id.llCapacity);
        this.siColor1 = view.findViewById(R.id.si_Color1);
        this.siColor2 = view.findViewById(R.id.si_Color2);
        this.siColor3 = view.findViewById(R.id.si_Color3);
        this.siColor4 = view.findViewById(R.id.si_Color4);
        this.siColor5 = view.findViewById(R.id.si_Color5);
        this.siColor5 = view.findViewById(R.id.si_Color6);

    }

    private void getArgument() {
        this.idProduct = getArguments().getInt("idProduct");
        Log.d("idProduct", String.valueOf(idProduct));
        this.nameProduct = getArguments().getString("nameProduct");
        this.descriptionProduct = getArguments().getString("descriptionProduct");
        this.priceProduct = getArguments().getInt("priceProduct");
        this.urlImageSimple = getArguments().getString("urlImageSimple");

    }

    private void loadDetailProduct() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        this.tvTitleDetailProduct.setText(this.nameProduct);
        this.tvDescriptionProduct.setText(this.descriptionProduct);
        this.tvPriceProduct.setText(decimalFormat.format(this.priceProduct) + "đ");
    }

    private void loadImage() {
        Picasso.get().load("https://img.icons8.com/color/48/000000/star--v1.png").error(R.drawable.ic_baseline_error_24).into(ivEvaluateProduct);
    }

    private void slideImageProduct() {

        this.sliderAdapterDetailProduct = new SliderAdapterDetailProduct(this.listColor);
        this.sv_DetailProduct.setSliderAdapter(this.sliderAdapterDetailProduct);
        this.sv_DetailProduct.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        this.sv_DetailProduct.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        this.sv_DetailProduct.setScrollTimeInSec(4);
        this.sv_DetailProduct.startAutoCycle();

    }

    private void setBackgroundCardView() {
        this.cvCenter.setBackgroundResource(R.drawable.corner_cardview_center_product);
        this.cvBottom.setBackgroundResource(R.drawable.corner_cardview_bottom_product);
    }


    private void getColorByIdProduct(int idProduct) {
        this.listColor = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetColorByIDProduct(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            sv_DetailProduct.setVisibility(View.VISIBLE);
                            iv_DetailProduct.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idColor = jsonObject.getInt("idImage");
                            int idProduct = jsonObject.getInt("idProduct");
                            String urlImages = jsonObject.getString("urlImage");
                            String color = jsonObject.getString("color");
                            ColorProduct colorProduct = new ColorProduct(idColor, idProduct, urlImages, color);
                            listColor.add(colorProduct);
                            sliderAdapterDetailProduct.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sv_DetailProduct.setVisibility(View.GONE);
                        iv_DetailProduct.setVisibility(View.VISIBLE);
                        Picasso.get().load(urlImageSimple).error(R.drawable.ic_baseline_error_24).into(iv_DetailProduct);
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

    private void getDataCapacityByIDProduct(int idProduct) {
        this.listCapacity = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getUrlGetCapacityIDProduct(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            llCapacity.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idCapacity = jsonObject.getInt("idCapacity");
                            int idproduct = jsonObject.getInt("idProduct");
                            String capacity = jsonObject.getString("capacity");
                            int price = jsonObject.getInt("price");
                            CapacityProduct product = new CapacityProduct(idCapacity, capacity, price);
                            listCapacity.add(product);
                        }
                        setTextPositionButton();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        llCapacity.setVisibility(View.GONE);
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

    private void checkPositionButton(String capacity) {

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        for (int i = 0; i < listCapacity.size(); i++) {
            CapacityProduct capacityProduct = listCapacity.get(i);
            if (capacityProduct.getCapacity().equals(capacity)) {
                tvPriceProduct.setText(decimalFormat.format(capacityProduct.getPrice()));
            }
        }
    }

    private void setTextPositionButton() {
        Log.d("listCapacity", String.valueOf(listCapacity.size()));
        int lenghtList = listCapacity.size();
        if (lenghtList != 0) {
            if (position_Button1 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button1);
                mbtn_detail1.setText(capacityProduct.getCapacity());
                mbtn_detail1.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail1.setVisibility(View.GONE);
            }
            if (position_Button2 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button2);
                mbtn_detail2.setText(capacityProduct.getCapacity());
                mbtn_detail2.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail2.setVisibility(View.GONE);
            }
            if (position_Button3 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button3);
                mbtn_detail3.setText(capacityProduct.getCapacity());
                mbtn_detail3.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail3.setVisibility(View.GONE);
            }
            if (position_Button4 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button4);
                mbtn_detail4.setText(capacityProduct.getCapacity());
                mbtn_detail4.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail4.setVisibility(View.GONE);
            }
            if (position_Button5 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button5);
                mbtn_detail5.setText(capacityProduct.getCapacity());
                mbtn_detail5.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail5.setVisibility(View.GONE);
            }
            if (position_Button6 < lenghtList) {
                CapacityProduct capacityProduct = listCapacity.get(position_Button6);
                mbtn_detail6.setText(capacityProduct.getCapacity());
                mbtn_detail6.setVisibility(View.VISIBLE);
            } else {
                mbtn_detail6.setVisibility(View.GONE);
            }

        }

    }

    private int position_Button1 = 0;
    private int position_Button2 = 1;
    private int position_Button3 = 2;
    private int position_Button4 = 3;
    private int position_Button5 = 4;
    private int position_Button6 = 5;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mbtn_detail1:
                String capacity1 = mbtn_detail1.getText().toString();
                checkPositionButton(capacity1);
                positionButton(position_Button1);
                break;
            case R.id.mbtn_detail2:
                String capacity2 = mbtn_detail2.getText().toString();
                checkPositionButton(capacity2);
                positionButton(position_Button2);
                break;
            case R.id.mbtn_detail3:
                String capacity3 = mbtn_detail3.getText().toString();
                checkPositionButton(capacity3);
                positionButton(position_Button3);
                break;
            case R.id.mbtn_detail4:
                String capacity4 = mbtn_detail4.getText().toString();
                checkPositionButton(capacity4);
                positionButton(position_Button4);
                break;
            case R.id.mbtn_detail5:
                String capacity5 = mbtn_detail5.getText().toString();
                checkPositionButton(capacity5);
                positionButton(position_Button5);
                break;
            case R.id.mbtn_detail6:
                String capacity6 = mbtn_detail6.getText().toString();
                checkPositionButton(capacity6);
                positionButton(position_Button6);
                break;
            case R.id.si_Color1:
                break;
            case R.id.si_Color2:
                break;
            case R.id.si_Color3:
                break;
            case R.id.si_Color4:
                break;
            case R.id.si_Color5:
                break;
            case R.id.si_Color6:
                break;

        }
    }

    private void positionButton(int position_Button) {
        //check data với vị trí buttons đã click
        if (position_Button == 0) {
            mbtn_detail1.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail1.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Button == 1) {
            mbtn_detail2.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail2.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Button == 2) {
            mbtn_detail3.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail3.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Button == 3) {
            mbtn_detail4.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail4.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Button == 3) {
            mbtn_detail5.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail5.setBackgroundColor(Color.TRANSPARENT);
        }
        if (position_Button == 3) {
            mbtn_detail5.setBackgroundColor(Color.parseColor("#ecc8ff"));
        } else {
            mbtn_detail5.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}