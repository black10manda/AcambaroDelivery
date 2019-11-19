package com.example.acambarodelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Modelos.Usuario;

public class Regristro extends Activity {

    EditText txtUsuario,txtPass,txtpassconfirm,txtname,txtphone,txtaddress,txtanswer;
    Spinner pregunta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regristro);
        txtUsuario=findViewById(R.id.txtUsuario);
        txtPass=findViewById(R.id.txtPass);
        txtpassconfirm=findViewById(R.id.txtPassconfim);
        txtname=findViewById(R.id.txtname);
        txtphone=findViewById(R.id.txtphone);
        txtaddress=findViewById(R.id.txtaddress);
        txtanswer=findViewById(R.id.txtrespuesta);

        pregunta=(Spinner)findViewById(R.id.pregunta);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.preguntas,android.R.layout.simple_spinner_item);
        pregunta.setAdapter(adapter);

    }

    public void btnReg(View view) {
        if (txtanswer.getText().toString().trim().isEmpty()){
            txtanswer.setError("Campo vacio");
            return;
        }
        if (txtphone.getText().toString().trim().isEmpty()){
            txtphone.setError("Campo vacio");
            return;
        }
        if (txtname.getText().toString().trim().isEmpty()){
            txtname.setError("Campo vacio");
            return;
        }
        if (txtUsuario.getText().toString().trim().isEmpty()){
            txtUsuario.setError("Campo vacio");
            return;
        }

        if (txtPass.getText().toString().isEmpty()){
            txtPass.setError("Campo vacio");
            return;
        }

        if (txtpassconfirm.getText().toString().isEmpty()){
            txtpassconfirm.setError("Campo vacio");
            return;
        }

        if (txtPass.getText().toString().length() < 8){
            txtPass.setError("Al menos 8 caracteres");
            return;
        }

        if (!txtPass.getText().toString().equals(txtpassconfirm.getText().toString())){
            Toast.makeText(this, "la contraseña es distinta", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario=new Usuario();
        usuario.setUser(txtUsuario.getText().toString().trim());
        usuario.setPass(txtPass.getText().toString());
        usuario.setName(txtname.getText().toString());
        usuario.setPhone(txtphone.getText().toString());
        usuario.setAddress(txtaddress.getText().toString());
        usuario.setQuestion(pregunta.getSelectedItem().toString());
        usuario.setAnswer(txtanswer.getText().toString());


        new AddUserToServer().execute(usuario);



    }
    public class AddUserToServer extends AsyncTask<Usuario,Integer,Boolean> {
        //adaptador personalizaso
        @Override
        protected Boolean doInBackground(Usuario... usua) {

            String params="user="+usua[0].getUser()+"&password="+usua[0].getPass()+"&name="+usua[0].getName()+
                    "&phone="+usua[0].getPhone()+"&address="+usua[0].getAddress()+"&question="+usua[0].getQuestion()+
                    "&answer="+usua[0].getAnswer();

            try {
                URL url = new URL("http://10.17.31.234/testServer/rest/AddUser.php");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream= connection.getOutputStream();
                BufferedWriter writer=new BufferedWriter( new OutputStreamWriter(outputStream,"UTF-8"));
                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();

                connection.connect();

                int responseCode=connection.getResponseCode();

                if (responseCode==HttpURLConnection.HTTP_OK){
                    Log.i("AdUser","usuario agregado con exito");
                    return true;

                }else{
                    Log.i("AdUser","no se agrego"+responseCode);
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

            if (aBoolean){
                startActivity(new Intent(Regristro.this,MainActivity.class));
                Toast.makeText(Regristro.this, "Registro exitoso inicia sesion", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Regristro.this, "Error al registro", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
