# Changelog

Este projeto segue um formato simples inspirado em “Keep a Changelog”.

## v0.1.1 - 2026-01-08

### Fixed
- **Core:** Substituídas comparações de `gameState` usando `==` por `String.equals(...)` para evitar falhas de comparação.
- **Core:** Implementado `stop()` para encerrar o loop principal, realizar `join()` da thread e liberar recursos adequadamente.
- **Confiabilidade:** Correção do encerramento de threads/recursos para evitar hangs na finalização da aplicação.

### Changed
- **Documentação:** Atualizadas notas e recomendações no changelog para refletir as correções.

## v0.1.0 - 2026-01-02

### Added
- **Core:** Loop principal, `tick()`/`render()` e estados do jogo (`MENU`, `NORMAL`, `GAME_OVER`).
- **Jogador e entidades:** `Player`, `Entity`, `Enemy`, `Bullet`, `BulletShoot`, `Weapon`, `Lifepack`.
- **Mundo e tiles:** Sistema de mapa/tiles (`World`, `Tile`, `FloorTile`, `WallTile`, `Camera`).
- **Gráficos/UI:** `Spritesheet` e `UI`.
- **Menu e som:** `Menu` e `Sound` (música em loop).

### Changed
- **Inicial:** Primeiro estado público do projeto (MVP).

## Known issues / Notes

- Melhorias incrementais de performance e refatoração pendentes.

### Next Steps

- Refatoração do código e análise de desempenho e melhoria de qualidade.