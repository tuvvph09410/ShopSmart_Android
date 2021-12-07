package com.example.shopsmart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsmart.Entity.Product;
import com.example.shopsmart.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduct> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_product, null);
        ViewHolderProduct viewHolderProduct = new ViewHolderProduct(view);
        return viewHolderProduct;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        Product product = this.productList.get(position);
        if (product.getPrice() > 15000000){
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            Picasso.get().load(product.getUrlImage()).placeholder(R.drawable.ic_baseline_home_24).error(R.drawable.ic_baseline_error_24).into(holder.ivProduct);
            holder.tvNameProduct.setText(product.getName());
            holder.tvPriceProduct.setText(decimalFormat.format(product.getPrice())+"₫");
        }


    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvNameProduct;
        TextView tvPriceProduct;
        CardView cardView;

        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_fragmentImageProduct);
            tvNameProduct = itemView.findViewById(R.id.tv_fragmentNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tv_fragmentPriceProduct);
            cardView = (CardView) itemView.findViewById(R.id.cv_itemProduct);
        }
    }
}
