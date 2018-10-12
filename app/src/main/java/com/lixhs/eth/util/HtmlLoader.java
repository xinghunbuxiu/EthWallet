/**
 *
 */
package com.lixhs.eth.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 殷丽娟
 * @version v0.0.1
 * @company 创影科技
 * @date 2013-5-8
 */
public class HtmlLoader {
    public HashMap<String, SoftReference<Elements>> htmlCache = new HashMap<String, SoftReference<Elements>>();
    // 线程池：最大50条，每次执行：1条，空闲线程结束的超时时间：180秒
    private ExecutorService pool = Executors.newFixedThreadPool(10); // 固定线程池;
    private static volatile HtmlLoader htmlLoader;

    public Elements load(final String htmlUrl, final JsoupCallBack jsoupCallBack) {
        if (htmlCache.containsKey(htmlUrl)) {
            SoftReference<Elements> softReference = htmlCache.get(htmlUrl);
            Elements htmlCache = softReference.get();
            if (htmlCache != null) {
                return htmlCache;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                jsoupCallBack.success((Elements) message.obj, htmlUrl);
            }
        };
        // 用线程池来做下载图片的任务
        pool.submit(new Runnable() {
            @Override
            public void run() {
                Elements elements = loadHtmlFromUrl(htmlUrl);
                htmlCache.put(htmlUrl, new SoftReference<Elements>(elements));
                Message message = handler.obtainMessage(0, elements);
                handler.sendMessage(message);
            }
        });

        return null;
    }

    public void deleteCatch() {
        htmlCache.clear();
    }

    // 网络图片先下载到本地cache目录保存，以imagUrl的图片文件名保存。如果有同名文件在cache目录就从本地加载
    public static Elements loadHtmlFromUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements element = document.getElementById("ContentPlaceHolder1_divSummary").select("table").select("tbody").select("tr");
            ;
            return element;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface JsoupCallBack {
        public void success(Elements document, String url);
    }

    public static HtmlLoader get(Context context) {
        if (htmlLoader == null) {
            synchronized (Glide.class) {
                if (htmlLoader == null) {
                    htmlLoader = new HtmlLoader();
                }
            }

        }
        return htmlLoader;
    }
}
