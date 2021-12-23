package com.example.shopsmart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopsmart.Entity.Category;
import com.example.shopsmart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryMenuStartAdapter extends BaseAdapter {
    private List<Category> categoryList;
    private Context context;

    public CategoryMenuStartAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater layoutInflater;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_menu_item_category, null);
            viewHolder.iv_itemMenuImage = convertView.findViewById(R.id.iv_item_menu);
            viewHolder.tv_itemMenuTitle = convertView.findViewById(R.id.tv_item_menu_title);
            viewHolder.tv_itemMenuDescription = convertView.findViewById(R.id.tv_item_menu_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category category = (Category) getItem(position);
        Picasso.get().load(category.getUrlImage()).placeholder(R.drawable.ic_baseline_home_24).error(R.drawable.ic_baseline_error_24).into(viewHolder.iv_itemMenuImage);
        viewHolder.tv_itemMenuTitle.setText(category.getTitle());
        viewHolder.tv_itemMenuDescription.setText(category.getDescription());
        return convertView;
    }

    public class ViewHolder {
        ImageView iv_itemMenuImage;
        TextView tv_itemMenuTitle;
        TextView tv_itemMenuDescription;
    }
}
