package com.example.shopsmart.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.Fragment.fragment_Detail_Product;
import com.example.shopsmart.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductApplecareAdapter extends RecyclerView.Adapter<ProductApplecareAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductApplecareAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_applecare_product, null);
        ProductApplecareAdapter.ViewHolder viewHolder = new ProductApplecareAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Applecare);
        holder.tv_nameApplecare.setText(product.getName());
        if (product.getFromPrice() != 0) {
            holder.tv_priceApplecare.setText(decimalFormat.format(product.getToPrice()) + " - " + decimalFormat.format(product.getFromPrice()) + "₫");
        } else {
            holder.tv_priceApplecare.setText(decimalFormat.format(product.getToPrice()) + "₫");
        }

        if (product.getActive() == 0) {
            holder.tv_activeApplecare.setText("SẮP VỀ HÀNG");
        } else {
            holder.tv_activeApplecare.setText("CÒN HÀNG");
        }
        holder.idProduct = product.getId();
        holder.nameProduct = product.getName();
        holder.descriptionProduct = product.getDescription();
        holder.priceProduct = product.getToPrice();
        holder.urlImageSimple = product.getUrlImage();
        holder.activeProduct = product.getActive();
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Applecare;
        TextView tv_nameApplecare;
        TextView tv_priceApplecare;
        TextView tv_activeApplecare;
        CardView cardView;
        int idProduct;
        String nameProduct;
        String descriptionProduct;
        int priceProduct;
        String urlImageSimple;
        int activeProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Applecare = itemView.findViewById(R.id.iv_Applecare_Product);
            tv_nameApplecare = itemView.findViewById(R.id.tv_nameApplecare_Product);
            tv_priceApplecare = itemView.findViewById(R.id.tv_priceApplecare_Product);
            tv_activeApplecare = itemView.findViewById(R.id.tv_activeApplecare_Product);
            cardView = itemView.findViewById(R.id.cv_Applecare_Product);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    fragment_Detail_Product fragment_detail_product = new fragment_Detail_Product();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idProduct", idProduct);
                    bundle.putString("nameProduct", nameProduct);
                    bundle.putString("descriptionProduct", descriptionProduct);
                    bundle.putInt("priceProduct", priceProduct);
                    bundle.putString("urlImageSimple", urlImageSimple);
                    bundle.putInt("activeProduct", activeProduct);
                    fragment_detail_product.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, fragment_detail_product)
                            .addToBackStack(fragment_detail_product.getClass().getName())
                            .commit();

                }
            });
        }
    }
}
