package com.example.acambarodelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Modelos.Usuario;

public class Menu extends AppCompatActivity {
    TextView pruebauser,userM,passM,nameM,phoneM,addressM,questionM,answeM;

    String id,idFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        pruebauser=findViewById(R.id.pruebauser);
        userM=findViewById(R.id.userM);
        passM=findViewById(R.id.passM);
        nameM=findViewById(R.id.nameM);
        phoneM=findViewById(R.id.phoneM);
        addressM=findViewById(R.id.addressM);
        questionM=findViewById(R.id.questionM);
        answeM=findViewById(R.id.answeM);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        String[] parts = id.split(",");
        char idc1 = parts[0].charAt(1);
        char idc2 = parts[0].charAt(2);
        idFinal=""+idc1+idc2;
        pruebauser.setText("id: "+idFinal);

        new GetUser().execute();

        SharedPreferences preferences;
        preferences = getSharedPreferences("Usuario",
                MODE_PRIVATE);
        pruebauser.setText(preferences.getString("id", "error"));
        userM.setText(preferences.getString("user", "error"));
        passM.setText(preferences.getString("pass", "error"));
        nameM.setText(preferences.getString("name", "error"));
        phoneM.setText(preferences.getString("phone", "error"));
        addressM.setText(preferences.getString("address", "error"));
        questionM.setText(preferences.getString("question", "error"));
        answeM.setText(preferences.getString("answer", "error"));
    }

    public class GetUser extends AsyncTask<Void, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            URLConnection connection=null;
            JSONArray jsonArray=null;
            try {
                connection = new URL("http://192.168.1.104/testServer/rest/getUserByID.php?id="+idFinal).openConnection();

                InputStream inputStream=(InputStream)connection.getContent();
                byte[] buffer=new byte[100000];
                int size=inputStream.read(buffer);

                jsonArray=new JSONArray(new String(buffer,0,size));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }
        ///llenar el adaptador con los datos del arreglo tipo JSON que regresa el servidor


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            //consultar los datos de la respuesta del servidor qu evienen en la variable
            //del parametro del metodo; el cual es del mismo tipo que el resultado del metodo
            //doinbckgroun
            //recorrer el jsonarray y consultar los nodos del json

            for (int i=0;i<jsonArray.length();i++){
                Usuario usuario=new Usuario();
                try {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    //usuario.setId(jsonObject.getInt("id"));
                    usuario.setUser(jsonObject.getString("user"));
                    usuario.setPass(jsonObject.getString("pass"));
                    usuario.setName(jsonObject.getString("name"));
                    usuario.setPhone(jsonObject.getString("phone"));
                    usuario.setAddress(jsonObject.getString("address"));
                    usuario.setQuestion(jsonObject.getString("question"));
                    usuario.setAnswer(jsonObject.getString("answer"));

                    SharedPreferences preferences = getSharedPreferences("Usuario",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id", idFinal);
                    editor.putString("user", usuario.getUser());
                    editor.putString("pass", usuario.getPass());
                    editor.putString("name", usuario.getName());
                    editor.putString("phone", usuario.getPhone());
                    editor.putString("address", usuario.getAddress());
                    editor.putString("question", usuario.getQuestion());
                    editor.putString("answer",usuario.getAnswer());
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



        }
    }
}
