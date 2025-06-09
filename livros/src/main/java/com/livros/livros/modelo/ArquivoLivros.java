package com.livros.livros.modelo;
import com.livros.livros.entidades.Livro;

import java.util.ArrayList;

import com.livros.livros.aeds3.*;

public class ArquivoLivros extends Arquivo<Livro> {
    Arquivo<Livro> arqLivros;
    HashExtensivel<ParISBNID> indiceISBN;
    ArvoreBMais<ParTituloId> indiceTitulo;

    public ArquivoLivros() throws Exception {
        super("livros", Livro.class.getConstructor());
        indiceISBN = new HashExtensivel<>(
            ParISBNID.class.getConstructor(),
            4,
            "./dados/"+nomeEntidade+"/indiceISBN.d.db",
            "./dados/"+nomeEntidade+"/indiceISBN.c.db"
            );
        indiceTitulo = new ArvoreBMais<>(
            ParTituloId.class.getConstructor(), 
            5, 
            "./dados/"+nomeEntidade+"/indiceTitulo.db");
    }

    @Override
    public int create(Livro l) throws Exception {
        int id = super.create(l);
        indiceISBN.create(new ParISBNID(l.getISBN(), id));
        indiceTitulo.create(new ParTituloId(l.getTitulo(), id));
        return id;
    }

    public Livro readISBN(String isbn) throws Exception {
        if(!Livro.isValidISBN13(isbn))
            throw new Exception("ISBN inválido");
        ParISBNID pii = indiceISBN.read(ParISBNID.hash(isbn));
        if(pii != null)
            return read(pii.getId());    // na superclasse
        else 
            return null;
    }

    public Livro[] readTitulo(String titulo) throws Exception {
        if(titulo.length()==0)
            return null;
        ArrayList<ParTituloId> ptis = indiceTitulo.read(new ParTituloId(titulo, -1));
        if(ptis.size()>0) {
            Livro[] livros = new Livro[ptis.size()];
            int i=0;
            for(ParTituloId pti: ptis) 
                livros[i++] = read(pti.getId());
            return livros;
        }
        else 
            return null;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Livro l = read(id);   // na superclasse
        if(l!=null) {
            if(super.delete(id))
                return indiceISBN.delete(ParISBNID.hash(l.getISBN()))
                    && indiceTitulo.delete(new ParTituloId(l.getTitulo(), id));
        }
        return false;
    }

    public boolean delete(String isbn) throws Exception {
        ParISBNID pii =  indiceISBN.read(ParISBNID.hash(isbn));
        if(pii!=null) {
            return delete(pii.getId());
        }
        return false;
    }

    @Override
    public boolean update(Livro novoLivro) throws Exception {
        Livro l = read(novoLivro.getID());    // na superclasse
        if(l!=null) {
            if(super.update(novoLivro)) {
                if(!l.getISBN().equals(novoLivro.getISBN())) {
                    indiceISBN.delete(ParISBNID.hash(l.getISBN()));
                    indiceISBN.create(new ParISBNID(novoLivro.getISBN(), l.getID()));
                }
                if(!l.getTitulo().equals(novoLivro.getTitulo())) {
                    indiceTitulo.delete(new ParTituloId(l.getTitulo(), l.getID()));
                    indiceTitulo.create(new ParTituloId(novoLivro.getTitulo(), novoLivro.getID()));
                }
                return true;
            }
        }
        return false;
    }

    public void povoar() throws Exception {
        try {
            create(new Livro("9783161484100", "O Senhor dos Anéis", "J.R.R. Tolkien", 1,
            java.time.LocalDate.of(1954, 7, 29), 39.90f));
        } catch (Exception e) {
            System.out.println("Book already exists or error: " + e.getMessage());
        }

        try {
            create(new Livro("9786525933061", "Vou me apaixonar por você mesmo assim", "Haruka Mitsui", 1,
            java.time.LocalDate.of(2024, 9, 1), 30.00f));
        } catch (Exception e) {
            System.out.println("Book already exists or error: " + e.getMessage());
        }

        try {
            create(new Livro("9788578270698", "A Revolução dos Bichos", "George Orwell", 1,
            java.time.LocalDate.of(1945, 8, 17), 22.50f));
        } catch (Exception e) {
            System.out.println("Book already exists or error: " + e.getMessage());
        }

        try {
            create(new Livro("9788583623298", "Terra das Gemas", "Haruko Ichikawa", 1,
            java.time.LocalDate.of(2015, 1, 1), 30.00f));
        } catch (Exception e) {
            System.out.println("Book already exists or error: " + e.getMessage());
        }
        try {
            create(new Livro("9781941220979", "Kizumonogatari Wound Tale", "Nisioisin", 1,
            java.time.LocalDate.of(2008, 1, 1), 30.00f));
        } catch (Exception e) {
            System.out.println("Book already exists or error: " + e.getMessage());
        }
    }
}
