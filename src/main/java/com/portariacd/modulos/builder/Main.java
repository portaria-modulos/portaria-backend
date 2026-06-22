package com.portariacd.modulos.builder;

public class Main {
    public static  void main(String[] args){
     Pessoa pessoa =  new Pessoa.Titular().setIdade("17").setNome("Elivandor").build();
        System.out.println("Nome "+pessoa.getNome());
        System.out.println("Idade "+pessoa.getIdade());
    }
}
