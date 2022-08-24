package virtualdevelopercloud.origin_app_v;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.matomo.sdk.Tracker;
import org.matomo.sdk.extra.MatomoApplication;
import org.matomo.sdk.extra.TrackHelper;


/**
 * Created by urieldeonati on 01/02/19.
 */
public class MainActivity extends AppCompatActivity  {

    private static double ltitud, longitud;
    private static String cd = "";
    private static String pais = "";
    private static String cp = "";
    private static String direccion = "";
    private static String estado = "";

    Context context;


    public void solicitarPermisos(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)) {
            Log.d("mmmm+0101011", "mmm");
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.d("mmmm+9999", "mmm");
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        } else {
            Log.d("mmmm+0000", "mmm");
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d("mmmm+111", "mmm");
        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("mmmm+", "mmm");
            } else {
                Log.d("mmmm+222", "mmm");
                //Toast.makeText(this, "Se requiere el permiso para su correcto funcionamiento" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    WebView myWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat); Log.e("idnew+", "1");
        super.onCreate(savedInstanceState);Log.e("idnew+", "2");
        setContentView(R.layout.activity_main);Log.e("idnew+", "3");



        getSupportActionBar().hide();Log.e("idnew+", "4");
        myWebView = (WebView) findViewById(R.id.webViewINi);Log.e("idnew+", "5");
        WebSettings webSettings = myWebView.getSettings();Log.e("idnew+", "6");
        webSettings.setJavaScriptEnabled(true);Log.e("idnew+", "7");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setUseWideViewPort(true);
        // myWebView.addJavascriptInterface(new WebAppInterface(MainActivity.this,MainActivity.this), "Android");
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");


        myWebView.setWebViewClient(new WebViewClient());


        String msg = "";
        Intent recibir = this.getIntent();

        SessionManager sm = new SessionManager(getApplicationContext());

        if (sm.getUser().length() > 0) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }

        myWebView.loadUrl("https://productracker.com/mOriginApp/A-Login.php");
    }

    //Se controla el evento al presionar el botón regresar
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
if (keyCode == event.KEYCODE_BACK){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("¿Desea salir de OriginApp?")
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
    builder.show();
}
        return false;
    }



    class WebAppInterface {
        MainActivity mCallback;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(MainActivity c) {
            mCallback = c;
        }

        /**
         * Start an EPX scan from the web page
         */


        @JavascriptInterface
        public void IngresarOriginApp(String iduser, String token) {
            Intent intent = new Intent(mCallback, Home.class);
            Log.e("idnew+", iduser);
            SessionManager sesm = new SessionManager(getApplicationContext());
            sesm.createLoginSession(iduser, iduser);
            startActivity(intent);

        }


    }
}
