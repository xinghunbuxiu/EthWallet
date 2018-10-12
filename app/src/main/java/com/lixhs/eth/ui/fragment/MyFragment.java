package com.lixhs.eth.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.lixh.base.BaseFragment;
import com.lixh.view.UToolBar;
import com.lixhs.eth.R;


public class MyFragment extends BaseFragment {

    @Override
    public void initTitle(UToolBar toolBar) {
        toolBar.setTitle("我");
        toolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

}
