package es.jagarciavi.photospat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PhotoSpat extends AppCompatActivity {

    private static final String TAG = "PhotoSpat";
    private WebView webview;
    private ProgressDialog progressDialog;
    private int counter = 0;
    private int firstLoad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_spat);

        webview = (WebView) findViewById(R.id.webview);

        final Activity activity = this;

        // Enable JavaScript and lets the browser go back
        webview.getSettings().setJavaScriptEnabled(true);
        webview.canGoBack();

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //counter = 0;
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                // Check to see if there is a progress dialog
                if (progressDialog == null) {
                    // If no progress dialog, make one and set message
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage(getResources().getString(R.string.loading));
                    progressDialog.show();

                    // Hide the webview while loading
                    webview.setEnabled(false);
                }
            }

            public void onPageFinished(WebView view, String url) {
                // Page is done loading;
                // hide the progress dialog and show the webview

                Log.i(TAG, "The app is in the onPageFinished method");

                String finalUrl = webview.getUrl().toString();

                Log.i(TAG, "finalurl string is " + finalUrl);

                if (finalUrl.equals("https://instagram.com/accounts/login/")) {
                    if(firstLoad == 1) {
                        Log.i(TAG, "The strings are equal, we'll set counter value to 4");
                        counter = 4;
                    } else {
                        Log.i(TAG, "It's not the load of the login page...");
                        firstLoad = 1;
                    }
                }

                if (progressDialog.isShowing() && counter == 4) {
                    Log.i(TAG, "On loop -> Dismiss progress dialog ");
                    progressDialog.dismiss();
                    // Improve this
                    //progressDialog = null;
                    webview.setEnabled(true);
                    counter = 0;
                }
                Log.i(TAG, "Counter value is " + counter);
                counter++;
            }

            public void onReceivedError(WebView view, int errorCod, String description, String failingUrl) {
                // Disable webview and progress dialog in app because of an error
                webview.setEnabled(false);
                setContentView(R.layout.activity_photo_spat);
                progressDialog.dismiss();
                Log.i(TAG, "Received error: " + errorCod + " " + description + " " + failingUrl);

                // Show dialog with error
                new AlertDialog.Builder(PhotoSpat.this)
                        .setTitle(getResources().getString(R.string.errorDialogTitle))
                                .setMessage(getResources().getString(R.string.errorDialogMessage))
                                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // The app will finish now
                                        finish();
                                    }
                                })
                                .show();
            }

        });


        webview.loadUrl("https://www.instagram.com/accounts/login");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_photo_spat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

