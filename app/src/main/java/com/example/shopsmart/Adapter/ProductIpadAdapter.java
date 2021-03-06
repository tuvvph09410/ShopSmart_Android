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

public class ProductIpadAdapter extends RecyclerView.Adapter<ProductIpadAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductIpadAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_ipad_product, null);
        ProductIpadAdapter.ViewHolder viewHolder = new ProductIpadAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Ipad);
        holder.tv_nameIpad.setText(product.getName());
        if (product.getFromPrice() != 0) {
            holder.tv_priceIpad.setText(decimalFormat.format(product.getToPrice()) + " - " + decimalFormat.format(product.getFromPrice()) + "₫");
        } else {
            holder.tv_priceIpad.setText(decimalFormat.format(product.getToPrice()) + "₫");
        }
        if (product.getActive() == 0) {
            holder.tv_activeIpad.setText("SẮP VỀ HÀNG");
        } else {
            holder.tv_activeIpad.setText("CÒN HÀNG");
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
        ImageView iv_Ipad;
        TextView tv_nameIpad;
        TextView tv_priceIpad;
        TextView tv_activeIpad;
        CardView cardView;
        int idProduct;
        String nameProduct;
        String descriptionProduct;
        int priceProduct;
        String urlImageSimple;
        int activeProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Ipad = itemView.findViewById(R.id.iv_Ipad_Product);
            tv_nameIpad = itemView.findViewById(R.id.tv_nameIpad_Product);
            tv_priceIpad = itemView.findViewById(R.id.tv_priceIpad_Product);
            tv_activeIpad = itemView.findViewById(R.id.tv_activeIpad_Product);
            cardView = itemView.findViewById(R.id.cv_Ipad_Product);
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
