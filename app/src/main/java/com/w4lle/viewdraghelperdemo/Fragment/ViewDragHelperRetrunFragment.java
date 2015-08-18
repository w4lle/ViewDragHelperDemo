package com.w4lle.viewdraghelperdemo.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.w4lle.viewdraghelperdemo.R;

/**
 * 带边界和自动回弹效果
 */
public class ViewDragHelperRetrunFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_drag_helper_retrun, container, false);
    }


}
