package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //iniciar os componentes da tela
        EditText id = findViewById(R.id.edCodigo);
        EditText nome = findViewById(R.id.edNome);
        EditText preco = findViewById(R.id.edPreco);
        EditText quantidade = findViewById(R.id.edQuantidade);
        Button cadastrar = findViewById(R.id.btCadastrar);
        Button remover = findViewById(R.id.btRemover);
        Button atualizar = findViewById(R.id.btAtualizar);
        Button buscar = findViewById(R.id.btBuscar);
        Button listar = findViewById(R.id.btListar);

        //usar classe do Volley para iniciar a configuração das requisições
        RequestQueue requisicao = Volley.newRequestQueue(this);
        //configurar o caminho da API (url)
        //nesse caso não podemos usar o localhost pois, como o Android é um sistema operacional, o localhost irá direcionar para o IP interno do Android e não
        //do computador aonde o webservice está executando. Para indicar o localhost do computador, devemos usar o IP reservado do android que é o 10.0.2.2
        String url = "http://localhost:5000/api/Produto";

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListarNovo.class));
            }
        });

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //criar um objeto no padrão JSON com os dados infomados na tela
                JSONObject dadosBody = new JSONObject();
                try{
                    //passar os dados informados na tela e o nome do parâmetro
                    dadosBody.put("id", id.getText().toString());
                    dadosBody.put("nome", nome.getText().toString());
                    dadosBody.put("preco", preco.getText().toString());
                    dadosBody.put("quantidade", quantidade.getText().toString());

                }catch (JSONException exc){
                    exc.printStackTrace();
                }

                //como o webservice, ao cadastrar um produto, retorna um objeto no padrão JSON, então devemos usar a classe JsonObjectRequest para recuperar
                //a resposta do webservice
                JsonObjectRequest enviarPut = new JsonObjectRequest(
                        Request.Method.PUT, //como os dados serão enviados
                        url, //para onde serão enviados, via post
                        dadosBody, //dados que serão enviados (body da requisição)
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("mensagem")) {
                                    Toast.makeText(MainActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erro ao Atualizar", Toast.LENGTH_SHORT).show();
                    }
                }
                );

                //usar o objeto "requisicao" para enviar os dados do POST
                requisicao.add(enviarPut); // envia os dados para o webservice
            }
        });

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest remover = new JsonObjectRequest(
                        Request.Method.DELETE,
                        url + "?id=" + id.getText().toString(),
                        null, //sem conteudo no body
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("mensagem")) {
                                    Toast.makeText(MainActivity.this, "Removido", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Produto não Encontrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(MainActivity.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requisicao.add(remover);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //como o webservice retorna um Array (vetor) com o objeto Produto, então devemos configurar a requisição usando JsonArrayRequest
                JsonArrayRequest buscarID = new JsonArrayRequest(
                        Request.Method.GET,
                        url + " buscar?id=" + id.getText().toString(),
                        null, //não vai passar nada no Body
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //testar se o "reponse" possui um objeto Produto
                                if (response.length() == 0) {
                                    Toast.makeText(MainActivity.this, "Produto não Encontrado", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Ler o Produto encontrado e converter a posição 0 para um objeto Json na configuração da classe Produto
                                    try {
                                        //converter a resposta do webservice para a classe Produto
                                        JSONObject objeto = response.getJSONObject(0);
                                        Produto p = new Produto(
                                                objeto.getInt("id"),
                                                objeto.getString("nome"),
                                                objeto.getDouble("preco"),
                                                objeto.getInt("quantidade"));
                                        //ainda dentro do "onResponse", colocar os dados do produto na tela
                                        nome.setText(p.getNome());
                                        preco.setText(p.getPreco() + "");
                                        quantidade.setText(p.getQuantidade() + "");

                                    } catch (JSONException exc) {
                                        exc.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
                );
                //enviar a requisição para o webservice
                requisicao.add(buscarID);
            }
        });


        //código do botão cadastrar
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //criar um objeto no padrão JSON com os dados infomados na tela
                JSONObject dadosBody = new JSONObject();
                try{
                    //passar os dados informados na tela e o nome do parâmetro
                    dadosBody.put("id", id.getText().toString());
                    dadosBody.put("nome", nome.getText().toString());
                    dadosBody.put("preco", preco.getText().toString());
                    dadosBody.put("quantidade", quantidade.getText().toString());

                }catch (JSONException exc){
                    exc.printStackTrace();
                }

                //como o webservice, ao cadastrar um produto, retorna um objeto no padrão JSON, então devemos usar a classe JsonObjectRequest para recuperar
                //a resposta do webservice
                JsonObjectRequest enviarPost = new JsonObjectRequest(
                        Request.Method.POST, //como os dados serão enviados
                        url, //para onde serão enviados, via post
                        dadosBody, //dados que serão enviados (body da requisição)
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("mensagem")) {
                                    Toast.makeText(MainActivity.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erro ao Enviar", Toast.LENGTH_SHORT).show();
                    }
                }
                );

                //usar o objeto "requisicao" para enviar os dados do POST
                requisicao.add(enviarPost); // envia os dados para o webservice
            }
        });
    }
}