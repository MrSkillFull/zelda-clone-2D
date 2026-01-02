// importando classe de entidades
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// importando objetos próprios
import com.flstudios.main.Game;
import com.flstudios.main.Sound;
import com.flstudios.world.Camera;
import com.flstudios.world.World;

/**
 * Entidade inimiga.
 *
 * Implementa uma IA simples de perseguição ao jogador com:
 * - colisão com paredes e outros inimigos
 * - dano ao encostar no jogador (chance por tick)
 * - dano ao ser atingido por projéteis (BulletShoot)
 */
public class Enemy extends Entity {
	
	// movimentação
	private double speed = 0.4; // velocidade do inimigo
	
	// colisão (hitbox)
	private int maskx = 8, masky = 8, maskw=10, maskh = 10;
	
	// animação
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 1;
	
	// vida do inimigo (randômica para variar a dificuldade)
	private int life = Game.rand.nextInt(7) + 1;
	
	// feedback de dano (pisca por alguns frames)
	public boolean isDamaged = false;
	public int damageFrames = 10, damageCurrent = 0;
	
	// sprites do inimigo
	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(0,64,16,16);
		sprites[1] = Game.spritesheet.getSprite(16,64,16,16);
		
	}
	
	/**
	 * Atualização por frame do inimigo.
	 */
	public void tick() {
				// máscara ajustada (hitbox menor que o sprite)
				maskx = 6;
				masky = 6;
				maskw = 8;
				maskh = 10;
				
				// se não está colidindo com o jogador, tenta perseguir
				if(isColiddingWithPlayer() == false) {
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
				&& !isColidding((int)(x+speed), this.getY())) {
			x+=speed;
		}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
				&& !isColidding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY() && World.isFree(this.getX(),(int) (y+speed))
				&& !isColidding(this.getX(),(int) (y+speed))) {
			y+=speed;
		}else if((int)y > Game.player.getY() && World.isFree(this.getX(),(int) (y-speed))
				&& !isColidding(this.getX(),(int) (y-speed))) {
			y-=speed;
		}
				}else {
					// dano por contato (chance por tick)
					if(Game.rand.nextInt(100) < 10) {
						Sound.dano.play();
						Game.player.life -= Game.rand.nextInt(3);	
						Game.player.isDamaged = true;
					}
					
				}
			//System.out.println(index);
			// animação do inimigo
			frames++;
			maxFrames = 20;
			if(frames >= maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			
			// colisão com tiros
			collindBullet();
			
			// morte
			if(life <=0) {
				destroySelf();
			}
			
			// feedback de dano
			if(isDamaged == true) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
	}
	
	/**
	 * Remove o inimigo das listas globais.
	 */
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	/**
	 * Verifica colisão com projéteis do jogador.
	 */
	public void collindBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColidding(this, e)) {
					Sound.hit.play();
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
				
			}
		}
	}
	
	/**
	 * Colisão simples com o jogador (retângulos).
	 */
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		
		
		return enemyCurrent.intersects(player);
	}
	
	/**
	 * Evita que inimigos se sobreponham (colisão inimigo-inimigo).
	 */
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) 
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renderiza o inimigo e seu feedback de dano.
	 */
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
