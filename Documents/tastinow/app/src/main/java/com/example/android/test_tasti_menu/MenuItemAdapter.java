package com.example.android.test_tasti_menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.NumberViewHolder> {
    private static final String TAG = MenuItemAdapter.class.getSimpleName();

    private int mNumberItems;
    private String[] mMenuItems;

    public MenuItemAdapter(String[] items){
        mNumberItems = items.length;
        mMenuItems = items;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.list_menu_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        String oneItem;
        holder.bind(mMenuItems[position]);
    }

    public int getItemCount(){
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView listItem;
        public NumberViewHolder(View itemView){
            super(itemView);

            listItem = (TextView) itemView.findViewById(R.id.tv_menu_item);
        }

        void bind(String oneItem) {
            listItem.setText(oneItem);
        }
    }
}
