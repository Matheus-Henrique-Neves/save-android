package com.example.webservice;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderProduto extends RecyclerView.ViewHolder {
    TextView itemNome, itemPreco, itemQuantidade;
    ImageButton itemApagar;

    //construtor

    public ViewHolderProduto(View layoutItem){
        super(layoutItem);
        itemNome = layoutItem.findViewById(R.id.itemNome);
        itemPreco = layoutItem.findViewById(R.id.item_Preco);
        itemQuantidade = layoutItem.findViewById(R.id.item_Quantidade);
        itemApagar = layoutItem.findViewById(R.id.item_Apagar);

    }
}
