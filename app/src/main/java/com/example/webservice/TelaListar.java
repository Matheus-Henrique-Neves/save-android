package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TelaListar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listar);

        ListView listView = findViewById(R.id.listView1);
        //lista para armazenar os Produtos que serão recebidos do webservice
        List<Produto> listaFinal = new ArrayList<>();
        //link do webservice
        String url = "http://10.0.2.2:5000/api/Produto/";

        //o método do webservice que lista todos os produtos irá retornar um Array
        JsonArrayRequest buscaTodos = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, //sem conteudo no body
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                //converter cada produto e adicionar na listaFinal
                                JSONObject objeto = response.getJSONObject(i);
                                Produto p = new Produto(
                                        objeto.getInt("id"), objeto.getString("nome"), objeto.getDouble("preco"), objeto.getInt("quantidade"));
                                listaFinal.add(p);
                            } catch (JSONException exc) {
                                exc.printStackTrace();
                            }
                        }

                        //adicionar a listaFinal no adapter
                        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(
                                TelaListar.this, //contexto
                                android.R.layout.simple_expandable_list_item_1, //layout padrão
                                listaFinal); // lista com os valores
                        listView.setAdapter(adapter); //coloca os valores no ListView
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(TelaListar.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //enviar a requisição para o webservice
        RequestQueue requisicao = Volley.newRequestQueue(TelaListar.this);
        requisicao.add(buscaTodos);
    }
}