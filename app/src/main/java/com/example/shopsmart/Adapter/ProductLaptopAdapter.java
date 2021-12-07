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

public class ProductLaptopAdapter  extends RecyclerView.Adapter<ProductLaptopAdapter.ViewHolder>{
    private Context context;
    private List<Product> productList;
    public ProductLaptopAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }



    @NonNull
    @Override
    public ProductLaptopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_laptop_product, null);
        ProductLaptopAdapter.ViewHolder viewHolder = new ProductLaptopAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductLaptopAdapter.ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Laptop);
        holder.tv_nameLaptop.setText(product.getName());
        holder.tv_priceLaptop.setText(decimalFormat.format(product.getPrice())+"₫");
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Laptop;
        TextView tv_nameLaptop;
        TextView tv_priceLaptop;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Laptop = itemView.findViewById(R.id.iv_Laptop_Product);
            tv_nameLaptop = itemView.findViewById(R.id.tv_nameLaptop_Product);
            tv_priceLaptop = itemView.findViewById(R.id.tv_priceLaptop_Product);
            cardView = itemView.findViewById(R.id.cv_Laptop_Product);
        }
    }
}
