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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.R;
import com.example.shopsmart.Until.CheckConnected;
import com.example.shopsmart.Until.Server;
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


public class fragment_Detail_Product extends Fragment {
    private CardView cvCenter, cvBottom;
    private ActionBar actionBar;
    private SliderView sv_DetailProduct;
    private TextView tvTitleDetailProduct, tvDescriptionProduct,tvPriceProduct;
    private ImageView ivEvaluateProduct;
    private List<ColorProduct> listColor;
    private SliderAdapterDetailProduct sliderAdapterDetailProduct;
    private int idProduct;
    private String nameProduct;
    private String descriptionProduct;
    private int priceProduct;

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
        }
        return view;
    }

    private void createComponent(View view) {
        this.cvCenter = view.findViewById(R.id.cv_center);
        this.cvBottom = view.findViewById(R.id.cv_bottom);
        this.sv_DetailProduct = view.findViewById(R.id.sv_DetailProduct);
        this.tvTitleDetailProduct = view.findViewById(R.id.tv_nameDetailProduct);
        this.ivEvaluateProduct = view.findViewById(R.id.iv_evaluateProduct);
        this.tvDescriptionProduct = view.findViewById(R.id.tv_informationProduct);
        this.tvPriceProduct=view.findViewById(R.id.tvPriceProduct);
    }

    private void getArgument() {
        this.idProduct = getArguments().getInt("idProduct");
        Log.d("idProduct", String.valueOf(idProduct));
        this.nameProduct = getArguments().getString("nameProduct");
        this.descriptionProduct = getArguments().getString("descriptionProduct");
        this.priceProduct=getArguments().getInt("priceProduct");


    }

    private void loadDetailProduct() {
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
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
        this.sv_DetailProduct.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
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