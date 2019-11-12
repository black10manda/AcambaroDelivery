package com.example.acambarodelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import Modelos.Usuario;

public class MainActivity extends Activity {

    EditText edtUser,edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUser=findViewById(R.id.edtUser);
        edtPass=findViewById(R.id.edtPass);

        SharedPreferences preferences;
        preferences = getSharedPreferences("Usuario",
                MODE_PRIVATE);
        boolean log = preferences.getBoolean("log", false);
        if (log){
            Usuario usuario=new Usuario();
            usuario.setUser(preferences.getString("user", "error"));
            usuario.setPass(preferences.getString("pass", "error"));
            new LoginRest().execute(usuario);
        }

    }
    public void btniniciar(View view) {

        //startActivity(new Intent(MainActivity.this,Usuarios.class));

        if (validarDatos(edtUser.getText().toString().trim(),edtPass.getText().toString())){

            Usuario usuario=new Usuario();
            usuario.setUser(edtUser.getText().toString());
            usuario.setPass(edtPass.getText().toString());
            new LoginRest().execute(usuario);
        }


    }

    public boolean validarDatos(String user,String pass){

        if (TextUtils.isEmpty(user)){
            edtUser.setError(getString(R.string.emplyFieldError));
            edtUser.setFocusable(true);
            return false;
        }

        if (TextUtils.isEmpty(pass)){
            edtPass.setError(getString(R.string.emplyFieldError));
            edtPass.setFocusable(true);
            return false;
        }

        return true;
    }

    public void btnRegistro(View view) {
        //iniciar activity registro
        startActivity(new Intent(MainActivity.this,Regristro.class));
    }

    public void btnContra(View view) {
        Toast.makeText(this, "activiti olvido", Toast.LENGTH_SHORT).show();
    }

    public  class LoginRest extends AsyncTask<Usuario,Integer, Boolean> {

        //preparar la conexion con el servidor
        URLConnection connection;
        //variable para almacenar el resultado que nos muestra el servidor
        String result="0";
        JSONObject jsonArray=null;
        //apuntar la variable de conxion al servidor
        @Override
        protected Boolean doInBackground(Usuario... usua) {

            try {
                connection= new URL("http://192.168.1.104/testServer/rest/Login.php?user="+usua[0].getUser()+"&password="+usua[0].getPass()).openConnection();
                InputStream inputStream=(InputStream) connection.getContent();
                byte[] buffer=new byte[10000000];
                result =new String(buffer,0,inputStream.read(buffer));
                int size=inputStream.read(buffer);

                Log.i("result",result+":");

                if (result.charAt(2)=='1'){
                    Log.i("entro ","1");

                    return true;
                }else {
                    Log.i("no entro ","0");
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }




        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Usuario usuarios=new Usuario();

            String cadena=result;
            String[] parts = cadena.split(":");
               if (aBoolean){

                   Toast.makeText(MainActivity.this, "Welcome ", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(MainActivity.this, MenuNavigatinDrawer.class);
                   intent.putExtra("id", parts[2]);
                   startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }



        }
    }
}
