package com.lixhs.eth.bean;


import com.lixh.bean.BaseModel;

/**
 * Created by LIXH on 2018/10/10.
 * email lixhVip9@163.com
 * des
 */

public class TaskListData extends BaseModel {

    String txsCode;
    String ethAccount;
    String ethMoney;

    public String getEthAccount() {
        return this.ethAccount;
    }

    public void setEthAccount(String ethAccount) {
        this.ethAccount = ethAccount;
    }

    public String getTxsCode() {
        return this.txsCode;
    }

    public void setTxsCode(String txsCode) {
        this.txsCode = txsCode;
    }

    public String getEthMoney() {
        return this.ethMoney;
    }

    public void setEthMoney(String ethMoney) {
        this.ethMoney = ethMoney;
    }
}
