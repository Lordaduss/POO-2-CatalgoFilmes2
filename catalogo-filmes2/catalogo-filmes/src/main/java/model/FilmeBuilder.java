package model;

// Padrão Builder: monta um Filme passo a passo, sem construtor de 6 parâmetros
public class FilmeBuilder {

    private int id;
    private String titulo;
    private String diretor;
    private int ano;
    private Double nota;
    private Genero genero;

    public FilmeBuilder id(int id) {
        this.id = id;
        return this;
    }

    public FilmeBuilder titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public FilmeBuilder diretor(String diretor) {
        this.diretor = diretor;
        return this;
    }

    public FilmeBuilder ano(int ano) {
        this.ano = ano;
        return this;
    }

    public FilmeBuilder nota(Double nota) {
        this.nota = nota;
        return this;
    }

    public FilmeBuilder genero(Genero genero) {
        this.genero = genero;
        return this;
    }

    public Filme build() {
        return new Filme(id, titulo, diretor, ano, nota, genero);
    }
}
