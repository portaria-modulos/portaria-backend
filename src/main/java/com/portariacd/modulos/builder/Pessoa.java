package com.portariacd.modulos.builder;

public class Pessoa {

    private String nome;
    private String idade;
   //Contrutor da class
    public Pessoa(Titular builder) {
        this.nome = builder.nome;
        this.idade = builder.idade;
    }
    public Pessoa() {

    }
    static class  Titular {
        private String nome;
        private String idade;

        public Titular(String nome, String idade) {
            this.nome = nome;
            this.idade = idade;
        }

        public Titular() {

        }

        public Titular setNome(String nome) {
            this.nome = nome;
            return this;        }

        public Titular setIdade(String idade) {
            this.idade = idade;
            return this;
        }
        public Pessoa build(){
            return new Pessoa(this);
        }


    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }
}
