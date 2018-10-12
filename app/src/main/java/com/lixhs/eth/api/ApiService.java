package com.lixhs.eth.api;


import com.lixh.base.BaseResPose;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by helin on 2016/10/9 17:09.
 */

public interface ApiService {

    @GET("/txs")
    Observable<BaseResPose<String>> getTxsInfo(@Query("p") int p);

}
