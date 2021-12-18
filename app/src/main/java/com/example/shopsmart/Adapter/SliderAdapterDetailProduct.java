package com.example.shopsmart.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shopsmart.Entity.ColorProduct;
import com.example.shopsmart.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapterDetailProduct extends SliderViewAdapter<SliderAdapterDetailProduct.Holder> {
    private List<ColorProduct> listColor;

    public SliderAdapterDetailProduct(List<ColorProduct> listColor) {
        this.listColor = listColor;
    }

    @Override
    public SliderAdapterDetailProduct.Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_product, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterDetailProduct.Holder viewHolder, int position) {
        ColorProduct product = listColor.get(position);
        Picasso.get().load(product.getUrlImage()).placeholder(R.drawable.ic_baseline_home_24).error(R.drawable.ic_baseline_error_24).into(viewHolder.iv_detailProduct);

    }

    @Override
    public int getCount() {
        return listColor.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView iv_detailProduct;

        public Holder(View itemView) {
            super(itemView);
            iv_detailProduct = itemView.findViewById(R.id.iv_DetailProduct);
        }
    }
}
