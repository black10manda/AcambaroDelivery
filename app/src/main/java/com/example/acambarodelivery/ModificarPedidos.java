package com.example.acambarodelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.acambarodelivery.ui.home.HomeFragment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Modelos.Pedido;

public class ModificarPedidos extends AppCompatActivity {

    String idp;
    String dinero,direcion1,persona1,indicacion1,direcion2,persona2,indicacion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_pedidos);
        final Spinner tipo = findViewById(R.id.txtEnvio);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.tipos,android.R.layout.simple_spinner_item);
        tipo.setAdapter(adapter);
        final EditText money = findViewById(R.id.txtMoney);

        final EditText address1 = findViewById(R.id.txtDireccionA);
        final EditText people1 = findViewById(R.id.txtPersonaA);
        final EditText indications1 = findViewById(R.id.txtIndicaciones);
        final EditText address2 = findViewById(R.id.txtDireccionB);
        final EditText people2 = findViewById(R.id.txtPersonaB);
        final EditText indications2 = findViewById(R.id.txtIndicacionesB);
        final Button btnPedido=findViewById(R.id.btnPedido);
        Intent intent=getIntent();
        dinero=intent.getStringExtra("money");
        direcion1=intent.getStringExtra("address1");
        persona1=intent.getStringExtra("people1");
        indicacion1=intent.getStringExtra("indications2");
        direcion2=intent.getStringExtra("address2");
        persona2=intent.getStringExtra("people2");
        indicacion2=intent.getStringExtra("indications2");

        money.setText(dinero);

        address1.setText(direcion1);
        people1.setText(persona1);
        indications1.setText(indicacion1);

        address2.setText(direcion2);
        people2.setText(persona2);
        indications2.setText(indicacion2);

        btnPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                preferences = getSharedPreferences("Usuario",
                        MODE_PRIVATE);
                String id=preferences.getString("id", "error");
                if (money.getText().toString().trim().isEmpty()){
                    money.setError("Campo vacio");
                    return;
                }
                if (address1.getText().toString().trim().isEmpty()){
                    address1.setError("Campo vacio");
                    return;
                }
                if (people1.getText().toString().trim().isEmpty()){
                    people1.setError("Campo vacio");
                    return;
                }
                if (indications1.getText().toString().trim().isEmpty()){
                    indications1.setError("Campo vacio");
                    return;
                }
                if (address2.getText().toString().trim().isEmpty()){
                    address2.setError("Campo vacio");
                    return;
                }
                if (people2.getText().toString().trim().isEmpty()){
                    people2.setError("Campo vacio");
                    return;
                }
                if (indications2.getText().toString().trim().isEmpty()){
                    indications2.setError("Campo vacio");
                    return;
                }

                Intent intent=getIntent();
                idp=intent.getStringExtra("idP");
                //Toast.makeText(ModificarPedidos.this, "idp: "+idp  , Toast.LENGTH_SHORT).show();

                Pedido pedido=new Pedido();
                pedido.setId(Integer.parseInt(idp));
                //pedido.setUser_id(id);
                pedido.setType(tipo.getSelectedItem().toString());
                pedido.setMoney(money.getText().toString());
                pedido.setAddress1(address1.getText().toString());
                pedido.setPeople1(people1.getText().toString());
                pedido.setIndications1(indications1.getText().toString());
                pedido.setAddress2(address2.getText().toString());
                pedido.setPeople2(people2.getText().toString());
                pedido.setIndications2(indications2.getText().toString());

                new EdithPedidoToServer().execute(pedido);
            }
        });
    }
    public class EdithPedidoToServer extends AsyncTask<Pedido,Integer,Boolean> {
        //adaptador personalizaso
        @Override
        protected Boolean doInBackground(Pedido... pedidos) {

            String params="id="+pedidos[0].getId()+"&type="+pedidos[0].getType()+"&money="+pedidos[0].getMoney()+
                    "&address1="+pedidos[0].getAddress1()+"&people1="+pedidos[0].getPeople1()+"&indications1="+pedidos[0].getIndications1()+
                    "&address2="+pedidos[0].getAddress2()+"&people2="+pedidos[0].getPeople2()+"&indications2="+pedidos[0].getIndications2();

            try {
                URL url = new URL("http://10.17.31.234/testServer/rest/UpdatePedido.php");
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

                Log.i("params",params);
                int responseCode=connection.getResponseCode();

                if (responseCode==HttpURLConnection.HTTP_OK){
                    Log.i("updatepedido","pedido actualizado con exito");
                    return true;

                }else{
                    Log.i("updatepedido","no se agrego"+responseCode);
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
                startActivity(new Intent(ModificarPedidos.this, MainActivity.class));
                Toast.makeText(ModificarPedidos.this, "Pedido actualizado con exito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ModificarPedidos.this, "Error al actualizar pedir", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
