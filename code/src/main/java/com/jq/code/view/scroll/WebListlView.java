package com.jq.code.view.scroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jq.code.R;
import com.jq.code.view.FillListView;
import com.jq.code.view.FooterLoadingView;
import com.jq.code.view.text.CustomTextView;

/**
 * Created by hfei on 2016/5/26.
 */
public class WebListlView extends ScrollView {

    private boolean isLoading = false;
    private boolean isPageFinished = false;
    private OnLoadListener mOnLoadListener;
    private LinearLayout parent;
    private WebView webView;
    private FillListView listView;
    private LinearLayout line;
    private FooterLoadingView mListViewFooter;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private static final String TAG = "WebListlView";
    private BaseAdapter mAdapter;

    public WebListlView(Context context) {
        super(context);
    }

    public WebListlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);

    }

    private void init(Context context) {
        parent = new LinearLayout(context);
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        initWebView(context);
        parent.addView(webView);
        initLine(context);
        parent.addView(line);
        initListView(context);
        parent.addView(listView);
        addView(parent);
        mListViewFooter = new FooterLoadingView(context);
        mListViewFooter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 分割线
     *
     * @param context
     */
    private void initLine(Context context) {
        line = new LinearLayout(context);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        line.setPadding(0, 20, 0, 20);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER);
        line.setVisibility(View.INVISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 1, 1);
        View view1 = new View(context);
        view1.setLayoutParams(params);
        view1.setBackgroundColor(context.getResources().getColor(R.color.gray));

        View view2 = new View(context);
        view2.setLayoutParams(params);
        view2.setBackgroundColor(context.getResources().getColor(R.color.gray));

        CustomTextView text = new CustomTextView(context);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setPadding(4, 0, 4, 0);
        text.setTextSize(36);
        text.setTextColor(context.getResources().getColor(R.color.text_gray));
        text.setTypeface(CustomTextView.LTEX);
        text.setText("留言");

        line.addView(view1);
        line.addView(text);
        line.addView(view2);
    }

    /**
     * list显示栏
     *
     * @param context
     */
    private void initListView(Context context) {
        listView = new FillListView(context);
        listView.setFocusable(false);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 内容
     *
     * @param context
     */
    private void initWebView(Context context) {
        webView = new WebView(context);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
//            webView.getSettings().setCacheMode(
//                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = context.getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        // 设置 Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebClient());

    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        mAdapter = null;
        mAdapter = adapter;
        listView.setAdapter(adapter);
        if (mAdapter.getCount() > 0) {
            line.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载url
     *
     * @param url
     */
    public void loadUrl(String url) {
        Log.i(TAG, "URL: " + url);
        webView.loadUrl(url);
    }

    /**
     * 滚动到list头部
     */
    public void srollToListHead() {
        int height = webView.getMeasuredHeight() + line.getMeasuredHeight() - 300;
        smoothScrollTo(0, height);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        parent = (LinearLayout) getChildAt(0);
        int d = parent.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0 && !isLoading) {
            isLoading = true;
            loadData();
        } else
            super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading(isLoading);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            parent.removeView(mListViewFooter);
            parent.addView(mListViewFooter);
        } else {
            parent.removeView(mListViewFooter);
        }
    }

    public void setLoadOver() {
        isLoading = false;
        mListViewFooter.reLoadOver("全部加载完了");
        parent.removeView(mListViewFooter);
        parent.addView(mListViewFooter);
    }

    class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG,"onPageFinished");
            isPageFinished = true;
            listView.setAdapter(mAdapter);
            if(webViewClientListener != null){
                webViewClientListener.onPageFinished(view,url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            if(webViewClientListener != null){
                webViewClientListener.onReceivedError(view,errorCode,description,failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView view,
                                       SslErrorHandler handler, SslError error) {
            // handler.cancel(); //默认的处理方式，WebView变成空白页
            handler.proceed();// 接受证书
            // handleMessage(Message msg); //其他处理
        }
    }
    public WebViewClientListener webViewClientListener ;
    public void setWebViewClientListener(WebViewClientListener listener){
        this.webViewClientListener = listener ;
    }
    public interface WebViewClientListener{
       void onPageFinished(WebView view, String url) ;
       void onReceivedError(WebView view, int errorCode,
                             String description, String failingUrl) ;

    }
    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface OnLoadListener {
        /**
         *上拉加载
         */
        void onLoad();
    }
}