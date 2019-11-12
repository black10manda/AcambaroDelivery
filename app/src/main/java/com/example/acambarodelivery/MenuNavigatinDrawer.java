package com.example.acambarodelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
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

public class MenuNavigatinDrawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String id,idFinal;
    TextView textNameUser,textUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigatin_drawer);
        textNameUser=findViewById(R.id.textNameUser);
        textUser=findViewById(R.id.textUser);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        String[] parts = id.split(",");
        char idc1 = parts[0].charAt(1);
        char idc2 = parts[0].charAt(2);
        idFinal=""+idc1+idc2;

        new GetUser().execute();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_gallery, R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


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
                    editor.putBoolean("log", true);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigatin_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_settings:
                Toast.makeText(this, "Regresa Pronto", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("Usuario",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("log", false);
                editor.commit();

                startActivity(new Intent(this, MainActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
