package com.example.shopsmart.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopsmart.Activity.MainActivity;
import com.example.shopsmart.Adapter.ItemCapacityDetailProducAdapter;
import com.example.shopsmart.Adapter.ItemColorDetailProductAdapter;
import com.example.shopsmart.Adapter.SliderAdapterDetailProduct;
import com.example.shopsmart.Entity.CapacityProduct;
import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.Interface.ItemClickListenerCapacity;
import com.example.shopsmart.Interface.ItemClickListenerColor;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_Detail_Product extends Fragment {
    private CardView cvCenter, cvBottom;
    private SliderView sv_DetailProduct;
    private TextView tvTitleDetailProduct, tvDescriptionProduct, tvBasePriceDetail, tvActiveDetailProduct, tvSalePriceDetail;
    private ImageView ivEvaluateProduct, iv_DetailProduct;
    private Button mbtnAddInfomation, mbtnAddCart;

    private List<ColorProduct> listColor;
    private List<CapacityProduct> listCapacity;
    private SliderAdapterDetailProduct sliderAdapterDetailProduct;
    private int idProduct;
    private String nameProduct;
    private String descriptionProduct;
    private int priceProduct;
    private String urlImageSimple;
    private int activeProduct;
    private LinearLayout llCapacity, llColor;
    private RecyclerView rvColor, rvCapacityDetail;
    private ItemColorDetailProductAdapter itemColorDetailProductAdapter;
    private ItemCapacityDetailProducAdapter itemCapacityDetailProducAdapter;
    private int getSalePriceProduct = 0;
    private int getIdCapacityProduct = 0;
    private int getIdColorProduct = 0;
    private int idCart;

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

        this.checkActive();

        this.setBackgroundCardView();

        this.loadDetailProduct();

        this.initItem();

        this.clickButton();
        return view;
    }

    private void clickButton() {
        this.mbtnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSalePriceProduct != 0) {
                    Log.d("getCapacityProduct", String.valueOf(getSalePriceProduct));
                } else {
                    Toast.makeText(getContext(), "Vui chọn đầy đủ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        this.mbtnAddInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void checkActive() {
        if (this.activeProduct == 0) {
            this.tvActiveDetailProduct.setVisibility(View.VISIBLE);
            this.tvActiveDetailProduct.setText("SẮP VỀ HÀNG");
            this.mbtnAddInfomation.setVisibility(View.VISIBLE);
            this.mbtnAddCart.setVisibility(View.GONE);
        } else {
            this.mbtnAddCart.setVisibility(View.VISIBLE);
            this.mbtnAddInfomation.setVisibility(View.GONE);
            this.tvActiveDetailProduct.setVisibility(View.GONE);
        }

    }

    private void initItem() {
        this.listColor = new ArrayList<>();
        this.listCapacity = new ArrayList<>();

        if (CheckConnected.haveNetworkConnection(getContext())) {

            this.getColorByIdProduct(idProduct);

            this.slideImageProduct();

            this.loadImage();

            this.getDataCapacityByIDProduct(idProduct);

        }

    }

    private void initItemColor() {
        if (!this.listColor.isEmpty()) {
            this.itemColorDetailProductAdapter = new ItemColorDetailProductAdapter(getContext(), this.listColor);
            this.rvColor.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            this.rvColor.setLayoutManager(linearLayoutManager);
            this.rvColor.setAdapter(itemColorDetailProductAdapter);

            this.itemColorDetailProductAdapter.setItemClickListener(new ItemClickListenerColor() {
                @Override
                public void onClick(View view, int position, int idImage) {
                    for (int i = 0; i < rvColor.getChildCount(); i++) {
                        if (i == position) {
                            rvColor.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.button_background_color, null));
                        } else {
                            rvColor.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    Log.d("positionID", String.valueOf(idImage));
                    sv_DetailProduct.setCurrentPagePosition(position);
                }
            });

        } else {
            Toast.makeText(getContext(), "isEmpty", Toast.LENGTH_SHORT).show();
        }
    }

    private void initItemCapacity() {
        if (!listCapacity.isEmpty()) {
            this.itemCapacityDetailProducAdapter = new ItemCapacityDetailProducAdapter(getContext(), this.listCapacity);
            this.rvCapacityDetail.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            this.rvCapacityDetail.setLayoutManager(linearLayoutManager);
            this.rvCapacityDetail.setAdapter(itemCapacityDetailProducAdapter);
            this.itemCapacityDetailProducAdapter.setItemClickListener(new ItemClickListenerCapacity() {
                @Override
                public void onClick(View view, int position, int price, int salePrice ,int idCapacity) {
                    for (int i = 0; i < rvCapacityDetail.getChildCount(); i++) {
                        if (i == position) {
                            rvCapacityDetail.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.button_background_color));
                        } else {
                            rvCapacityDetail.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    if (price != 0 || salePrice != 0) {
                        if (salePrice != 0) {
                            getSalePriceProduct = salePrice;
                            tvSalePriceDetail.setText(decimalFormat.format(salePrice) + "đ");
                            tvBasePriceDetail.setVisibility(View.VISIBLE);
                            tvBasePriceDetail.setText(decimalFormat.format(price) + "đ");
                            tvBasePriceDetail.setPaintFlags(tvBasePriceDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            tvSalePriceDetail.setText(decimalFormat.format(price) + "đ");
                            tvBasePriceDetail.setVisibility(View.GONE);
                        }

                    } else {
                        tvSalePriceDetail.setText("Giá liên hệ");
                        tvBasePriceDetail.setVisibility(View.GONE);
                    }

                }

            });
        } else {
            Toast.makeText(getContext(), "isEmpty", Toast.LENGTH_SHORT).show();
        }
    }

    private void createComponent(View view) {
        this.cvCenter = view.findViewById(R.id.cv_center);
        this.cvBottom = view.findViewById(R.id.cv_bottom);
        this.sv_DetailProduct = view.findViewById(R.id.sv_DetailProduct);
        this.tvTitleDetailProduct = view.findViewById(R.id.tv_nameDetailProduct);
        this.ivEvaluateProduct = view.findViewById(R.id.iv_evaluateProduct);
        this.tvDescriptionProduct = view.findViewById(R.id.tv_informationProduct);
        this.tvBasePriceDetail = view.findViewById(R.id.tvBasePriceDetail);
        this.iv_DetailProduct = view.findViewById(R.id.iv_svDetailProduct);
        this.llCapacity = view.findViewById(R.id.llCapacity);
        this.llColor = view.findViewById(R.id.llColor);
        this.rvColor = view.findViewById(R.id.rv_colorDetail);
        this.rvCapacityDetail = view.findViewById(R.id.rv_capacityDetail);
        this.tvSalePriceDetail = view.findViewById(R.id.tvSalePriceDetail);
        this.tvActiveDetailProduct = view.findViewById(R.id.tvActiveDetailProduct);
        this.mbtnAddInfomation = view.findViewById(R.id.mbtnAddInfomation);
        this.mbtnAddCart = view.findViewById(R.id.mbtnAddCart);

    }

    private void getArgument() {
        this.idProduct = getArguments().getInt("idProduct");
        Log.d("idProduct", String.valueOf(idProduct));
        this.nameProduct = getArguments().getString("nameProduct");
        this.descriptionProduct = getArguments().getString("descriptionProduct");
        this.priceProduct = getArguments().getInt("priceProduct");
        this.urlImageSimple = getArguments().getString("urlImageSimple");
        this.activeProduct = getArguments().getInt("activeProduct");
    }

    private void loadDetailProduct() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        this.tvTitleDetailProduct.setText(this.nameProduct);
        this.tvDescriptionProduct.setText(this.descriptionProduct);
        this.tvSalePriceDetail.setText(decimalFormat.format(this.priceProduct) + "đ");
        this.tvBasePriceDetail.setVisibility(View.GONE);
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
                            String codeColor = jsonObject.getString("codeColor");
                            Log.d("codeColor", codeColor);
                            ColorProduct colorProduct = new ColorProduct(idColor, idProduct, urlImages, color, codeColor);
                            listColor.add(colorProduct);
                            sliderAdapterDetailProduct.notifyDataSetChanged();
                        }
                        initItemColor();
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
                            int salePrice = jsonObject.getInt("sale_price");
                            CapacityProduct product = new CapacityProduct(idCapacity, capacity, price, salePrice);
                            listCapacity.add(product);
                        }
                        initItemCapacity();
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


}