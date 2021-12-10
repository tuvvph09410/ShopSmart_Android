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

public class ProductEarphoneAdapter extends RecyclerView.Adapter<ProductEarphoneAdapter.ViewHolder>{
    private Context context;
    private List<Product> productList;
    public ProductEarphoneAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_earphone_product, null);
        ProductEarphoneAdapter.ViewHolder viewHolder = new ProductEarphoneAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Earphone);
        holder.tv_nameEarphone.setText(product.getName());
        holder.tv_priceEarphone.setText(decimalFormat.format(product.getPrice())+"₫");
        if (product.getActive() == 0) {
            holder.tv_activeEarphone.setText("SẮP VỀ HÀNG");
        } else {
            holder.tv_activeEarphone.setText("CÒN HÀNG");
        }
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Earphone;
        TextView tv_nameEarphone;
        TextView tv_priceEarphone;
        TextView tv_activeEarphone;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Earphone = itemView.findViewById(R.id.iv_Earphone_Product);
            tv_nameEarphone = itemView.findViewById(R.id.tv_nameEarphone_Product);
            tv_priceEarphone = itemView.findViewById(R.id.tv_priceEarphone_Product);
            tv_activeEarphone = itemView.findViewById(R.id.tv_activeEarphone_Product);
            cardView = itemView.findViewById(R.id.cv_Earphone_Product);
        }
    }
}
