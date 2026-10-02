package xyz.app0773312;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String HOME_URL = "https://0773312.xyz/";
    private WebView webView;
    private LinearLayout loadingView;
    private LinearLayout errorView;
    private TextView errorText;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        loadingView = findViewById(R.id.loadingView);
        errorView = findViewById(R.id.errorView);
        errorText = findViewById(R.id.errorText);
        Button retry = findViewById(R.id.retryButton);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading();
            }

            @Override public void onPageFinished(WebView view, String url) {
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    showError("האתר אינו זמין כרגע. אפשר לנסות שוב בעוד רגע.");
                }
            }
        });

        retry.setOnClickListener(v -> loadHome());
        loadHome();
    }

    private void loadHome() {
        showLoading();
        webView.loadUrl(HOME_URL);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        errorText.setText(message);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
