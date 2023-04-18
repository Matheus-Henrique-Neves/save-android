package com.example.webservice;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int quantidade;

    //construtores

    public Produto(int id, String nome, double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto(){ }

    @Override
    public String toString(){
        return "[" + id + "] Nome: " + nome + " R$: " + preco + " Qtde: " + quantidade;
    }

    //get e set

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
