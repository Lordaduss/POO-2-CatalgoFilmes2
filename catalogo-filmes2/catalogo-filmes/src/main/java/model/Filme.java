package model;

public class Filme {

    private int id;
    private String titulo;
    private String diretor;
    private int ano;
    private Double nota;
    private Genero genero;

    // construtor visível só no pacote: a criação passa obrigatoriamente pelo FilmeBuilder
    Filme(int id, String titulo, String diretor, int ano, Double nota, Genero genero) {
        this.id = id;
        this.titulo = titulo;
        this.diretor = diretor;
        this.ano = ano;
        this.nota = nota;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDiretor() {
        return diretor;
    }

    public int getAno() {
        return ano;
    }

    public Double getNota() {
        return nota;
    }

    public Genero getGenero() {
        return genero;
    }
}
