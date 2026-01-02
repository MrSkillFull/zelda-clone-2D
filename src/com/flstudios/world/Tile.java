// importando classe de mundo
package com.flstudios.world;

// importando bibliotecas java
import java.awt.Graphics;
import java.awt.image.BufferedImage;

// importando objetos próprios
import com.flstudios.main.Game;

/**
 * Tile base do mundo.
 *
 * Um tile é um elemento estático do mapa (chão/parede) com posição fixa e sprite.
 */
public class Tile {

	// sprites padrão dos tiles (recortados do spritesheet)
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	
	// dados do tile
	private BufferedImage sprite; // sprite deste tile
	private int x, y; // posição em pixels no mundo
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	/**
	 * Renderiza o tile com offset da câmera.
	 */
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
