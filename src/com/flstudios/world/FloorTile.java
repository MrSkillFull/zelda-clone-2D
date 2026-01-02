// importando classe de mundo
package com.flstudios.world;

// importando bibliotecas java
import java.awt.image.BufferedImage;

/**
 * Tile de chão.
 *
 * Atualmente não adiciona comportamento extra; existe para diferenciar tipos
 * (ex.: para colisão/validações futuras) e deixar a leitura do mapa mais clara.
 */
public class FloorTile extends Tile {

	public FloorTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}
}
