// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

// importando objetos próprios
import com.flstudios.main.Game;
import com.flstudios.world.Camera;

/**
 * Projétil disparado pelo jogador.
 *
 * É movimentado por um vetor direção (dx,dy) e tem tempo de vida limitado,
 * sendo removido automaticamente após alguns frames.
 */
public class BulletShoot extends Entity {

	// direção normalizada (ou quase) e velocidade
	private double dx, dy;
	private double speed = 4;
	
	// lifetime do projétil (em ticks)
	private int life = 45, currentLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		
	}

	/**
	 * Atualiza posição e controla expiração do projétil.
	 */
	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		currentLife++;
		if(currentLife == life) {
			Game.bullets.remove(this);
			return;
		}
		
	}
	
	/**
	 * Renderiza o projétil (simples: oval amarelo).
	 */
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
