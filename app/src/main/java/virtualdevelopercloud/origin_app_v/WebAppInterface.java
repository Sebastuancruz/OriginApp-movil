package virtualdevelopercloud.origin_app_v;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by urieldeonati on 08/03/19.
 */
public class WebAppInterface extends AppCompatActivity{
    Context mContext;
    MainActivity ma;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c,MainActivity ma) {
        mContext = c;
        this.ma=ma;
    }



    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }




    public ZXingScannerView zxingscannerview = null;

@JavascriptInterface
    public void scan(){
    Log.e("reference",mContext.toString());

    Intent intent = new Intent(mContext, MainActivity.class);
    startActivity(intent);

    }


}
