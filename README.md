# Gerenciador-de-Livro
  Nesse trabalho nós adaptamos o CRUD de Livros para uma interface web. Demorou bastante tempo para entender como poderíamos fazer essa adaptação e produzir o código, mas ficamos satisfeitos com o resultado. Para que esse CRUD funcionasse na web, os arquivos Principal.java e MenuLivros.java do código original foram excluídos, e suas funções passaram a serem feitas pelos arquivos LivrosApplication.java e LivroController.java, relacionados ao Spring Boot, e index.html. O index.html possui o CSS e o javascript incorporados à ele, porque achamos melhor fazer assim, considerando que esse projeto não é muito grande. A maior dificuldade que tivemos foi fazer o programa lidar com entradas não esperadas, como por exemplo o usuário tentar alterar um livro e colocar o ISBN errado, ou não preencher todos os campos. Como já dito, essa página é um CRUD de Livros, então nela é possível pesquisar um livro por ISBN ou título, adicionar, alterar, deletar um livro e povoar. A seguir, respondemos às perguntas solicitadas sobre o desenvolvimento e funcionamento do trabalho:
| Pergunta                                                                 | Resposta         |
|--------------------------------------------------------------------------|------------------|
| A interface web (HTML, CSS e JavaScript) foi criada?                    | ✅ Sim           |
| O CRUD foi ajustado para incorporar o Spring Boot?                      | ✅ Sim           |
| As operações do CRUD podem ser usadas via a interface web?              | ✅ Sim           |
| Há um tutorial explicando como criar uma interface web para um CRUD?    | ✅ Sim           |
| O trabalho está funcionando corretamente?                                | ✅ Sim           |
| O trabalho está completo?                                               | ✅ Sim           |
| O trabalho é original e não é a cópia de um trabalho de um colega?      | ✅ Sim, é original |

Para rodar a aplicação, é necessário abrir apenas a pasta "livros" no vs code ou em outra aplicação. No terminal você deve ir até essa pasta e digitar "./gradlew bootRun" e então ir até a página http://localhost:8081/ .

Integrantes do grupo: Ana Luíza de Morais Lemos, Daniel Lucas Soares Madureira e João Paolinelli e Silva
