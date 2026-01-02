<div align="center">

# 🗡️ Zelda Clone 2D (PixelSurvival)

Jogo 2D top-down em Java (AWT/Swing), com mapa tile-based e progressão por fases carregadas a partir de imagens (`.png`). Possui menu inicial, HUD (vida/munição/FPS), inimigos com IA simples e sistema de pickups.

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![AWT/Swing](https://img.shields.io/badge/AWT%2FSwing-UI-2F74C0?style=for-the-badge)
![Plataforma](https://img.shields.io/badge/Plataforma-Windows%20%7C%20macOS%20%7C%20Linux-3C873A?style=for-the-badge)
![Licen%C3%A7a](https://img.shields.io/badge/Licen%C3%A7a-MIT-000000?style=for-the-badge)

</div>

> 🎯 **Entry point:** `com.flstudios.main.Game` (método `main`).

## 🧰 Tecnologias

- **Java** (compatível com Java 8 / `JavaSE-1.8`)
- **AWT/Swing** (renderização e input)
- **Tilemap por imagem PNG** (fases em `res/`)
- **Áudio** (música e efeitos)

## 🧭 Sumário

- [✨ Recursos](#recursos)
- [🎮 Controles](#controles)
- [🔫 Regras de munição e recarga](#municao)
- [🗺️ Mapas e geração de fase](#mapas)
- [🧱 Estrutura do projeto](#estrutura)
- [⚙️ Requisitos](#requisitos)
- [▶️ Como executar](#executar)
- [📝 Limitações e notas](#notas)
- [🧩 Dicas para evoluir](#dicas)
- [📄 Licença](#licenca)

<a id="recursos"></a>
## ✨ Recursos

- **Movimentação** do jogador com colisão em paredes.
- **Câmera** seguindo o jogador.
- **Inimigos** perseguindo o jogador, causando dano por contato.
- **Tiros** (tecla X e botão direito do mouse), com tempo de vida limitado (bullets são destruídas após saírem da tela).
- **Pickups**:
  - *Weapon* (habilita arma e adiciona munição inicial).
  - *Bullet* (recarrega munição reserva).
  - *Lifepack* (cura vida aleatoriamente).
- **HUD**:
  - Barra de vida (canto superior esquerdo).
  - Munição atual/reserva (canto superior direito).
  - Contador de FPS (canto inferior direito).
- **Sistema de fases**: quando todos os inimigos morrem, o jogo carrega o próximo mapa.
- **Áudio**: música em loop e efeitos (coleta, tiro, hit, dano).

<a id="controles"></a>
## 🎮 Controles

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

<a id="municao"></a>
## 🔫 Regras de munição e recarga

- `ammoAtual`: munição no pente (inicia em `0`, máximo `10`).
- `ammoSafe`: munição reserva (inicia em `10`, máximo `60`).
- Quando `ammoAtual == 0` e há munição na reserva, a recarga acontece com uma pequena barra de progresso exibida acima do jogador.

<a id="mapas"></a>
## 🗺️ Mapas e geração de fase (level design)

As fases são imagens PNG em `res/` (ex.: `level1.png`, `level2.png`, `level3.png`). Cada pixel representa um tile/entidade conforme a cor.

### Cores suportadas

| Cor (ARGB em hex) | Significado |
|---|---|
| `0xFF000000` | Chão (Floor) |
| `0xFFFFFFFF` | Parede (Wall) |
| `0xFF0026FF` | Posição inicial do Player |
| `0xFFFF0000` | Inimigo |
| `0xFFFFD800` | Arma (Weapon) |
| `0xFFFF7F7F` | Kit de vida (Lifepack) |
| `0xFFFF6A00` | Munição (Bullet pickup) |

### Progressão de fases

- O jogo controla o número da fase via `CUR_LEVEL` e `MAX_LEVEL`.
- Ao zerar inimigos, carrega `level{N}.png` e reinicia as listas de entidades.

> Observação: existe `res/level4.png`, mas por padrão o jogo utiliza até `MAX_LEVEL = 3` (possibilidade de aumentar as fases no futuro).

<a id="estrutura"></a>
## 🧱 Estrutura do projeto

```
.
src/                     # código-fonte Java
  com/flstudios/
    entities/            # Player, Enemy, tiros e pickups
    graficos/            # Spritesheet e UI
    main/                # Game, Menu, Sound
    world/               # World, tiles e câmera
res/                     # sprites, mapas e áudio (classPath)
bin/                     # saída de compilação (Eclipse)
.project / .classpath    # metadados do Eclipse
```

<a id="requisitos"></a>
## ⚙️ Requisitos

- **Java 8** (o projeto está configurado para `JavaSE-1.8`).
- Windows/macOS/Linux (usa AWT/Swing; o exemplo de comandos abaixo está em Windows/PowerShell).

<a id="executar"></a>
## ▶️ Como executar

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

<a id="notas"></a>
## 📝 Limitações e notas

- **Carregar jogo**: a opção aparece no menu, mas não possui lógica implementada (ainda).
- **Continue/continuar**: quando o menu está em modo *pause*, o texto muda para *Continue*, mas a opção interna ainda é `novo jogo`.
- Se você ver textos com acentos quebrados (ex.: "munição"), garanta que os arquivos estão em **UTF-8** e compile com `-encoding UTF-8`.

<a id="dicas"></a>
## 🧩 Dicas para evoluir

- Para adicionar mais fases: coloque `levelN.png` em `res/` e ajuste `MAX_LEVEL` em `Game`.
- Para ajustar balanceamento: vida do inimigo e dano estão em `Enemy`, munição e recarga em `Player`.

<a id="licenca"></a>
## 📄 Licença

Este projeto está licenciado sob os termos da licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.