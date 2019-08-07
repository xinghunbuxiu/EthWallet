package com.lixhs.eth.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.lixh.base.BaseFragment;
import com.lixh.bean.Message;
import com.lixh.rxhttp.Observable;
import com.lixh.utils.SharedPreferencesUtil;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment<HomePresenter> {
    List<TaskListData> marketListData = new ArrayList<TaskListData>();
    HomePresenter presenter;
    int page;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.wakuang)
    Button wakuang;
    boolean isStart;

    @Override
    public void initTitle(UToolBar toolBar) {
        toolBar.setTitle("ETH");
        toolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = getPresenter();
    }

    public void finish() {
        getEthAcount(0);
    }

    private void getEthAcount(int index) {
        ULog.e("第几页", index + "");
        if (!isStart) {
            return;
        }
        if (index == marketListData.size()) {
            page++;
            startWakuang(page);
            return;
        }
        TaskListData data = marketListData.get(index);
        String eth = "https://api-ropsten.etherscan.io/api?module=account&action=balance&address=0x";
        HtmlLoader.get(activity).load(eth + data.getEthAccount(), (document, url) -> {
            ULog.e("第几个", index + "");
            String balance = null;
            try {
                balance = document.text();
                JSONObject result = JSON.parseObject(balance);
                if (!result.getString("result").equals("0")) {
                    ULog.e("eth 余额", balance);
                    UToast.showShort(balance);
                    SharedPreferencesUtil.getInstance().putString("Eth:" + data.getEthAccount(), data.getTxsCode());
                }
                tvBalance.setText(index + "--\n" + data.getTxsCode() + "--\n" + balance);
                getNext(index);
            } catch (Exception e) {
                ULog.e("eth 余额", data.getTxsCode());
                e.printStackTrace();
            }
        });
    }

    private void getNext(int index) {
        index = index + 1;
        getEthAcount(index);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_eth_task;
    }

    @Override
    public void update(Observable o, Message arg) {

    }

    void startWakuang(int page) {
        wakuang.setText("第" + page + "页" + (isStart ? "开始" : "暂停"));
        marketListData.clear();
        presenter.getTxsInfo(marketListData, page);
    }

    @OnClick(R.id.wakuang)
    public void onViewClicked() {
        isStart = !isStart;
        startWakuang(page);
    }
}
