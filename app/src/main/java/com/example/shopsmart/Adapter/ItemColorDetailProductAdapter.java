package com.example.shopsmart.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.Interface.ItemClickListenerColor;
import com.example.shopsmart.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ItemColorDetailProductAdapter extends RecyclerView.Adapter<ItemColorDetailProductAdapter.Holder> {
    private Context context;
    private List<ColorProduct> colorProducts;
    private static ItemClickListenerColor itemClickListenerColor;

    public ItemColorDetailProductAdapter(Context context, List<ColorProduct> colorProducts) {
        this.context = context;
        this.colorProducts = colorProducts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListenerColor itemClickListenerColor) {
        ItemColorDetailProductAdapter.itemClickListenerColor = itemClickListenerColor;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_color_detail_product, null);
        ItemColorDetailProductAdapter.Holder holder = new ItemColorDetailProductAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ColorProduct colorProduct = colorProducts.get(position);
        if (colorProduct.getCodeColor().length() != 0) {
            Log.d("sizeColor", colorProduct.getCodeColor());
            holder.si_Color.setVisibility(View.VISIBLE);
            holder.si_Color.setBackgroundColor(Color.parseColor(colorProduct.getCodeColor()));
            holder.position = position;
            holder.idImage=colorProduct.getIdColor();
        } else {
            holder.si_Color.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return this.colorProducts.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ShapeableImageView si_Color;
        int position;
        int idImage;
        public Holder(@NonNull View itemView) {
            super(itemView);
            si_Color = itemView.findViewById(R.id.si_Color);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (itemClickListenerColor != null){
                    itemClickListenerColor.onClick(v,position,idImage);
                }
                }
            });
        }
    }
}
