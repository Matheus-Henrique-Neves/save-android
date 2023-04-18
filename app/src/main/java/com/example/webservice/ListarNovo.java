package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class ListarNovo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_novo);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //lista para armazenar os Produtos que serão recebidos do webservice
        List<Produto> listaFinal = new ArrayList<>();
        //link do webservice
        String url = "http://localhost:5000/api/Produto/";

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

                      AdapterProduto adapter = new AdapterProduto(
                              listaFinal, ListarNovo.this);
                      RecyclerView.LayoutManager layout = new LinearLayoutManager(ListarNovo.this);
                      recyclerView.setLayoutManager(layout);
                      recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ListarNovo.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //enviar a requisição para o webservice
        RequestQueue requisicao = Volley.newRequestQueue(ListarNovo.this);
        requisicao.add(buscaTodos);
    }
}