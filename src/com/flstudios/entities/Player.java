// importando classe do jogador
package com.flstudios.entities;

// importando bibliotecas java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

// importando objetos próprios
import com.flstudios.main.Game;
import com.flstudios.main.Sound;
import com.flstudios.world.Camera;
import com.flstudios.world.World;

/**
 * Entidade do jogador.
 *
 * Responsabilidades:
 * - Movimentação + animação (direita/esquerda)
 * - Vida e feedback de dano
 * - Coleta de itens (vida/munição/hasGun)
 * - Disparo (tecla X e mouse) + recarga
 */
public class Player extends Entity {

	// input/movimento
	public boolean right, left, up, down; // flags setadas pelo Game via teclado
	public int right_dir = 0, left_dir = 1, dir = right_dir; // direções lógicas e direção atual
	public double walkSpeed = 0.7, runSpeed = 1.0, speed = walkSpeed; // velocidade atual, velocidade de caminhada e velocidade de corrida
	public boolean isRunning = false; // indica se o jogador está correndo
	private int minStamina = 0, maxStamina = 100, curStamina = maxStamina; // estamina mínima, máxima e atual

	// animação
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3; // controle de frames/índice
	
	// vida/dano
	public double life = 100, maxLife = 100; // vida atual e máxima
	private boolean moved = false; // indica se o jogador se moveu neste tick
	
	// sprites (animações e feedback)
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage playerDamageRight;
	private BufferedImage playerDamageLeft;
	
	// munição (estado global exibido na HUD)
	public static int ammoAtual = 0, ammoAtualMax = 10,ammoSafe = 10, ammoSafeMax = 20,  maxAmmo = ammoAtualMax + ammoSafeMax;
	public static boolean reloading = false; // indica se está recarregando (usado para desenhar barra)
	public static int currentFramesReloading = 0, maxFramesReloading = 60; // controle da barra de recarga
	
	// combate
	public boolean hasGun; // indica se possui a arma
	public boolean shoot = false; // disparo via teclado
	public boolean mouseShoot = false; // disparo via mouse
	
	// feedback de dano
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	// posição do mouse (coordenadas na tela com escala aplicada no Game)
	public int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		// carrega frames do spritesheet
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
	
	/**
	 * Atualização por frame do jogador.
	 */
	public void tick() {

		// estado de recarga (exibido na render)
		if(ammoAtual == 0 && ammoSafe > 0 && hasGun) {
			reloading = true;
		}
		
		// movimentação + colisão com paredes
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
		
		// animação de caminhada
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
		
		// feedback de dano
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 30) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		// colisões com pickups
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionWeapon();
		
			
		// disparo via teclado (X)
			if( hasGun == true && ammoAtual > 0 && shoot == true) {
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
		
		// disparo via mouse (calcula direção pelo ângulo)
		if(hasGun && ammoAtual > 0 && mouseShoot) {
			mouseShoot = false;
			Sound.shoot.play();
			//System.out.println(angle);
			
			
			if( hasGun == true && ammoAtual > 0) {
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
		
		// condição de game over
		if(life <= 0) {
			//Game Over!
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		// câmera segue o jogador, limitada ao tamanho do mapa
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0,World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0,World.HEIGHT*16 - Game.HEIGHT) ;	
		
		
	}
	
	/**
	 * Coleta de lifepack: recupera uma quantidade randômica de vida.
	 */
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
	
	/**
	 * Coleta de munição: adiciona munição ao estoque (ammoSafe).
	 */
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
					//System.out.println("Muni��o Atual: "+ammo);
				}
				
			}
		}
	}
	
	/**
	 * Coleta de arma: habilita o estado hasGun e dá munição inicial.
	 */
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					Sound.collect.play();
					Game.entities.remove(atual);
					hasGun = true;
					ammoAtual+=5;
				}
			}
		}
	}
	
	
	
	
	/**
	 * Renderização do jogador (sprites + arma + barra de recarga).
	 */
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) 
			{
				if(hasGun == true) {
					//desenhar hasGun para a direita.
					g.drawImage(WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() + 3 - Camera.y, null );
				}
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				
			}
			else if(dir == left_dir) 
			{
				if(hasGun == true) {
					//desenhar hasGun para a esquerda.
					g.drawImage(WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() + 3 - Camera.y, null );
				}
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
		
		// feedback de dano
		else {
			if(dir == right_dir) 
			{
				g.drawImage(playerDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			else if(dir == left_dir)
			{
				g.drawImage(playerDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			if(hasGun == true) 
			{
				if(dir == right_dir) 
				{
					g.drawImage(WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() + 3 - Camera.y, null );
					g.drawImage(playerDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
				else if(dir == left_dir)
				{
					g.drawImage(WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() + 3 - Camera.y, null );
					g.drawImage(playerDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
		
		// barra de recarga (quando jogador está recarregando)
		if(Game.player.reloading == true) {
			g.setColor(Color.red);
			g.fillRect(this.getX() - Camera.x, this.getY() - 5 - Camera.y, this.maxFramesReloading /3,5);
			g.setColor(Color.yellow);
			g.fillRect(this.getX() - Camera.x, this.getY() - 5 - Camera.y, this.currentFramesReloading /3,5);
			this.currentFramesReloading++;
			if(this.currentFramesReloading == this.maxFramesReloading && ammoSafe != 0) {
			
				// completar munição atual a partir do estoque seguro
				int ammoParaRecarregar = 0;
				if((ammoSafe + ammoAtual) >= 10)
				{
					ammoParaRecarregar = 10 - ammoAtual;
					ammoAtual	+= 	ammoParaRecarregar;
					ammoSafe	-= 	ammoParaRecarregar;
				}else
				{
					ammoParaRecarregar = ammoSafe;
					ammoAtual += ammoParaRecarregar;
				}
			
				this.currentFramesReloading = 0;
				reloading = false;
				shoot = false;
				mouseShoot = false;
			}
		}

		// barra de estamina (quando jogador está correndo)
		if(isRunning)
		{
			speed = runSpeed;
			curStamina--;

			if(curStamina <= minStamina)
			{
				curStamina = minStamina;
				speed = walkSpeed;
				isRunning = false;
			}

			// background da barra de estamina
			g.setColor(Color.red);
			g.fillRect(this.getX() - Camera.x, this.getY() - 10 - Camera.y,   (maxStamina * (maxFramesReloading /3)) / maxStamina,5);

			// barra de estamina atual
			g.setColor(Color.blue);
			g.fillRect(this.getX() - Camera.x, this.getY() - 10 - Camera.y,  (curStamina * (maxFramesReloading /3)) / maxStamina ,5);
		}
		else
		{
			curStamina++;
			if(curStamina > maxStamina)
			{
				curStamina = maxStamina;
				speed = walkSpeed;
			}
		}
	}
}
