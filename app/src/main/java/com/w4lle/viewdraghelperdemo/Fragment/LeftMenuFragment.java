package com.w4lle.viewdraghelperdemo.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.w4lle.viewdraghelperdemo.R;

/**
 * Created by w4lle on 15-8-17.
 * Copyright (c) 2015 Boohee, Inc. All rights reserved.
 */

public class LeftMenuFragment extends ListFragment {

    private MenuItem[] mItems;

    private LeftMenuAdapter mAdapter;

    private LayoutInflater mInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = LayoutInflater.from(getActivity());

        MenuItem menuItem = null;
        String[] arrays = getResources().getStringArray(R.array.array_left_menu);
        mItems = new MenuItem[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            menuItem = new MenuItem(arrays[i], false);
            mItems[i] = menuItem;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(0xffffffff);
        setListAdapter(mAdapter = new LeftMenuAdapter(getActivity(), mItems));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mMenuItemSelectedListener != null) {
            mMenuItemSelectedListener.menuItemSelected(position, ((MenuItem) getListAdapter().getItem(position)).text);
        }
        mAdapter.setSelected(position);
    }


    //选择回调的接口
    public interface OnMenuItemSelectedListener {
        void menuItemSelected(int position, String title);
    }

    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }
}


class MenuItem {

    public MenuItem(String text, boolean isSelected) {
        this.text = text;
        this.isSelected = isSelected;
    }

    boolean isSelected;
    String text;
}


class LeftMenuAdapter extends ArrayAdapter<MenuItem> {
    private LayoutInflater mInflater;
    private int mSelected;

    public LeftMenuAdapter(Context context, MenuItem[] objects) {
        super(context, -1, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_left_menu, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        title.setText(getItem(position).text);
        convertView.setBackgroundColor(Color.TRANSPARENT);

        if (position == mSelected) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.state_menu_item_selected));
        }

        return convertView;
    }

    public void setSelected(int position) {
        this.mSelected = position;
        notifyDataSetChanged();
    }


}
