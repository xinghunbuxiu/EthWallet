package com.lixhs.eth.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.lixh.base.BaseFragment;
import com.lixh.base.Page;
import com.lixh.base.adapter.recycleview.BaseViewHolder;
import com.lixh.bean.Message;
import com.lixh.rxhttp.Observable;
import com.lixh.utils.LoadingTip;
import com.lixh.utils.ULog;
import com.lixh.utils.UToast;
import com.lixh.view.UToolBar;
import com.lixhs.eth.R;
import com.lixhs.eth.bean.TaskListData;
import com.lixhs.eth.presenter.HomePresenter;
import com.lixhs.eth.util.HtmlLoader;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<HomePresenter> {
    Page.Builder builder;
    List<TaskListData> marketListData = new ArrayList<TaskListData>();
    HomePresenter presenter;
    Page page;

    @Override
    public void initTitle(UToolBar toolBar) {
        toolBar.setTitle("ETH");
        toolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = getPresenter();
        builder = new Page.Builder<TaskListData>(activity) {
            @Override
            public void onBindViewData(final BaseViewHolder viewHolder, int position, final TaskListData item) {
                viewHolder.setText(R.id.tv_txs, item.getTxsCode());
                viewHolder.setText(R.id.tv_account, item.getEthAccount());
                HtmlLoader.get(activity).load("https://etherscan.io/address/0x" + item.getEthAccount(), new HtmlLoader.JsoupCallBack() {
                    @Override
                    public void success(Elements document, String url) {
                        String balance = null;
                        try {
                            balance = document.text();
                            if (!balance.contains("0")) {
                                ULog.e("eth 余额", balance);
                                UToast.showShort(balance);
                            }
                            viewHolder.setText(R.id.tv_money, balance);
                        } catch (Exception e) {
                            ULog.e("eth 余额", item.getTxsCode());
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void onBindFooterViewData(BaseViewHolder headerView, int position) {

            }

            @Override
            public void onItemClick(View view, int position, TaskListData data) {

            }
        }.setAutoLoadMore(true).setRefresh(true).setDivideHeight(R.dimen.space_1).setLoadTip(tip);
        builder.setOnLoadingListener(onLoadingListener).setAutoRefresh(true);
        builder.setArrayAdapter(R.layout.rvitem_market_list, marketListData);
        page = builder.Build(Page.PageType.List);
        layout.setContentView(page.getRootView());
    }


    Page.OnLoadingListener onLoadingListener = new Page.OnLoadingListener() {
        @Override
        public void load(int page, final Page.OnLoadFinish onLoadFinish) {
            presenter.getTxsInfo(marketListData, page);
        }
    };

    public void finish() {
        page.finish(marketListData, LoadingTip.LoadStatus.FINISH);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void update(Observable o, Message arg) {

    }
}
