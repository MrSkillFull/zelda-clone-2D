// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.image.BufferedImage;

/**
 * Pickup de arma.
 *
 * Ao colidir com o jogador, habilita o estado de arma e concede munição inicial
 * (ver Player.checkCollisionWeapon()).
 */
public class Weapon extends Entity {

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
