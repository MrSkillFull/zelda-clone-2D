# Zelda Clone 2D (PixelSurvival)

Jogo 2D top-down em Java (AWT/Swing), com mapa tile-based e progressão por fases carregadas a partir de imagens (`.png`). O projeto possui menu inicial, HUD de vida/munição, inimigos com IA simples para perseguição e sistema de pickups para coleta de vida, arma e munição.

> **Entry point:** `com.flstudios.main.Game` (método `main`).

## Recursos (features)

- **Movimentação** do jogador com colisão em paredes.
- **Câmera** seguindo o jogador.
- **Inimigos** perseguindo o jogador, causando dano por contato.
- **Tiros** (tecla X e botão direito do mouse), com tempo de vida limitado. (as bullets são destruídas após sairem da tela)
- **Pickups**:
  - *Weapon* (habilita arma e adiciona munição inicial).
  - *Bullet* (recarrega munição reserva).
  - *Lifepack* (cura vida aleatoriamente).
- **HUD**:
  - Barra de vida (canto superior esquerdo).
  - Munição atual/reserva (canto superior direito).
  - Contador de FPS (canto inferior direito).
- **Sistema de fases**: quando todos os inimigos morrem, o jogo carrega o próximo mapa.
- **áudio**: música em loop e efeitos (coleta, tiro, hit, dano).

## Controles

**Gameplay**

- **Mover:** `WASD` ou `Setas`
- **Correr:** `Shift` (aumenta velocidade enquanto pressionado, simulando uma corrida)
- **Atirar (reta, baseado na direção do personagem):** `X` (dispara ao soltar a tecla)
- **Atirar (mirando):** `Clique do mouse` (atira na direção do clique)
- **Menu/Pause:** `Esc`

**Menu**

- **Navegar:** `Cima/Baixo` (ou `W/S`)
- **Selecionar:** `Enter`

**Game Over**

- **Continuar/Reiniciar:** `Enter`

## Regras de munição e recarga

- `ammoAtual`: munição no pente (inicia em `0`, máximo `10`).
- `ammoSafe`: munição reserva (inicia em `10`, máximo `60`).
- Quando `ammoAtual == 0` e há munição na reserva, a recarga acontece com uma pequena barra de progresso exibida acima do jogador.

## Mapas e geração de fase (level design)

As fases são imagens PNG em `res/` (ex.: `level1.png`, `level2.png`, `level3.png`). Cada pixel representa um tile/entidade conforme a cor.

### Cores suportadas

| Cor (ARGB em hex) | Significado |
|---|---|
| `0xFF000000` | Cháo (Floor) |
| `0xFFFFFFFF` | Parede (Wall) |
| `0xFF0026FF` | Posição inicial do Player |
| `0xFFFF0000` | Inimigo |
| `0xFFFFD800` | Arma (Weapon) |
| `0xFFFF7F7F` | Kit de vida (Lifepack) |
| `0xFFFF6A00` | munição (Bullet pickup) |

### Progressão de fases

- O jogo controla o número da fase via `CUR_LEVEL` e `MAX_LEVEL`.
- Ao zerar inimigos, carrega `level{N}.png` e reinicia as listas de entidades.

> Observação: existe `res/level4.png`, mas por padrão o jogo utiliza até `MAX_LEVEL = 3`. (possibilidade de aumentar as fases no futuro)

## Estrutura do projeto

```
.
 src/                     # código-fonte Java
    com/flstudios/
        entities/        # Player, Enemy, tiros e pickups
        graficos/        # Spritesheet e UI
        main/            # Game, Menu, Sound
        world/           # World, tiles e camera
 res/                     # sprites, mapas e áudio (classPath)
 bin/                     # saída de compilação (Eclipse)
 .project / .classpath     # metadados do Eclipse
```

## Requisitos

- **Java 8** (o projeto está configurado para `JavaSE-1.8`).
- Windows/macOS/Linux (usa AWT/Swing; o exemplo de comandos abaixo está em Windows/PowerShell).

## Como executar

### Opção A) Eclipse (recomendado)

1. `File > Import... > Existing Projects into Workspace`
2. Selecione a pasta do projeto.
3. Rode a classe `com.flstudios.main.Game`.

> O `res/` já está configurado como source folder no `.classpath`, então os assets são encontrados via `getResource("/...")`.

### Opção B) Linha de comando (PowerShell)

Na raiz do projeto:

1) Compilar:

```powershell
$src = Get-ChildItem -Recurse -Filter *.java -Path .\src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d .\bin -cp .\res $src
```

2) Executar:

```powershell
java -cp "bin;res" com.flstudios.main.Game
```

## Limitações e notas

- **Carregar jogo**: a Opção aparece no menu, mas não possui lógica implementada. (ainda)
- **Continue/continuar**: quando o menu está em modo `pause`, o texto muda para *Continue*, mas a Opção interna ainda é `novo jogo`.
- Se você ver textos com acentos quebrados (ex.: "munição"), garanta que os arquivos estáo em **UTF-8** e compile com `-encoding UTF-8`.

## Dicas para evoluir

- Para adicionar mais fases: coloque `levelN.png` em `res/` e ajuste `MAX_LEVEL` em `Game`.
- Para ajustar balanceamento: vida do inimigo e dano estáo em `Enemy`, munição e recarga em `Player`.

## Licença

Este projeto está licenciado sob os termos da licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.