package com.flstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.flstudios.graficos.Spritesheet;
import com.flstudios.main.Game;
import com.flstudios.main.Sound;
import com.flstudios.world.Camera;
import com.flstudios.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.0;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	public double life = 100, maxLife = 100;
	private int maskx = 10, masky = 10, maskw = 10, maskh = 10;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamageRight;
	private BufferedImage playerDamageLeft;
	
	public static int ammoAtual = 0,ammoAtualMax = 10,ammoSafe = 10, ammoSafeMax = 60,  maxAmmo = ammoAtualMax + ammoSafeMax;
	public static boolean reloading = false;
	public static int currentFramesReloading = 0, maxFramesReloading = 60;
	
	public boolean arma;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public int mx, my;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamageRight = Game.spritesheet.getSprite(0, 16, 16, 16);
		playerDamageLeft = Game.spritesheet.getSprite(16, 16, 16, 16);
		for(int i = 0; i < 4; i++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(96+(i*16), 16, 16, 16);
			}
	}
	
	public void tick() {
		reloading = false;
		if(ammoAtual == 0 && ammoSafe > 0 && arma) {
			reloading = true;
		}else
			reloading = false;
		
		
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y-=speed;
		}else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			y+=speed;
		}
		
		
		if(moved) {
			//System.out.println(index);
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 30) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionWeapon();
		
			
			if( arma == true && ammoAtual > 0 && shoot == true) {
				shoot = false;
				Sound.shoot.play();
				ammoAtual--;
			int dx= 0;
			int px = 0;
			int py = 7;
			if(dir == right_dir) {
				px = 20;
				dx = 1;
			}else {
				dx = -1;
				px = -8;
			}
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
			Game.bullets.add(bullet);
			}
		
		if(arma && ammoAtual > 0 && mouseShoot) {
			mouseShoot = false;
			Sound.shoot.play();
			//System.out.println(angle);
			
			
			if( arma == true && ammoAtual > 0) {
				ammoAtual--;
				
			int px = 0, py = 7;
			double angle = 0;
			
			if(dir == right_dir) {
				px = 20;
				 angle = Math.atan2(my - (this.getY() + py - Camera.y),mx - (this.getX() + px - Camera.x));
			}else {
				px = -8;
				 angle = Math.atan2(my - (this.getY() + py - Camera.y),mx - (this.getX() + px - Camera.x));
			}
			double dx= Math.cos(angle);
			double dy = Math.sin(angle);
			
			
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			}
			
			
		}
		
		if(life <= 0) {
			//Game Over!
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0,World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0,World.HEIGHT*16 - Game.HEIGHT) ;	
		
		
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack && life < 100) {
				if(Entity.isColidding(this, atual)) {
					Sound.collect.play();
					int ganhouVida = Game.rand.nextInt(21);
					if(ganhouVida == 0) 
						ganhouVida = 1;
					life += ganhouVida;
					Game.entities.remove(atual);
					if(life > 100)
						life = 100;
				}
				
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet && ammoSafe < ammoSafeMax) {
				if(Entity.isColidding(this, atual)) {
					Sound.collect.play();
					Game.entities.remove(atual);
					ammoSafe+=10;
					if(ammoSafe > ammoSafeMax) {
						ammoSafe = ammoSafeMax;
					}
					//System.out.println("Munição Atual: "+ammo);
				}
				
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					Sound.collect.play();
					Game.entities.remove(atual);
					arma = true;
					ammoAtual+=5;
				}
			}
		}
	}
	
	
	
	
	public void render(Graphics g) {
		if(!isDamaged) {
		if(dir == right_dir) {
			if(arma == true) {
				//desenhar arma para a direita.
				g.drawImage(WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() + 3 - Camera.y, null );
			}
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			
		}else if(dir == left_dir) {
			if(arma == true) {
				//desenhar arma para a esquerda.
				g.drawImage(WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() + 3 - Camera.y, null );
			}
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			
			}
		}else {
			if(dir == right_dir) {
			g.drawImage(playerDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else if(dir == left_dir){
				g.drawImage(playerDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			if(arma == true) {
				if(dir == right_dir) {
				g.drawImage(WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() + 3 - Camera.y, null );
				g.drawImage(playerDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else if(dir == left_dir){
					g.drawImage(WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() + 3 - Camera.y, null );
					g.drawImage(playerDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}
			
		}
		
		if(Game.player.reloading == true) {
			g.setColor(Color.red);
			g.fillRect(this.getX() - Camera.x, this.getY() - 5 - Camera.y, this.maxFramesReloading /3,5);
			g.setColor(Color.yellow);
			g.fillRect(this.getX() - Camera.x, this.getY() - 5 - Camera.y, this.currentFramesReloading /3,5);
			this.currentFramesReloading++;
			if(this.currentFramesReloading == this.maxFramesReloading && ammoSafe >= 10) {
				ammoAtual+=10;
				ammoSafe-=10;
				this.currentFramesReloading = 0;
			}else if(Game.player.currentFramesReloading == Game.player.maxFramesReloading && ammoSafe < 10) {
				ammoAtual += ammoSafe;
				ammoSafe -= ammoSafe;
				this.currentFramesReloading = 0;
			}
		}
		
	}
}
