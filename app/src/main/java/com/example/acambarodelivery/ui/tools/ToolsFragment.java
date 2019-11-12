package com.example.acambarodelivery.ui.tools;

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
import com.example.acambarodelivery.Regristro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Modelos.Usuario;

import static android.content.Context.MODE_PRIVATE;

public class ToolsFragment extends Fragment{

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);



        final EditText user=root.findViewById(R.id.txtUsuarioU);
        final EditText pass=root.findViewById(R.id.txtPassU);
        final EditText passConfirm=root.findViewById(R.id.txtPassconfimU);
        final EditText name=root.findViewById(R.id.txtnameU);
        final EditText phone=root.findViewById(R.id.txtphoneU);
        final EditText address=root.findViewById(R.id.txtaddressU);
        final Spinner question=root.findViewById(R.id.preguntaU);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.preguntas,android.R.layout.simple_spinner_item);
        question.setAdapter(adapter);

        final EditText answer=root.findViewById(R.id.txtrespuestaU);
        final Button btnActualizarUser=root.findViewById(R.id.btnActualizar);


       // final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("Usuario",
                        MODE_PRIVATE);

                user.setText(preferences.getString("user", "error"));
                pass.setText(preferences.getString("pass", "error"));
                passConfirm.setText(preferences.getString("pass", "error"));
                name.setText(preferences.getString("name", "error"));
                phone.setText(preferences.getString("phone", "error"));
                address.setText(preferences.getString("address", "error"));
                answer.setText(preferences.getString("answer", "error"));
                btnActualizarUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (answer.getText().toString().trim().isEmpty()){
                            answer.setError("Campo vacio");
                            return;
                        }
                        if (phone.getText().toString().trim().isEmpty()){
                            phone.setError("Campo vacio");
                            return;
                        }
                        if (name.getText().toString().trim().isEmpty()){
                            name.setError("Campo vacio");
                            return;
                        }
                        if (user.getText().toString().trim().isEmpty()){
                            user.setError("Campo vacio");
                            return;
                        }
                        if (pass.getText().toString().isEmpty()){
                            pass.setError("Campo vacio");
                            return;
                        }
                        if (passConfirm.getText().toString().isEmpty()){
                            passConfirm.setError("Campo vacio");
                            return;
                        }
                        if (pass.getText().toString().length() < 8){
                            pass.setError("Al menos 8 caracteres");
                            return;
                        }
                        if (!pass.getText().toString().equals(passConfirm.getText().toString())){
                            Toast.makeText(getActivity(), "las contraseñas son diferentes", Toast.LENGTH_SHORT).show();
                            pass.setError("las contraseñas son diferentes");
                            passConfirm.setError("las contraseñas son diferentes");
                            return;
                        }

                        SharedPreferences preferences;
                        preferences = getActivity().getSharedPreferences("Usuario",
                                MODE_PRIVATE);
                        Usuario usuario=new Usuario();
                        usuario.setId(Integer.parseInt(preferences.getString("id", "error")));
                        usuario.setUser(user.getText().toString().trim());
                        usuario.setPass(pass.getText().toString());
                        usuario.setName(name.getText().toString());
                        usuario.setPhone(phone.getText().toString());
                        usuario.setAddress(address.getText().toString());
                        usuario.setQuestion(question.getSelectedItem().toString());
                        usuario.setAnswer(answer.getText().toString());


                        new UpdateUserToServer().execute(usuario);
                        //Toast.makeText(getActivity(), "Actualizo", Toast.LENGTH_SHORT).show();
                    }

                });



            }
        });
        return root;

    }
    public class UpdateUserToServer extends AsyncTask<Usuario,Integer,Boolean> {
        //adaptador personalizaso
        @Override
        protected Boolean doInBackground(Usuario... usua) {

            String params="id="+usua[0].getId()+"&user="+usua[0].getUser()+"&password="+usua[0].getPass()+"&name="+usua[0].getName()+
                    "&phone="+usua[0].getPhone()+"&address="+usua[0].getAddress()+"&question="+usua[0].getQuestion()+
                    "&answer="+usua[0].getAnswer();

            try {
                URL url = new URL("http://192.168.1.104/testServer/rest/UpdateUser.php");
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
                startActivity(new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(), "Actualizacion con exito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }

        }
    }

}