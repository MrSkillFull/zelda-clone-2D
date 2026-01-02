// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.image.BufferedImage;

/**
 * Pickup de vida.
 *
 * Ao colidir com o jogador, recupera vida (ver Player.checkCollisionLifePack()).
 */
public class Lifepack extends Entity {

	public Lifepack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
}
