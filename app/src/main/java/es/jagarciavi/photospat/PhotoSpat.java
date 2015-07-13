package es.jagarciavi.photospat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PhotoSpat extends AppCompatActivity {

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
                    progressDialog.setMessage("Cargando, por favor, espere...");
                    progressDialog.show();

                    // Hide the webview while loading
                    webview.setEnabled(false);
                }
            }

            public void onPageFinished(WebView view, String url) {
                // Page is done loading;
                // hide the progress dialog and show the webview

                Log.i(null, "The app is in the onPageFinished method");

                String finalUrl = webview.getUrl().toString();

                Log.i(null, "finalurl string is " + finalUrl);

                if (finalUrl.equals("https://instagram.com/accounts/login/")) {
                    if(firstLoad == 1) {
                        Log.i(null, "The strings are equal, we'll set counter value to 4");
                        counter = 4;
                    } else {
                        Log.i(null, "It's not the load of the login page...");
                        firstLoad = 1;
                    }
                }

                if (progressDialog.isShowing() && counter == 4) {
                    Log.i(null, "On loop -> Dismiss progress dialog ");
                    progressDialog.dismiss();
                    //progressDialog = null;
                    webview.setEnabled(true);
                    counter = 0;
                }
                Log.i(null, "Counter value is " + counter);
                counter++;
            }

            public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
                Toast.makeText(PhotoSpat.this, "Your Internet Connection May not be active Or " + description, Toast.LENGTH_LONG).show();
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

