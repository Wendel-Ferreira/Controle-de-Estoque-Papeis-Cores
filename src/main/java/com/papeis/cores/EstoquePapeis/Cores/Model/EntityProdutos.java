package com.papeis.cores.EstoquePapeis.Cores.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "EstoqueProdutos")
public class EntityProdutos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer qnt;

    @Column
    private Double preco;

    @Column
    private Double totalPreco;  // Implementação futura

    public EntityProdutos() {
    }

    // Construtor sem o ID, pois ele é gerado automaticamente
    public EntityProdutos(String nome, Integer qnt, Double preco) {
        this.nome = nome;
        this.qnt = qnt;
        this.preco = preco;
        calcularPreco();
    }
    // Construtor com ID para fazer UPDATE
    public EntityProdutos(Integer id ,String nome, Integer qnt, Double preco) {
        this.nome = nome;
        this.qnt = qnt;
        this.preco = preco;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQnt() {
        return qnt;
    }

    public void setQnt(Integer qnt) {
        this.qnt = qnt;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;

    }

    public Double getTotalPreco() {
        return totalPreco;
    }

    public void setTotalPreco(Double totalPreco) {
        this.totalPreco = totalPreco; //Depois fazer uma forma de sempre que alterado fazer o calculo
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EntityProdutos{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", qnt=" + qnt +
                ", preco=" + preco +
                ", totalPreco=" + totalPreco +
                '}';
    }
    public void calcularPreco(){
        this.setTotalPreco(this.getQnt() * this.getPreco());
    }
}
