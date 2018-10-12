package com.lixhs.eth.ui;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lixh.base.BaseActivity;
import com.lixh.view.LoadView;
import com.lixh.view.UToolBar;
import com.lixhs.eth.R;
import com.lixhs.eth.presenter.TabPresenter;
import com.lixhs.eth.ui.fragment.HomeFragment;
import com.lixhs.eth.ui.fragment.MyFragment;

/**
 * Created by LIXH on 2016/12/21.
 * email lixhVip9@163.com
 * des
 */
public class TabsActivity extends BaseActivity<TabPresenter> {


    @Override
    public boolean isShowBack() {
        return false;
    }

    @Override
    public boolean isDoubleExit() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initTitle(UToolBar toolBar) {

    }

    @Override
    public void initLoad(LoadView.Builder builder) {
        builder.swipeBack = false;
        builder.hasToolbar = false;
        builder.hasBottomBar = true;
        builder.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页").setActiveColorResource(R.color.colorAccent)
                , new BottomNavigationItem(R.mipmap.ic_launcher, "我的").setActiveColorResource(R.color.blue));
        builder.addFragment(new HomeFragment(), new MyFragment());
        builder.setOnTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

}
