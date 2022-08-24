package virtualdevelopercloud.origin_app_v;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import virtualdevelopercloud.origin_app_v.plainObjects.MarcasModel;
import virtualdevelopercloud.origin_app_v.plainObjects.Perfil;


public class DetallesProducto {

    public String nombre;
    public int error=3;
    private String descripcion;
    List<Lista> lista = new ArrayList<>();
    public String getDescripcion() {
        return descripcion;
    }

    public Perfil getMiPerfil(String var) throws Exception {
        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("https://virtualdevelopercloud.com/OriginApp/mod_webservices/VerPerfil.php?id=" + URLEncoder.encode(var, "UTF-8")).openConnection();
        Log.e("testeo","https://virtualdevelopercloud.com/OriginApp/mod_webservices/VerPerfil.php?code=" + URLEncoder.encode(var, "UTF-8"));
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }


        JSONObject object = new JSONObject(string); //Creamos un objeto JSON a partir de la cadena

        Log.e("testeo",string);
        Perfil p=new Perfil();
        p.setCelular(object.getString("celular"));
        p.setEmail(object.getString("email"));
        p.setFacebook(object.getString("Facebook"));
        p.setFechnacimiento(object.getString("fechnacimiento"));
        p.setImg(object.getString("img"));
        p.setNombre(object.getString("nombre"));
        p.setSexo(object.getString("sexo"));
        return p;
    }



    public String getNombre() {
        return nombre;
    }


    public int getPostsForTag(String var,String id_usuario,int almacenar) throws Exception {
        String var1="";
        if(var.length() > 13){
             var1 = var.substring((var.length()-14),var.length() );
            HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/validateProduct.php?code=" + URLEncoder.encode(var1, "UTF-8")+"&id_usuario="  + URLEncoder.encode(id_usuario, "UTF-8")+"&guardar="+almacenar).openConnection();
            Log.e("testeo","http://virtualdevelopercloud.com/OriginApp/mod_webservices/validateProduct.php?code=" + URLEncoder.encode(var1, "UTF-8")+"&id_usuario="  + URLEncoder.encode(id_usuario, "UTF-8"));
            try {
                httpUrlConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {

                e.printStackTrace();
            }
            httpUrlConnection.setDoInput(true);
            try {
                httpUrlConnection.connect();
                Log.e("testeo", "hhhh");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("testeo","qqq");
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String string = "";
            String s = null;
            while ((s = in.readLine()) != null) {
                string += s;
            }
            Log.e("testeo",string);
            return scaningJSON(string);
        }else {
            return 1;
        }

    }




    public int getLogin(String usr, String pas, double latitud, double longitud, String cd, String pais, String zip, String dir, String estado)  throws Exception {
        usr = "us=" + URLEncoder.encode(usr, "UTF-8");
        pas = "&ps=" + URLEncoder.encode(pas, "UTF-8");
        String lat = "&lat=" + latitud;
        String lon = "&lon=" + longitud;
        cd = "&cd=" + URLEncoder.encode(cd, "UTF-8");
        pais = "&pais=" + URLEncoder.encode(pais, "UTF-8");
        zip = "&zip=" + URLEncoder.encode(zip, "UTF-8");
        dir = "&dir=" + URLEncoder.encode(dir, "UTF-8");
        estado = "&estado=" + URLEncoder.encode(estado, "UTF-8");
        String  params = usr+pas+lat+lon+cd+pais+zip+dir+estado;
        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL(("http://virtualdevelopercloud.com/OriginApp/mod_webservices/Login.php?" + params).trim()).openConnection();
        Log.e("link","http://virtualdevelopercloud.com/OriginApp/mod_webservices/Login.php?" + params);

         try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {

            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
            Log.e("testeo", "hhhh");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("testeo","qqq");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }
        JSONObject object = new JSONObject(string); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        descripcion=object.getString("descripcion");
        Log.e("testeo",descripcion);
        Log.e("testeo",error+"");
        return error;

    }


    public int getPosts(String var, double latitud, double longitud, String cd, String pais, String zip, String dir, String estado) throws Exception {
        String lat = "&lat=" + latitud;
        String lon = "&lon=" + longitud;
        cd = "&cd=" + URLEncoder.encode(cd, "UTF-8");
        pais = "&pais=" + URLEncoder.encode(pais, "UTF-8");
        zip = "&zip=" + URLEncoder.encode(zip, "UTF-8");
        dir = "&dir=" + URLEncoder.encode(dir, "UTF-8");
        estado = "&estado=" + URLEncoder.encode(estado, "UTF-8");
        String  params = lat+lon+cd+pais+zip+dir+estado;
        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/ViewProductDetail.php?code=" + URLEncoder.encode(var, "UTF-8")+params).openConnection();
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }

        return scaningJSON(string);

    }

    List<Producto> lista_productos = new ArrayList<>();


    public List<Producto> getLista_productos() {
        return lista_productos;
    }

    public int scaningJSON(String json) throws JSONException {

        JSONObject object = new JSONObject(json); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        JSONArray json_array = object.optJSONArray("list"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
        if(json_array != null) {
            Log.e("testeo", json_array.length() + "");
            for (int i = 0; i < json_array.length(); i++) {
                lista_productos.add(new Producto(json_array.getJSONObject(i))); //creamos un objeto Fruta y lo insertamos en la lista
            }
        }
        return error;
    }

    public int getHitorial(String id_usuario) throws JSONException, IOException {
        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/historial.php?idusuario=" + id_usuario).openConnection();
        Log.e("hasta aqui","http://virtualdevelopercloud.com/OriginApp/mod_webservices/historial.php?idusuario=" + id_usuario);
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }
        Log.e("Logeaaa",string);
        return scaningJSONList(string);
    }

    public int scaningJSONList(String json) throws JSONException {

        JSONObject object = new JSONObject(json); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        JSONArray json_array = object.optJSONArray("list"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
        if(json_array != null) {

            for (int i = 0; i < json_array.length(); i++) {
                lista.add(new Lista(json_array.getJSONObject(i))); //creamos un objeto Fruta y lo insertamos en la lista
            }
        }
        return error;
    }

    public List<Lista> getLista() {
        return lista;
    }

    public int getDetallePorIDproducto(String idproducto) throws JSONException, IOException {

        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/VerdetallesProductoId.php?code=" + URLEncoder.encode(idproducto, "UTF-8")).openConnection();
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }

        return scaningJSON(string);
    }


    public int setUsuario(String alias, String registro_mail, String registro_fecha, String registro_contrasena, String tel, double ltitud, double longitud, String direccion, int sexo) throws Exception {
            /*id="";
	$nombre="";
	$constrasena="";
	$email="";
	$direccion="";
	$latitud=0;
	$longitud=0;
	$bloqueo=0;
	$conectado=0;
	$hash="";
	$sexo=0;
	$fechnacimiento="2000-01-01";
	$ext="";
		$telefono="";
	*/
        String sexo1= "&sexo=" +sexo;
        alias = "nombre=" + URLEncoder.encode(alias, "UTF-8");
        registro_contrasena = "&constrasena=" + URLEncoder.encode(registro_contrasena, "UTF-8");
        registro_mail = "&email=" + URLEncoder.encode(registro_mail, "UTF-8");
        direccion = "&direccion=" + URLEncoder.encode(direccion, "UTF-8");
        registro_fecha = "&fechnacimiento=" + URLEncoder.encode(registro_fecha, "UTF-8");
        tel = "&telefono=" + URLEncoder.encode(tel, "UTF-8");
        String lat = "&latitud=" +ltitud;
        String lon = "&longitud=" +longitud;



        String res=alias+registro_contrasena+registro_mail+direccion+registro_fecha+tel+lat+lon+direccion+sexo1;




        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/createUser.php?" + res).openConnection();
        Log.e("cadena","http://virtualdevelopercloud.com/OriginApp/mod_webservices/createUser.php?" + res);

        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }
        return scaningJSON(string);
    }

    public ArrayList<Coordenadas> getTiendasPorNombre(String nombre) throws Exception {


        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/buscarTiendasPorNombre.php?nombre=" + URLEncoder.encode(nombre, "UTF-8")).openConnection();
        Log.e("dentro bus","http://virtualdevelopercloud.com/OriginApp/mod_webservices/buscarTiendasPorNombre.php?nombre=" + URLEncoder.encode(nombre, "UTF-8"));
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }

        //return scaningJSON(string);
        ArrayList<Coordenadas> coo = new ArrayList<Coordenadas>();
        JSONObject object = new JSONObject(string); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        if(error==1){
            return null;
        }
        JSONArray json_array = object.optJSONArray("list"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
        if(json_array != null) {
            Log.e("testeo", json_array.length() + "");
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject ajo = json_array.getJSONObject(i);
                Coordenadas coordenadas= new Coordenadas();
                //ajo.getInt("id");
                coordenadas.setTitulo(ajo.getString("nombre"));
                //ajo.getString("direccion");
                coordenadas.setLatitud(ajo.getDouble("latitud"));
                coordenadas.setLongitud(ajo.getDouble("longitud"));
                coo.add(coordenadas);
                //ajo.getString("img");

            }
        }

        return coo;
    }

    public ArrayList<Coordenadas> getTiendasPorId_Marca(String id_marca)throws Exception {


        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/VerdetallesProductoId.php?id_marca="+id_marca).openConnection();
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }

        //return scaningJSON(string);
        ArrayList<Coordenadas> coo = new ArrayList<Coordenadas>();
        JSONObject object = new JSONObject(string); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        if(error==1){
            return null;
        }
        JSONArray json_array = object.optJSONArray("list"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
        if(json_array != null) {
            Log.e("testeo", json_array.length() + "");
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject ajo = json_array.getJSONObject(i);
                Coordenadas coordenadas= new Coordenadas();
                //ajo.getInt("id");
                coordenadas.setTitulo(ajo.getString("nombre"));
                //ajo.getString("direccion");
                coordenadas.setLatitud(ajo.getDouble("latitud"));
                coordenadas.setLongitud(ajo.getDouble("longitud"));
                coo.add(coordenadas);
                //ajo.getString("img");

            }
        }

        return coo;
    }

    public ArrayList<MarcasModel> getMisMarcas(String id_usuario) throws Exception{
        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/getMarcas.php?iduser=" + id_usuario).openConnection();
        Log.e("hasta aqui", "http://virtualdevelopercloud.com/OriginApp/mod_webservices/getMarcas.php?iduser=" + id_usuario);
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }
        Log.e("testeo", string);
        ArrayList<MarcasModel> m = new ArrayList<MarcasModel>();
        JSONObject object = new JSONObject(string); //Creamos un objeto JSON a partir de la cadena
        error = object.getInt("error");
        if(error==1){
            return null;
        }
        JSONArray json_array = object.optJSONArray("list");
        if(json_array != null) {
            Log.e("testeo", json_array.length() + "");
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject ajo = json_array.getJSONObject(i);
                MarcasModel marca= new MarcasModel();
                marca.setId(ajo.getString("id"));
                marca.setImg(ajo.getString("img"));
                marca.setNombre(ajo.getString("nombre"));
                m.add(marca);
                //ajo.getString("img");

            }
        }
        return m;

    }

    public int getMisProductos(String id_usuario, String idmarca) throws Exception {
        id_usuario = "idusuario="+id_usuario;
        idmarca = "&idmarca="+idmarca;

        HttpURLConnection httpUrlConnection = (HttpURLConnection)  new URL("http://virtualdevelopercloud.com/OriginApp/mod_webservices/getMisProductosPorMarca.php?" + id_usuario+idmarca).openConnection();
        Log.e("hasta aqui","http://virtualdevelopercloud.com/OriginApp/mod_webservices/getMisProductosPorMarca.php?" + id_usuario+idmarca);
        try {
            httpUrlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpUrlConnection.setDoInput(true);
        try {
            httpUrlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("hasta aqui","ssss");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        String string = "";
        String s = null;
        while ((s = in.readLine()) != null) {
            string += s;
        }
        Log.e("Logeaaa",string);
        return scaningJSONList(string);
    }


    public class Lista{

        int id=0;
        String nombre="";
        String descripcion="";
        String  img="";
        String fechaexpiracion="";
        String fechaproduccion="";

        public Lista(JSONObject objetoJSON) throws JSONException {
            this.id=objetoJSON.getInt("id");
            this.nombre=objetoJSON.getString("nombre");
            this.descripcion=objetoJSON.getString("descripcion");
            this.img=objetoJSON.getString("img");
            this.fechaexpiracion=objetoJSON.getString("fechaexpiracion");
            this.fechaproduccion=objetoJSON.getString("fechaproduccion");
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getImg() {
            return img;
        }

        public String getFechaexpiracion() {
            return fechaexpiracion;
        }

        public String getFechaproduccion() {
            return fechaproduccion;
        }
    }


    public class Producto{

        String promos;
        String id="";
        String nombremarca="";
        String nombreproducto="";
        String descripcion="";
        String con_neto="";
        String ingredientes="";
        String valor_nutricional="";
        String instrucciondesecho="";
        String precionpublico="";
        String precauciones="";
        String instrucciones="";
        String infoadicional="";
        String puntos="";
        String fotografias="";
        String fb="";
        String twitter="";
        String instagram="";
        String youtube="";
        String linkedin="";
        String campos="";


        public Producto(JSONObject objetoJSON) throws JSONException {
            this.id=objetoJSON.getString("id");
            this.nombremarca=objetoJSON.getString("nombremarca");
            this.nombreproducto=objetoJSON.getString("nombreproducto");
            this.descripcion=objetoJSON.getString("descripcion");
            this.con_neto=objetoJSON.getString("con_neto");
            this.ingredientes=objetoJSON.getString("ingredientes");
            this.valor_nutricional=objetoJSON.getString("valor_nutricional");
            this.instrucciondesecho=objetoJSON.getString("instrucciondesecho");
            this.precionpublico=objetoJSON.getString("precionpublico");
            this.precauciones=objetoJSON.getString("precauciones");
            this.instrucciones=objetoJSON.getString("instrucciones");
            this.infoadicional=objetoJSON.getString("infoadicional");
            this.puntos=objetoJSON.getString("puntos");
            this.fb=objetoJSON.getString("fb");
            this.twitter=objetoJSON.getString("twitter");
            this.instagram=objetoJSON.getString("instagram");
            this.youtube=objetoJSON.getString("youtube");
            this.linkedin=objetoJSON.getString("linkedin");
            this.campos=objetoJSON.getString("campos");
            this.fotografias=objetoJSON.getString("fotografias");
            this.promos=objetoJSON.getString("promos");

        }

        public String getPromos() {return promos;}

        public String getFb() {return fb;}

        public String getTwitter() {return twitter;}

        public String getInstagram() {return instagram;}

        public String getYoutube() {return youtube;}

        public String getLinkedin() {return linkedin;}

        public String getCampos() {return campos;}

        public String getFotografias() {
            return fotografias;
        }

        public void setFotografias(String fotografias) {
            this.fotografias = fotografias;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombremarca() {
            return nombremarca;
        }

        public void setNombremarca(String nombremarca) {
            this.nombremarca = nombremarca;
        }

        public String getNombreproducto() {
            return nombreproducto;
        }

        public void setNombreproducto(String nombreproducto) {
            this.nombreproducto = nombreproducto;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getCon_neto() {
            return con_neto;
        }

        public void setCon_neto(String con_neto) {
            this.con_neto = con_neto;
        }

        public String getIngredientes() {
            return ingredientes;
        }

        public void setIngredientes(String ingredientes) {
            this.ingredientes = ingredientes;
        }

        public String getValor_nutricional() {
            return valor_nutricional;
        }

        public void setValor_nutricional(String valor_nutricional) {
            this.valor_nutricional = valor_nutricional;
        }

        public String getInstrucciondesecho() {
            return instrucciondesecho;
        }

        public void setInstrucciondesecho(String instrucciondesecho) {
            this.instrucciondesecho = instrucciondesecho;
        }

        public String getPrecionpublico() {
            return precionpublico;
        }

        public void setPrecionpublico(String precionpublico) {
            this.precionpublico = precionpublico;
        }

        public String getPrecauciones() {
            return precauciones;
        }

        public void setPrecauciones(String precauciones) {
            this.precauciones = precauciones;
        }

        public String getInstrucciones() {
            return instrucciones;
        }

        public void setInstrucciones(String instrucciones) {
            this.instrucciones = instrucciones;
        }

        public String getInfoadicional() {
            return infoadicional;
        }

        public void setInfoadicional(String infoadicional) {
            this.infoadicional = infoadicional;
        }

        public String getPuntos() {
            return puntos;
        }

        public void setPuntos(String puntos) {
            this.puntos = puntos;
        }
    }
}
class Coordenadas{
    double latitud=0.0;
    double longitud=0.0;
    String titulo="";

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getTitulo() {
        return titulo;
    }
}