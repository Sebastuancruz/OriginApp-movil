package virtualdevelopercloud.origin_app_v;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Calendar;

/**
 * Created by urieldeonati on 08/02/19.
 */
public class Registrarse extends AppCompatActivity {
    private static double ltitud,longitud;
    private static String  cd ="";
    private static String  pais ="";
    private static String  cp ="";
    private static String  direccion ="";
    private static String estado="";
    EditText tel=null;
    EditText alias=null;
    EditText registro_contrasena=null;
    EditText registro_contrasena2=null;
    EditText registro_fecha=null;
    EditText registro_mail=null;
    RadioGroup sexo=null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioGroup viewById;

    public void solicitarPermisos(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            Log.d("mmmm+0101011", "mmm");
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.d("mmmm+9999", "mmm");
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
                        }})
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
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("mmmm+", "mmm");
            } else {
                Log.d("mmmm+222", "mmm");
                //Toast.makeText(this, "Se requiere el permiso para su correcto funcionamiento" , Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registrarse);








         //tel = (EditText) findViewById(R.id.telep);


        //tel.setText("+52");
         alias = (EditText) findViewById(R.id.alias);
         registro_contrasena = (EditText) findViewById(R.id.registro_contrasena);
         registro_contrasena2 = (EditText) findViewById(R.id.registro_contrasena2);
         registro_fecha = (EditText) findViewById(R.id.registro_fecha);
         registro_mail = (EditText) findViewById(R.id.registro_mail);


        sexo= viewById;
        registro_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( Registrarse.this, android.R.style.Theme, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = year+ "-" +month + "-" + day ;
                registro_fecha.setText(date);
            }
        };


    }

    public class Connection extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DetallesProducto dp= new DetallesProducto();
            try {

                /*tel;
                alias;
                registro_contrasena;
                registro_contrasena2;
                registro_fecha;
                registro_mail;*/


                int r = dp.setUsuario(alias.getText().toString(), registro_mail.getText().toString(), registro_fecha.getText().toString(), registro_contrasena.getText().toString(), "2721113456", ltitud, longitud, cd + " " + pais + " " + cp + " " + direccion + " " + estado, sexo.getCheckedRadioButtonId());

                if(r==1){

                }
                if(r==0){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void registrar(View view) {
        new Connection().execute();


    }
}
