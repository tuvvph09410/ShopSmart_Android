package com.example.shopsmart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsmart.Entity.CapacityProduct;
import com.example.shopsmart.Interface.ItemClickListenerCapacity;
import com.example.shopsmart.Interface.ItemClickListenerColor;
import com.example.shopsmart.R;

import java.util.List;

public class ItemCapacityDetailProducAdapter extends RecyclerView.Adapter<ItemCapacityDetailProducAdapter.ViewHolder> {
    private Context context;
    private List<CapacityProduct> capacityProductList;
    private static ItemClickListenerCapacity itemClickListenerCapacity;

    public ItemCapacityDetailProducAdapter(Context context, List<CapacityProduct> capacityProductList) {
        this.context = context;
        this.capacityProductList = capacityProductList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListenerCapacity itemClickListenerCapacity) {
        ItemCapacityDetailProducAdapter.itemClickListenerCapacity = itemClickListenerCapacity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_capacity_detail_product, null);
        ItemCapacityDetailProducAdapter.ViewHolder holder = new ItemCapacityDetailProducAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CapacityProduct capacityProduct = capacityProductList.get(position);
        holder.tvItemCapacity.setText(capacityProduct.getCapacity());
        holder.position = position;
        holder.price = capacityProduct.getPrice();
    }

    @Override
    public int getItemCount() {
        return this.capacityProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemCapacity;
        int position;
        int price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemCapacity = itemView.findViewById(R.id.tvItemCapacity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListenerCapacity != null) {
                        itemClickListenerCapacity.onClick(v, position, price);
                    }
                }
            });
        }
    }
}
