# Changelog

Este projeto segue um formato simples inspirado em “Keep a Changelog”.

## v0.1.0 - 2026-01-02

### Added
- **Core:** Loop principal, `tick()`/`render()` e estados do jogo (`MENU`, `NORMAL`, `GAME_OVER`).
- **Jogador e entidades:** `Player`, `Entity`, `Enemy`, `Bullet`, `BulletShoot`, `Weapon`, `Lifepack`.
- **Mundo e tiles:** Sistema de mapa/tiles (`World`, `Tile`, `FloorTile`, `WallTile`, `Camera`).
- **Gráficos/UI:** `Spritesheet` e `UI`.
- **Menu e som:** `Menu` e `Sound` (música em loop).

### Changed
- **Inicial:** Primeiro estado público do projeto (MVP).

### Known issues / Notes
- Comparação de `String`: uso de `==` em `gameState` pode falhar; preferir `.equals(...)`.
- `stop()` está vazio: thread/recursos não são finalizados corretamente.

### Next steps recomendados
- Trocar comparações de `gameState` (`==`) por `.equals(...)`.
- Implementar `stop()` (parar loop, `join` da thread, liberar recursos).