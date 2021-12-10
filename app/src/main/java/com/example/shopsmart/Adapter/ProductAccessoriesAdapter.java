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

public class ProductAccessoriesAdapter extends RecyclerView.Adapter<ProductAccessoriesAdapter.ViewHolder>{
    private Context context;
    private List<Product> productList;
    public ProductAccessoriesAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_accessories_product, null);
        ProductAccessoriesAdapter.ViewHolder viewHolder = new ProductAccessoriesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Accessories);
        holder.tv_nameAccessories.setText(product.getName());
        holder.tv_priceAccessories.setText(decimalFormat.format(product.getPrice())+"₫");
        if (product.getActive() == 0) {
            holder.tv_activeAccessories.setText("SẮP VỀ HÀNG");
        } else {
            holder.tv_activeAccessories.setText("CÒN HÀNG");
        }
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Accessories;
        TextView tv_nameAccessories;
        TextView tv_priceAccessories;
        TextView tv_activeAccessories;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Accessories = itemView.findViewById(R.id.iv_Accessories_Product);
            tv_nameAccessories = itemView.findViewById(R.id.tv_nameAccessories_Product);
            tv_priceAccessories = itemView.findViewById(R.id.tv_priceAccessories_Product);
            tv_activeAccessories =itemView.findViewById(R.id.tv_activeAccessories_Product);
            cardView = itemView.findViewById(R.id.cv_Accessories_Product);
        }
    }
}
