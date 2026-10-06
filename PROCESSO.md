# Processo e decisões — Trabalho 2 (MAF)

> Rascunho para ser conferido e ajustado com as palavras do grupo.
> Onde aparece **[PRINT]**, colocar a imagem do app naquela etapa (pasta `prints/`).

## 1. Como estava o projeto no Trabalho 1 e o que mudou

No Trabalho 1 o app tinha 5 telas (Login, Cardápio, Detalhe, Carrinho e Confirmação). Como ainda
não tínhamos visto Navigation, a troca de tela era feita com uma variável de estado e um `when`
na `MainActivity`. A lista de produtos era fixa no código e não dava para adicionar nem remover
nada.

**[PRINT: tela de Login e Cardápio do Trabalho 1]**

Agora o app tem:

- navegação de verdade com `NavHost` e `navController.navigate(...)`;
- uma barra de baixo (`NavigationBar`) para as áreas principais;
- duas listas que mudam de verdade (Produtos e Categorias), guardadas em `mutableStateListOf`;
- duas telas de detalhes que recebem o item clicado;
- duas telas novas (Categorias e Perfil) e o tema marrom no lugar do roxo padrão.

**[PRINT: app com a barra de baixo e as listas]**

## 2. Por que essas telas novas

- **Categorias (lista 2):** o app precisava de um segundo tipo de item além de Produto. Categoria
  faz sentido numa cafeteria (Cafés, Salgados) e se liga ao Produto, o que permite combinar as
  duas listas no detalhe.
- **Detalhe da categoria:** mostra os produtos daquela categoria e o resumo de preços.
- **Perfil:** dá uma terceira/quarta área para a barra de baixo, mostra um resumo do usuário e tem
  o botão de sair.
- **Detalhe do produto:** é a tela do Trabalho 1 evoluída. Em vez de valores fixos, ela busca o
  produto certo pelo id.

**[PRINT: Categorias, Detalhe da categoria e Perfil]**

## 3. Decisões de organização do código

- **Rotas:** ficam todas no objeto `Rotas`, em `AppNavigation.kt`, como `const val`. As rotas com
  argumento usam `{id}` e funções auxiliares (`Rotas.produto(id)`) para não errar o texto.
- **Onde ficam as listas:** numa classe `AppState`, criada uma vez no `AppNavigation` e passada para
  as telas. Assim todas as telas enxergam as mesmas listas, e quando uma tela adiciona ou remove um
  item, as outras atualizam sozinhas.
- **NavHost central:** um só, no `AppNavigation.kt`, que também controla quando mostrar a barra de
  cima, o botão de voltar e a barra de baixo.
- **Passagem de dados:** só o `id` vai na rota. A tela de detalhes usa o id para buscar o item no
  `AppState`, então sempre aparece o item certo.
- **Tema:** o `dynamicColor` foi desligado porque no Android 12+ ele trocava a paleta marrom pelas
  cores do papel de parede do celular.

**[PRINT: estrutura de arquivos no Android Studio]**

## 4. Complexidade extra na tela de Detalhes

No **Detalhe do produto** o grupo colocou:

1. preço final **calculado** a partir do preço base + tamanho + tipo de leite;
2. a **categoria** do produto, que vem da outra lista;
3. um botão **Ver categoria** que abre o Detalhe da categoria (navegação secundária).

Escolhemos isso porque é natural numa cafeteria personalizar o pedido, e porque mostra os dados das
duas listas juntos. No **Detalhe da categoria** também calculamos quantidade, preço médio, mais
barato e mais caro dos produtos dela.

**[PRINT: Detalhe do produto com o preço calculado e Detalhe da categoria]**

## 5. Dificuldades

> Escrever aqui, com sinceridade, o que cada integrante achou difícil e como resolveram.
> Sugestões de pontos que costumam dar trabalho neste tipo de projeto: passar o id pela rota,
> fazer a lista atualizar na tela com `mutableStateListOf`, e impedir remover uma categoria que
> ainda tem produtos.

**[PRINT: adicionando e removendo itens nas duas listas]**
