// importando classe de mundo
package com.flstudios.world;

// importando bibliotecas java
import java.awt.image.BufferedImage;

/**
 * Tile de parede (sólido).
 *
 * Em geral é usado como obstáculo: o método World.isFree(...) trata WallTile
 * como colisão.
 */
public class WallTile extends Tile {

	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}
}
