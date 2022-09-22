package virtualdevelopercloud.origin_app_v;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.google.zxing.Result;


import org.matomo.sdk.Tracker;
import org.matomo.sdk.TrackerBuilder;
import org.matomo.sdk.extra.MatomoApplication;
import org.matomo.sdk.extra.TrackHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


interface WebviewCallback {
    void startScan();
}

/**Clase Home:
 *
 * @author Uriel
 * @version 1.0
 */
public class Home extends AppCompatActivity implements ZXingScannerView.ResultHandler, WebviewCallback {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    private String id_usuario = "21";
    public static final String ERROR_DETECTED = "\n" + "No se detectó etiqueta NFC!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";


    /**Método solicitarPermiso
     *
     *Solicita al usuario permiso para utilizar la camara,
     *tener acceso a la galeria, utilizar el modulo NFC y
     *permanecer conectado a internet mientras se utiliza la aplicación.
     *
     * @param permiso
     * @param justificacion
     * Este parametro se utilizara para poner un cuadro de dialogo de
     * acuerdo al permiso que se este solicitando en el método onCreate.
     * @param requestCode
     * @param actividad
     */
    public void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    /** Método onRequestPermissionsResult
     *
     * Cuando la aplicación solicita permiso, el sistema le mostrará al usuario un cuadro de diálogo.
     * Cuando el usuario responde, el sistema llamará al método onRequestPermissionsResult() de la aplicación,
     * le pasará la respuesta del usuario y procesará la escena correspondiente.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Log.d("mmmm+231", "mmm");
        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Toast.makeText(this, "Se requiere el permiso para su correcto funcionamiento" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Probablemente el bug que se presenta al momento de apretar
     * el boton regresar se deba a este método, revisando developer.android
     * sugiere anular este metodo e invocar super.onBackPressed()
     *
     * Este metodo como tal controla los eventos del botón regresar.
     *
     */
    @Override
    public void onBackPressed() {
        Log.e("swipe", "dentro de regresar");
        // Do Here what ever you want do on back press;
    }

    /**Clase escanear
     **
     */
    public class escanear extends AsyncTask<Object, Object, Boolean> {
        private String text;
        private Home parent = null;

        /**Constructos de la clase escanear
         *
         * @param text
         * @param id_usuario
         * @param parent
         */
        public escanear(String text, String id_usuario, Home parent) {
            this.text = text;
            this.parent = parent;
        }


        @Override
        protected Boolean doInBackground(Object... params) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // call an external function as a result
            parent.scan();
        }

    }


    /**onCreate
     * En este metodo se inicializa el activity,
     * se define el tema que tendra y llama al xml.
     *
     * Se establece conexión con Matomo para generar reporte de datos.
     *
     * Define los permisos que serán solicitados al sistema al escanear.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
// The `Tracker` instance from the previous step
        Tracker tracker = ((MatomoApplication) getApplication()).getTracker();
// Track a screen view
        TrackHelper.track().screen("/home").title("Settings").with(tracker);
// Monitor your app installs
        TrackHelper.track().download().with(tracker);

/**
 * Si el método checkSelfPermission obtiene del manifest el valor 0 (PERMISSION_GRANTED)
 * podra acceder a la red de internet y a la camara del dispositivo.
 *
 * De lo contrario, si obtiene 1 (PERMISSION_DENIED) el dispositivo
 * presentara un cuadro de dialogo solicitando al usuario activar los permisos.
 *
 */
        SessionManager sm = new SessionManager(getApplicationContext());
        HandlerNFC();
        

        getSupportActionBar().hide();
        iniciar(getIntent().getIntExtra("escanear", 0));
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            solicitarPermiso(Manifest.permission.CAMERA, "Se requiere el permiso para escanear codigos",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            solicitarPermiso(Manifest.permission.INTERNET, "Esta aplicación funciona con señal de internet activa",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            solicitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE, "Esta aplicación solicita permiso de acceso a su galería",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_COARSE_LOCATION, "Esta aplicación solicita permiso de ubicacion",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION
                    , "Esta aplicación solicita permiso de ubicacion",
                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
        }

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {

        } else {
          isNFCActivated();
        }

    }

    public void isNFCActivated(){

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {

        } else {
           /* startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            Toast.makeText(this, "El modulo NFC esta desactivado", Toast.LENGTH_LONG).show(); */
            AlertDialog.Builder alerta = new AlertDialog.Builder(Home.this);
            alerta.setMessage("Por favor, habilite NFC. ¿Iniciar configuración?")
                    .setCancelable(false)
                    .setPositiveButton("Iniciar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("NFC está deshabilitado");
            titulo.show();
        }

    }

    private void HandlerNFC(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isNFCActivated();//llamamos nuestro metodo
                handler.postDelayed(this,10000);//se ejecutara cada 10 segundos
            }
        },190000);
    }

    WebView myWebView=null;

    /**Método iniciar
     *
     * En este metódo se llama al webview que se encuentra en el home.xml,
     * se toman sus configuraciones a través del metodo getSettings()
     * de la clase WebSettings.
     *
     * Después muestra el webView a través de su instancia (myWebView)
     * con el metodo setLoadWithOverviewMode();
     *
     * A partir de la linea 279 llama al lector de NFC local (nfcAdapter) y lo
     * obtiene a través del método getDefaultAdapter(), el cual debe recibir
     * un valor true para consultar en el Manifest si se ha otorgado permiso de
     * utilizar el lector.
     *
     * @param escanear
     */
    private void iniciar(int escanear) {

        myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setUseWideViewPort(true);
        // myWebView.addJavascriptInterface(new WebAppInterface(MainActivity.this,MainActivity.this), "Android");
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        String msg = "";
        Intent recibir = this.getIntent();
        readFromIntent(getIntent());
        SessionManager sm = new SessionManager(getApplicationContext());
         //Si no existe sesion en el sm permanecer en el registro.
        if (sm.getUser().length() == 0 || sm.getUser().replaceAll(""," ").compareTo("")==1 || sm.getUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        id_usuario = sm.getUser();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            // Toast.makeText(this, "Para validar productos originales necesitas  NFC en tu dispositivo.", Toast.LENGTH_LONG).show();

            msg = "Requieres un dispositivo NFC";

        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.NFC)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                solicitarPermiso(Manifest.permission.NFC, "Se requiere el permiso para lectura de etiquetas",
                        SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
            }
        }
        solicitarPermisoHacerLlamada();

        if (escanear == 1) {

            scan();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        /*Location location = getLastKnownLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
*/
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };

        myWebView.loadUrl("https://productracker.com/mOriginApp/index.php?msg="+msg+"&id_user="+sm.getUser());

        /*if(scan.quals("true")){
            scan();
        }*/


    }

    private Location getLastKnownLocation() {
        Location l=null;
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void ir(int i) {
        Log.d("swipe", "aaa--" + i);
        myWebView.loadUrl("javascript:swipe("+i+")");
    }

    //private Location loc;
    //private LocationManager locManager;

    /**Método LlamarCargarTag
     *
     *Toma los valores Strings que conforman al Tag
     * y los concatena para hacer una linea y acceder a la vista web
     * después lo valida en el método buildTagViews.
     */
    String code="";
    public void llamarCargarTag(final String txttag, final String id) {
        //LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Location location = getLastKnownLocation();
        //double latitude = location.getLatitude();
        //double longitude = location.getLongitude();

        if(code.compareTo(txttag) != 0){
            //Toast.makeText(this, "javascript:validarTag('" + txttag + "'," + id + ",'"+0.0+"@"+0.0+"')", Toast.LENGTH_SHORT).show();
            Log.d("logo", "------------------");

            myWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    myWebView.loadUrl("javascript:validarTag('" + txttag + "'," + id + ",'0.0@-0.0')");
                }
            });

            Log.d("logo", "------------------");
            // Toast.makeText(this, txttag, Toast.LENGTH_LONG).show();
            code= txttag;
        }
    }


    private void solicitarPermisoHacerLlamada() {



        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "OriginApp requiere permisos de Cámara", Toast.LENGTH_SHORT).show();
            }
        }*/


    }

    /**Método llamarFallo
     * C
     *
     */
    public void llamarFallo(){
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:cambioColor()");
            }
        });
    }

    public void detallesScan


            (final String code){
        setContentView(R.layout.home);
        iniciar(0);

    }

    public void cambiarImagen(){

        myWebView.loadUrl("javascript:cargarImagen('2')");

    }

    /**Clase WebAppInterface
     *
     * Inicializa la interfaz web dentro de la aplicacion
     * a través del webview.
     */

    class WebAppInterface {
        Home mCallback;
        /** Instantiate the interface and set the context */
        WebAppInterface(Home c) {
            mCallback = c;
        }
        /** Start an EPX scan from the web page */
        @JavascriptInterface
        public void scan() {
            Log.d("Escanear","checar nuestro scan");
            mCallback.startScan();
        }

        @JavascriptInterface
        public void abrirBrowser(String  url) {
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browse);
        }

        @JavascriptInterface
        public void subirimagen() {
            mCallback.processFile();
        }

        @JavascriptInterface
        public void cerrarSession() {
            SessionManager sm = new SessionManager(getApplicationContext());
            sm.LogIN();
            sm.logoutUser();
          // mCallback.processFile();
        }

        @JavascriptInterface
        public void salirdeOriginApp() {
            setResult(RESULT_OK);
            finish();
        }

        /**
         * Este método se encarga de compartir en redes sociales.
         */
        @JavascriptInterface
        public void compartirRS() {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Prueba la nueva aplicación OriginApp");
            startActivity(Intent.createChooser(intent, "Share with"));
        }




    }
    interface WebviewCallback {
        void startScan();
    }

    // Implement the ‘startScan()’ method defined by the WebviewCallback interface
    @Override
    public void startScan() {


            Intent intent = new Intent(this, Home.class);
            intent.putExtra("escanear", 1);
            startActivity(intent);


    }


    /**El metodo onKeyDown controla los eventos que suceden
     * cuando el usuario presiona el botón regresar.
     *
     *
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("back","dentrorr regresarr");

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something to hide the view
            /*AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
            manager.adjustVolume(AudioManager.ADJUST_LOWER, 0);*/
            return false;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            //Do something to hide the view
            /*AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
            manager.adjustVolume(AudioManager.ADJUST_RAISE, 0);*/
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {


        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if(zxingscannerview!=null) {
                zxingscannerview.stopCamera();
                startActivity(new Intent(this, Home.class));
                this.finish();
            }

        }
        return false;
    }


    public ZXingScannerView zxingscannerview = null;


    /**Este metodo obtiene información del codigo o etiqueta
     * a escanear a través de la clase ZXingScannerView.
     *
     */
    public void scan(){

        Log.d("Scaneando", "Starting Scan...");
        zxingscannerview= new ZXingScannerView(getApplicationContext());
        setContentView(zxingscannerview);
        zxingscannerview.setResultHandler(this);
        zxingscannerview.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            zxingscannerview.stopCamera();
        } catch (Exception e) {

        }

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    static String file_extn ="";

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                String filePath = getPath(uri);
                file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                Bitmap bitmap= BitmapFactory.decodeFile(filePath);
                Log.d("Subirimagen", filePath);


                new UploadFileAsync(filePath,this,id_usuario).execute();
                //Log.i(TAG, "Uri: " + uri.toString());
                //showImage(uri);
            }
        }
       if (resultCode != Activity.RESULT_OK) {
            try {

               // Toast.makeText(getApplicationContext(), "No se pudo obtener una respuesta", Toast.LENGTH_SHORT).show();
                String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");

                if (resultado != null) {
                    Toast.makeText(getApplicationContext(), "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show();
                }
                return;
            }catch(Exception e){
             //  Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {
                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Toast.makeText(getApplicationContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();
            }


        }
    }
    /******************************************************************************
     **********************************Subir imagen***************************
     ******************************************************************************/
    private class UploadFileAsync extends AsyncTask<String, Void, String> {
        private  String file;
        Home parent;
        private String us="";

        public UploadFileAsync(String file, Home home, String id_usuario) {
            this.file=file;
            this.parent=home;
            this.us=id_usuario;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String sourceFileUri = file;
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);
                if (sourceFile.isFile()) {
                    try {
                        String upLoadServerUri = "https://productracker.com/OriginApp/mod_webservices/upload.php?us="+us;
                        Log.d("Subirimagen", "Starting Scan...");
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + sourceFileUri + "\"" + lineEnd);
                        Log.d("Subirimagen", "2");
                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        Log.d("Subirimagen-dv", "--");
                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);
                        }
                        Log.d("Subirimagen-dv", "--");
                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();
                        Log.d("Subirimagen-RC", "--"+serverResponseCode);
                        if (serverResponseCode == 200) {


                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                        }
                        Log.d("Subirimagen", "3.");
                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
                return "end";
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            parent.cambiarImagen();
        }


    }

    /******************************************************************************
    **********************************Subir imagen***************************
    ******************************************************************************/

    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/

    /**Este metodo se encarga traducir los bytes que provienen
     * del tag a Hexadecimal.
     *
     *
     * @param src
     * @return
     */

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    /**Este método se encarga de lanzar el activity adecuado
     * en el momento es que se obtuvieron y procesaron los datos
     * del Tag.
     *
     * @param intent
     */
    private void readFromIntent(Intent intent) {


            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            String uid="";
            if(myTag!=null){
                uid = bytesToHexString(myTag.getId());
                Log.e("oneintent/", uid);


            }



        Log.e("oneintent/", "erros");
        Log.e("testeo", "part2");
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.e("testeo","part1.1");
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            Log.e("testeo","part1");
            buildTagViews(msgs,uid);
        }
    }

    private void buildTagViews(NdefMessage[] msgs,String uid) {
        String text = "";
        Log.e("testeo","part2.1");
        if (msgs == null || msgs.length == 0){
            llamarCargarTag("error-1-"+uid,id_usuario);
            //generarListadoMarcas(null);
            return;
        }
        try {

//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String sring1="UTF-8";
        String sring2="UTF-16";
        String textEncoding = ((payload[0] & 128) == 0) ? sring1 : sring2;
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        Log.e("testeo", "part4");
            Log.e("encoding", textEncoding);
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

        } catch (Exception e) {
            Log.e("testeo","part5");


            Log.e("UnsupportedEncoding", e.toString());
            llamarCargarTag(text+"error-1-"+uid,id_usuario);
            return;
        }

        //este el texto text); V1KDkrHEx2

        Log.e("UnsupportedEncoding", text+"--"+uid);
        llamarCargarTag(text+"-1-"+uid,id_usuario);

    }





    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }


    /**Notifica al hardware que hay un tag cerca del modulo
     * NFC del telefono a través del metodo ACTION_TAG_DISCOVERED
     *
     * Obtiene los bytes que contiene el byte a través de getByteArrayExtra
     * para que despues sean procesados en el método bytesToHexString.
     *
     * @param intent
     */

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){

            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            Log.e("oneintent/", uid.toString());

          Log.d("TAG", "tag ID = " + myTag.getId().toString());
        }

        Log.e("oneintent/", "erros");

    }


    public static boolean compruebaConexion(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;

    }
    @Override
    public void handleResult(Result result) {
        //Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        DetallesProducto dp =  new DetallesProducto();


        try {


            new Connection(result.getText(),this).execute();
            zxingscannerview.resumeCameraPreview(this);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*public void verDetalle(View view) {
        new Connection(this.text).execute();
    }*/
    public void validar(View view) {

        llamarCargarTag("14204724230119-","21");
    }


    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/



    public class Connection extends AsyncTask<Object, Object, String> {

        private  String text;
        Home parent;
        public Connection(String text,Home parent) {
            this.text=text;
            this.parent=parent;
        }

        @Override
        protected String doInBackground(Object... params) {

            try {
                return text;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
        @Override
        protected void onPostExecute(String text)
        {
            // call an external function as a result
            parent.detallesScan(text);
        }
    }


    private static final int GALLERY_REQUEST_CODE = 100;
    public void processFile() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }



}