# Processo e decisões — Trabalho 3 (Room)

> Rascunho para ser conferido e ajustado com as palavras do grupo.
> Onde aparece **[PRINT AQUI]** / **[VÍDEO AQUI]**, colocar a imagem ou o vídeo do app.

## 1. Como estava no Trabalho 2 e o que mudou

No Trabalho 2 todos os dados ficavam **na memória**, no arquivo `Dados.kt`:

- `produtos` e `pedidos` eram `mutableStateListOf(...)`, já começando com alguns itens fixos;
- os ids eram controlados à mão com `proximoIdProduto` e `proximoIdPedido`;
- o `Pedido` guardava os produtos inteiros numa `List<Produto>`.

Isso funcionava, mas ao fechar o app tudo voltava ao que estava no código.

No Trabalho 3 os produtos e os pedidos passaram a ser salvos num **banco de dados local com Room**.
Agora o que é cadastrado continua lá mesmo depois de fechar e abrir o app de novo.

O que mudou no código:

- `Dados.kt` ficou só com o **carrinho** e o **nome do usuário**, que são temporários de propósito
  (o carrinho ainda não é um pedido);
- foram criados `Produto.kt`, `Pedido.kt`, `ProdutoDao.kt`, `PedidoDao.kt` e `AppDatabase.kt`;
- cada tela busca seus dados no banco quando abre, e os botões de adicionar, editar e remover
  gravam no banco;
- o banco começa vazio: os produtos são cadastrados pela própria tela de Cardápio.

As telas, os Cards, a navegação e a barra de baixo continuaram iguais. A única coisa nova na
interface é a parte de **editar produto** na tela de detalhe.

## 2. Entity, DAO e Database

### Entities (tabelas)

| Classe | Tabela | Campos |
|---|---|---|
| `Produto` | `produtos` | `id`, `emoji`, `nome`, `descricao`, `preco`, `categoria` |
| `Pedido` | `pedidos` | `id`, `cliente`, `idsItens`, `total`, `status` |

- Os campos do `Produto` são os mesmos do Trabalho 2, porque são os que as telas já mostravam.
- O `id` é `@PrimaryKey(autoGenerate = true)`: quem cria o número é o banco. Ele é o último
  parâmetro com valor padrão `0`, então dá para escrever `Produto(emoji, nome, descricao, preco, categoria)`
  sem inventar um id.

**Por que `idsItens` é uma `String`?** No Trabalho 2 o pedido tinha `itens: List<Produto>`. O Room
só salva tipos simples (texto, número…); para salvar uma lista seria preciso criar um
`TypeConverter`. Para manter simples, guardamos só os ids dos produtos num texto separado por
vírgula, por exemplo `"1,3,3"` (um produto 1 e dois produtos 3). A função `listaIds()` transforma
esse texto de volta em lista `[1, 3, 3]` quando a tela precisa.

**Por que salvar o `total`?** Como o produto pode ser editado depois, o preço dele pode mudar.
Guardando o total no momento do pedido, o valor do pedido não muda sozinho.

### DAOs

`ProdutoDao` e `PedidoDao` têm as mesmas operações, todas `suspend fun`:

| Função | Anotação | O que faz |
|---|---|---|
| `inserir` | `@Insert` | cria uma linha nova |
| `buscarTodos` | `@Query("SELECT * FROM ...")` | traz todas as linhas |
| `atualizar` | `@Update` | altera a linha com o mesmo id |
| `deletar` | `@Delete` | apaga a linha |
| `buscarPorId` | `@Query("... WHERE id = :id")` | traz só uma linha |

No `PedidoDao`, o `inserir` devolve `Long`: é o id que o banco acabou de gerar.

### Database

`AppDatabase` junta as duas tabelas (`version = 1`) e entrega os DAOs. Ela segue o padrão
**Singleton** (`companion object` + `@Volatile` + `synchronized`), para o app inteiro usar um único
banco, chamado `cafe_database`.

## 3. Consulta além das 4 básicas

Além de inserir, buscar todos, atualizar e deletar, criamos a **`buscarPorId`** nos dois DAOs:

```kotlin
@Query("SELECT * FROM produtos WHERE id = :id")
suspend fun buscarPorId(id: Int): Produto?
```

Ela é usada nas telas que recebem um id pela rota: **Detalhe do produto**, **Detalhe do pedido** e
**Confirmação**. Assim a tela pede ao banco só o item que precisa, em vez de buscar tudo e procurar
na lista. Ela devolve `Produto?` (pode ser `null`), porque o item pode ter sido apagado.

## 4. Dificuldades da migração (de `mutableStateListOf` para Room)

- **Ids gerados pelo banco.** Antes a gente controlava o id com `proximoIdProduto++`. Agora o banco
  gera o id, então os objetos são criados sem id e o Room preenche.
- **Lista de itens do pedido.** O Room não salva `List<Produto>` direto. A solução foi guardar os ids
  como texto (`idsItens`) e, nas telas, procurar cada id na lista de produtos do banco. Se o produto
  foi apagado, o detalhe do pedido mostra "Produto removido".
- **A tela não atualizava sozinha.** Com `mutableStateListOf` a tela mudava assim que a lista mudava.
  Com o banco, depois de inserir, editar ou remover, é preciso **buscar a lista de novo**
  (`buscarTodos()`) para a tela mostrar a mudança.
- **Navegar depois de inserir.** No Carrinho, o pedido é salvo numa thread de fundo
  (`Dispatchers.IO`) e só depois sabemos o id gerado. Mas trocar de tela só pode ser feito na thread
  principal, então usamos `withContext(Dispatchers.Main) { navController.navigate(...) }`.
- **Telas de detalhe começam vazias.** O banco demora um instante para responder, então o produto
  começa `null` e a tela mostra "Carregando..." até os dados chegarem.
- **Dois jeitos de criar pedido.** Dava para criar pedido pela aba Pedidos (formulário com cliente e
  checkboxes) e pelo Carrinho. Isso confundia e chegou a gerar pedido sem nome de cliente. Decidimos
  que o pedido só nasce pelo fluxo Cardápio → Carrinho. A aba Pedidos virou uma lista: abre o
  detalhe (onde fica o "Marcar como entregue", que é o Update) e tem o Remover (Delete). Também
  bloqueamos o login sem nome (mínimo de 3 letras) e a confirmação com o carrinho vazio.

## 5. Teste de persistência

Roteiro usado:

1. Cadastrar um produto no Cardápio.
2. Abrir o produto e editar nome/preço com "Salvar alterações".
3. Remover outro produto pelo "✕".
4. Criar um pedido (pelo Carrinho).
5. Fechar o app completamente (tirar dos apps recentes).
6. Abrir de novo e conferir que produtos e pedidos continuam lá.

**[PRINT AQUI: Cardápio com os produtos cadastrados]**

**[PRINT AQUI: Detalhe do produto editado]**

**[PRINT AQUI: lista de Pedidos]**

**[VÍDEO AQUI: fechando e reabrindo o app com os dados salvos]**

## 6. Dificuldades do trio

**[PREENCHER]**

---

# Histórico — Trabalho 2 (MAF)

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
