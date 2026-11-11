package service;

import dao.FilmeDAO;
import dao.GeneroDAO;

// instâncias únicas dos services, compartilhadas pelos controllers
// (necessário para o Observer: todos precisam enxergar o mesmo GeneroService)
public class Servicos {

    public static final GeneroService GENERO = new GeneroService(new GeneroDAO(), new FilmeDAO());
    public static final FilmeService FILME = new FilmeService(new FilmeDAO());

    private Servicos() {
    }
}
