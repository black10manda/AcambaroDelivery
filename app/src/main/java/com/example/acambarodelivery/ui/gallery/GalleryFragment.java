package com.example.acambarodelivery.ui.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.acambarodelivery.MainActivity;
import com.example.acambarodelivery.MenuNavigatinDrawer;
import com.example.acambarodelivery.ModificarPedidos;
import com.example.acambarodelivery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import Modelos.Pedido;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ListAdapter myAdapter;
    ArrayList<Pedido> arrayList;
    ListView listPedidos;
    String[] elementos = {"jose", "pedro", "maria", "miguel", "luis", "daniel", "elena", "Laura", "Sofia"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        listPedidos=root.findViewById(R.id.listPedidos);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,elementos);
        //listPedidos.setAdapter(adapter2);
        registerForContextMenu(listPedidos);

        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
                new GetAllPedidos().execute();

            }
        });
        return root;
    }
    public class GetAllPedidos extends AsyncTask<Void, Integer, JSONArray> {
        String result="0";
        @Override
        protected JSONArray doInBackground(Void... voids) {
            URLConnection connection=null;
            JSONArray jsonArray=null;

            try {
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("Usuario",
                        MODE_PRIVATE);
                String id=preferences.getString("id", "error");
                connection = new URL("http://10.17.31.234/testServer/rest/getPedidoByID.php?id="+id).openConnection();

                InputStream inputStream=(InputStream)connection.getContent();
                byte[] buffer2=new byte[1000000];
                int size=inputStream.read(buffer2);



                jsonArray=new JSONArray(new String(buffer2,0,size));
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
            Log.i("json",jsonArray+"");
            arrayList=new ArrayList<Pedido>();

            //recorrer el jsonarray y consultar los nodos del json

            for (int i=0;i<jsonArray.length();i++){
                Pedido pedido=new Pedido();
                try {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    pedido.setId(jsonObject.getInt("id"));
                    pedido.setUser_id(jsonObject.getString("user_id"));
                    pedido.setType(jsonObject.getString("type"));
                    pedido.setMoney(jsonObject.getString("money"));
                    pedido.setAddress1(jsonObject.getString("address1"));
                    pedido.setPeople1(jsonObject.getString("people1"));
                    pedido.setIndications1(jsonObject.getString("indications1"));
                    pedido.setAddress2(jsonObject.getString("address2"));
                    pedido.setPeople2(jsonObject.getString("people2"));
                    pedido.setIndications2(jsonObject.getString("indications2"));

                    arrayList.add(pedido);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            ArrayAdapter<Pedido> adapter = new ArrayAdapter<Pedido>(getActivity(),android.R.layout.simple_expandable_list_item_1, arrayList);
           // myAdapter= new ListAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,arrayList);
            listPedidos.setAdapter(adapter);


        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Pedido pedido=new Pedido();
        switch (item.getItemId()){
            case R.id.itemContextDelete:
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();

                pedido.setId(arrayList.get(info.position).getId());
                new DeletePedidoToServer().execute(pedido);
                startActivity(new Intent(getActivity(), MainActivity.class));
                return true;

            case R.id.itemContextUpdate:

                String id= String.valueOf(arrayList.get(info.position).getId());
                String money= String.valueOf(arrayList.get(info.position).getMoney());
                String address1= String.valueOf(arrayList.get(info.position).getAddress1());
                String people1= String.valueOf(arrayList.get(info.position).getPeople1());
                String indications1= String.valueOf( arrayList.get(info.position).getIndications1());

                String address2= String.valueOf(arrayList.get(info.position).getAddress2());
                String people2= String.valueOf(arrayList.get(info.position).getPeople2());
                String indications2= String.valueOf( arrayList.get(info.position).getIndications2());

                Intent intent = new Intent(getActivity(), ModificarPedidos.class);
                intent.putExtra("idP", id);
                intent.putExtra("money",money );

                intent.putExtra("address1",address1 );
                intent.putExtra("people1",people1 );
                intent.putExtra("indications1",indications1);

                intent.putExtra("address2", address2);
                intent.putExtra("people2",people2);
                intent.putExtra("indications2", indications2);


                Toast.makeText(getActivity(), "id: "+id, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_contextual,menu);


    }
    public class DeletePedidoToServer extends AsyncTask<Pedido,Integer,String>{
        //adaptador personalizaso
        @Override
        protected String doInBackground(Pedido... usua) {

            String params="id="+usua[0].getId();

            try {
                URL url = new URL("http://10.17.31.234/testServer/rest/DeletePedido.php");
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
                    Log.i("DeUser","usuario eliminado con exito");

                }else{
                    Log.i("DeUser","no se elimino"+responseCode);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}