// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.image.BufferedImage;

/**
 * Pickup de munição.
 *
 * Ao colidir com o jogador, aumenta a munição reserva (ammoSafe)
 * (ver Player.checkCollisionAmmo()).
 */
public class Bullet extends Entity {

	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
