package com.wesmartclothing.tbra.base;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.constant.Key;

import butterknife.BindView;

/**
 * @Package com.wesmartclothing.tbra.base
 * @FileName BaseWeb2Fragment
 * @Date 2019/2/19 10:22
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BaseWeb2Fragment extends BaseAcFragment {


    @BindView(R.id.layout_web)
    FrameLayout mLayoutWeb;
    @BindView(R.id.progress_web)
    ProgressBar mProgressWeb;


    private String url;


    public static BaseWebFragment getInstance(String url) {
        BaseWebFragment webFragment = new BaseWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void initBundle(Bundle bundle) {
        url = bundle.getString(Key.BUNDLE_WEB_URL);
        initWebView();
    }

    private void initWebView() {
        WebView webView = new WebView(mContext);
        mLayoutWeb.addView(webView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressWeb.setVisibility(View.GONE);
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressWeb.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressWeb.setProgress(newProgress);
            }
        });

        WebSettings webSettings = webView.getSettings();

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.loadUrl(url);
    }


    public void onRelease() {
        mLayoutWeb.removeAllViews();
    }


    @Override
    public void onDestroyView() {
        onRelease();
        super.onDestroyView();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
