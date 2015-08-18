package com.w4lle.viewdraghelperdemo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.w4lle.viewdraghelperdemo.R;

/**
 * Created by w4lle on 15-8-18.
 * Copyright (c) 2015 Boohee, Inc. All rights reserved.
 */


public class ViewDragHelpNormalFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_drag_mormal, container, false);
    }
}
