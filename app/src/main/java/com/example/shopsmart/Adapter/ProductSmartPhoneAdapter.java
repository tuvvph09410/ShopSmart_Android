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

public class ProductSmartPhoneAdapter extends RecyclerView.Adapter<ProductSmartPhoneAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductSmartPhoneAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_smartphone_product, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_smartPhone);
        holder.tv_nameSmartPhone.setText(product.getName());
        holder.tv_priceSmartPhone.setText(decimalFormat.format(product.getPrice())+"₫");
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_smartPhone;
        TextView tv_nameSmartPhone;
        TextView tv_priceSmartPhone;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_smartPhone = itemView.findViewById(R.id.iv_smartPhone_Product);
            tv_nameSmartPhone = itemView.findViewById(R.id.tv_nameSmartPhone_Product);
            tv_priceSmartPhone = itemView.findViewById(R.id.tv_priceSmartPhone_Product);
            cardView = itemView.findViewById(R.id.cv_smartPhone_Product);
        }
    }
}
