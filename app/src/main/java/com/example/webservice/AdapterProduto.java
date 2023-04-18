package com.example.webservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<ViewHolderProduto> {
    //Objeto da classe Context para usar no AlertDialog
    private Context context;
    //Objeto para receber a lista de Produtos que será colocada no RecyclerView
    List<Produto> listaProdutos = new ArrayList<>();

    //Construtor para receber a lista e o contexto
    public AdapterProduto(List<Produto> listaProdutos, Context context){
        this.listaProdutos = listaProdutos;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolderProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Esse método é responsável por buscar layout_item.xml, carregar, montr e retornar
        //a classe LayoutFilter busca um xml para carregar na tela
        View xml = LayoutInflater.from(context).inflate(
                R.layout.layout_item,parent,false);
        ViewHolderProduto vhProduto = new ViewHolderProduto(xml);
        return vhProduto;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduto holder, int position) {
    holder.itemNome.setText(listaProdutos.get(position).getNome());
        holder.itemPreco.setText("R$" + listaProdutos.get(position).getPreco());
        holder.itemQuantidade.setText(listaProdutos.get(position).getQuantidade() + "");

        holder.itemApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Criar e configurar o AlertDialog
                AlertDialog.Builder configAlert = new AlertDialog.Builder(context);
                configAlert.setTitle("Apagar Produto");
                configAlert.setMessage("Deseja mesmo apagar o produto?");

                configAlert.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://localhost:5000/api/Produto/";
                        RequestQueue requisicao = Volley.newRequestQueue(context);


                        JsonObjectRequest remover = new JsonObjectRequest(
                                Request.Method.DELETE,
                                url + "?id=" + listaProdutos.get(position).getId(),
                                null, //sem conteudo no body


                                new Response.Listener<JSONObject>() {


                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response.has("resposta")) {
                                            //Remover o produto da listaProduto
                                            listaProdutos.remove(position);
                                            //att o recyclerView
                                            notifyDataSetChanged();

                                            Toast.makeText(context, "Removido", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Produto não Encontrado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        Toast.makeText(context, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        requisicao.add(remover);
                    }
                });
                configAlert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                configAlert.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }
}
