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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).placeholder(R.drawable.ic_baseline_home_24).error(R.drawable.ic_baseline_error_24).into(holder.ivProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvPriceProduct.setText(decimalFormat.format(product.getPrice()) + "₫");
        if (product.getActive() == 0) {
            holder.tvActiveProduct.setText("SẮP VỀ HÀNG");
        } else {
            holder.tvActiveProduct.setText("CÒN HÀNG");
        }
        position = holder.position;
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvNameProduct;
        TextView tvPriceProduct;
        TextView tvActiveProduct;
        CardView cardView;
        int position;

        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_fragmentImageProduct);
            tvNameProduct = itemView.findViewById(R.id.tv_fragmentNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tv_fragmentPriceProduct);
            tvActiveProduct = itemView.findViewById(R.id.tv_fragmentActiveProduct);
            cardView = (CardView) itemView.findViewById(R.id.cv_itemProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = productList.get(position);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    fragment_Detail_Product fragment_detail_product = new fragment_Detail_Product();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idProduct", product.getId());
                    fragment_detail_product.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Home, fragment_detail_product).addToBackStack(null).commit();

                }
            });
        }
    }

}
