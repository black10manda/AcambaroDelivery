package com.example.acambarodelivery.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.acambarodelivery.MainActivity;
import com.example.acambarodelivery.R;
import com.example.acambarodelivery.ui.tools.ToolsFragment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Modelos.Pedido;
import Modelos.Usuario;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);
        final Spinner tipo = root.findViewById(R.id.txtEnvio);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.tipos,android.R.layout.simple_spinner_item);
        tipo.setAdapter(adapter);
        final EditText money = root.findViewById(R.id.txtMoney);

        final EditText address1 = root.findViewById(R.id.txtDireccionA);
        final EditText people1 = root.findViewById(R.id.txtPersonaA);
        final EditText indications1 = root.findViewById(R.id.txtIndicaciones);
        final EditText address2 = root.findViewById(R.id.txtDireccionB);
        final EditText people2 = root.findViewById(R.id.txtPersonaB);
        final EditText indications2 = root.findViewById(R.id.txtIndicacionesB);
        final Button btnPedido=root.findViewById(R.id.btnPedido);


        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);


                btnPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences preferences;
                        preferences = getActivity().getSharedPreferences("Usuario",
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
                        Pedido pedido=new Pedido();
                        pedido.setUser_id(id);
                        pedido.setType(tipo.getSelectedItem().toString());
                        pedido.setMoney(money.getText().toString());
                        pedido.setAddress1(address1.getText().toString());
                        pedido.setPeople1(people1.getText().toString());
                        pedido.setIndications1(indications1.getText().toString());
                        pedido.setAddress2(address2.getText().toString());
                        pedido.setPeople2(people2.getText().toString());
                        pedido.setIndications2(indications2.getText().toString());

                        new AddPedidoToServer().execute(pedido);
                    }
                });


            }
        });
        return root;
    }

    public class AddPedidoToServer extends AsyncTask<Pedido,Integer,Boolean> {
        //adaptador personalizaso
        @Override
        protected Boolean doInBackground(Pedido... pedidos) {

            String params="user_id="+pedidos[0].getUser_id()+"&type="+pedidos[0].getType()+"&money="+pedidos[0].getMoney()+
                    "&address1="+pedidos[0].getAddress1()+"&people1="+pedidos[0].getPeople1()+"&indications1="+pedidos[0].getIndications1()+
                    "&address2="+pedidos[0].getAddress2()+"&people2="+pedidos[0].getPeople2()+"&indications2="+pedidos[0].getIndications2();

            try {
                URL url = new URL("http://10.17.31.234/testServer/rest/AddPedido.php");
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
                    Log.i("addpedido","pedido agregado con exito");
                    return true;

                }else{
                    Log.i("addpedido","no se agrego"+responseCode);
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
                startActivity(new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(), "Pedido con exito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Error al pedir", Toast.LENGTH_SHORT).show();
            }

        }
    }
}