package com.flstudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.flstudios.main.Game;
import com.flstudios.main.Sound;
import com.flstudios.world.Camera;
import com.flstudios.world.World;

public class Enemy extends Entity {
	
	private double speed = 0.4;
	private int maskx = 8, masky = 8, maskw=10, maskh = 10; 
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 1;
	
	private int life = Game.rand.nextInt(7) + 1;
	
	public boolean isDamaged = false;
	public int damageFrames = 10, damageCurrent = 0;
	
	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(0,64,16,16);
		sprites[1] = Game.spritesheet.getSprite(16,64,16,16);
		
	}
	public void tick() {
				maskx = 6;
				masky = 6;
				maskw = 8;
				maskh = 10;
				
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
					if(Game.rand.nextInt(100) < 10) {
						Sound.dano.play();
						Game.player.life -= Game.rand.nextInt(3);	
						Game.player.isDamaged = true;
						//System.out.println("Vida = "+ Game.player.life);
					}
					
				}
			//System.out.println(index);
			frames++;
			maxFrames = 20;
			if(frames >= maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			
			collindBullet();
			if(life <=0) {
				destroySelf();
			}
			
			if(isDamaged == true) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
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
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		
		
		return enemyCurrent.intersects(player);
	}
	
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
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
}
