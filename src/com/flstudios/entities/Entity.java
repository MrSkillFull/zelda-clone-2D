// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// importando objetos próprios
import com.flstudios.main.Game;
import com.flstudios.world.Camera;

/**
 * Classe base para qualquer entidade do jogo.
 *
 * Uma entidade possui:
 * - posição e tamanho
 * - sprite de renderização
 * - máscara de colisão (para separar hitbox do tamanho do sprite)
 */
public class Entity {

	// máscara de colisão (hitbox)
	public int maskx, masky, mwidth, mheight;
	
	// sprites compartilhados (recortados do spritesheet)
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(0, 3*16, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(0, 2*16, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(16, 3*16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(9*16,4*16,16,16);
	public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(16, 2*16, 16,16);
	public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(2*16, 2*16, 16,16);
	
	// transform (posição/tamanho)
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	// render
	private BufferedImage sprite; // sprite atual da entidade
	
	/**
	 * Construtor base.
	 */
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	/**
	 * Define a máscara (hitbox) da entidade.
	 */
	public void setMask(int maskx, int masky, int maskwidth, int maskheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = maskwidth;
		this.mheight = maskheight;
	}
	
	// setters/getters de posição e dimensões
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Atualização por frame (game loop). Sobrescreva nas subclasses.
	 */
	public void tick() {
		
	}
	
	/**
	 * Verifica colisão entre duas entidades usando suas máscaras.
	 */
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky,  + e1.mwidth,  + e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky,  + e2.mwidth,  + e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	/**
	 * Renderização base: desenha o sprite com offset da câmera.
	 */
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	//	g.setColor(Color.red);
	//	g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
	}
}
