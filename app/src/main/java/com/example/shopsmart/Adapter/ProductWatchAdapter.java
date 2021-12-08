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

public class ProductWatchAdapter extends RecyclerView.Adapter<ProductWatchAdapter.ViewHolder>{
    private Context context;
    private List<Product> productList;
    public ProductWatchAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_watch_product, null);
        ProductWatchAdapter.ViewHolder viewHolder = new ProductWatchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.productList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Picasso.get().load(product.getUrlImage()).error(R.drawable.ic_baseline_error_24).into(holder.iv_Watch);
        holder.tv_nameWatch.setText(product.getName());
        holder.tv_priceWatch.setText(decimalFormat.format(product.getPrice())+"₫");
        if (product.getActive() == 0) {
            holder.tv_activeWatch.setText("SẮP VỀ HÀNG");
        } else {
            holder.tv_activeWatch.setText("CÒN HÀNG");
        }
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Watch;
        TextView tv_nameWatch;
        TextView tv_priceWatch;
        TextView tv_activeWatch;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Watch = itemView.findViewById(R.id.iv_Watch_Product);
            tv_nameWatch = itemView.findViewById(R.id.tv_nameWatch_Product);
            tv_priceWatch = itemView.findViewById(R.id.tv_priceWatch_Product);
            tv_activeWatch = itemView.findViewById(R.id.tv_activeWatch_Product);
            cardView = itemView.findViewById(R.id.cv_Watch_Product);
        }
    }
}
