package com.livros.livros;

import com.livros.livros.entidades.Livro;
import com.livros.livros.modelo.ArquivoLivros;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private ArquivoLivros arqLivros;

    public LivroController() throws Exception {
        arqLivros = new ArquivoLivros();
    }

    @GetMapping("/isbn/{isbn}")
    public Livro buscarPorISBN(@PathVariable String isbn) {
        try {
            if (!Livro.isValidISBN13(isbn)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN inválido. Deve conter exatamente 13 dígitos.");
            }
            Livro livro = arqLivros.readISBN(isbn);
            if (livro == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado com ISBN: " + isbn);
            }
            return livro;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao buscar o livro.");
        }
    }

    @GetMapping("/titulo/{titulo}")
    public List<Livro> buscarPorTitulo(@PathVariable String titulo) {
        try {
            Livro[] livros = arqLivros.readTitulo(titulo);
            if (livros == null || livros.length == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum livro encontrado com o título: " + titulo);
            }
            return List.of(livros);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar o livro.");
        }
    }

    @PostMapping
    public ResponseEntity<String> incluirLivro(@RequestBody Livro livro) {
        try {
            if (!Livro.isValidISBN13(livro.getISBN())) {
                return ResponseEntity.badRequest().body("ISBN inválido");
            }
            arqLivros.create(livro);
            return ResponseEntity.ok("Livro incluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Livro já existente ou você não preencheu todos os campos.");
        }
    }

    @PutMapping("/{isbn}")
    public String alterarLivro(@PathVariable String isbn, @RequestBody Livro livroAtualizado) {
        try {
            Livro livro = arqLivros.readISBN(isbn);
            if (livro == null) {
                return "Livro não encontrado.";
            }
            if (livroAtualizado.getISBN() != null && Livro.isValidISBN13(livroAtualizado.getISBN())) {
                livro.setIsbn(livroAtualizado.getISBN());
            }
            if (livroAtualizado.getTitulo() != null) {
                livro.setTitulo(livroAtualizado.getTitulo());
            }
            if (livroAtualizado.getAutor() != null) {
                livro.setAutor(livroAtualizado.getAutor());
            }
            if (livroAtualizado.getEdicao() > 0 && livroAtualizado.getEdicao() < 128) {
                livro.setEdicao(livroAtualizado.getEdicao());
            }
            if (livroAtualizado.getDataLancamento() != null) {
                livro.setDataLancamento(livroAtualizado.getDataLancamento());
            }
            if (livroAtualizado.getPreco() > 0) {
                livro.setPreco(livroAtualizado.getPreco());
            }
            arqLivros.update(livro);
            return "Livro alterado com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @DeleteMapping("/{isbn}")
    public String excluirLivro(@PathVariable String isbn) {
        try {
            Livro livro = arqLivros.readISBN(isbn);
            if (livro == null) {
                return "Livro não encontrado.";
            }
            arqLivros.delete(isbn);
            return "Livro excluído com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping("/povoar")
    public String povoar() {
        try {
            arqLivros.povoar();
            return "Base povoada com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao povoar a base: " + e.getMessage();
        }
    }
}
