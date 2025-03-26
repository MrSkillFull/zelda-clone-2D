package com.flstudios.main;

	import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.flstudios.entities.BulletShoot;
import com.flstudios.entities.Enemy;
import com.flstudios.entities.Entity;
import com.flstudios.entities.Player;
import com.flstudios.graficos.Spritesheet;
import com.flstudios.graficos.UI;
import com.flstudios.world.World;

	public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

		public static final int WIDTH = 240;
		public static final int HEIGHT = 160;
		public static final int SCALE = 3;
		
		private int CUR_LEVEL = 1, MAX_LEVEL = 3;
		
		public static String natela;
		int frames = 0;
		public int fpsAtual;
		
		public static Random rand;
		
		public static JFrame frame;
		private Thread thread;
		private BufferedImage image;
		
		public static List<Entity> entities;
		public static List<Enemy> enemies;
		public static List<BulletShoot> bullets;
		public static Spritesheet spritesheet;
		
		
		public static Player player;
		
		public static World world;
		
		public UI ui;
		
		public Menu menu;
		
		public static String gameState = "MENU"; 
		private static boolean showMessageGameOver = true;
		private int currentFramesGameOver = 0, maxFramesGameOver = 30;
		private boolean restartGame = false;
		
		private boolean isRunning = false;
		
		public static void main(String[] args) {
			Game game = new Game();
			game.start();
		}
		
		public Game() {
			Sound.music.loop();
			ui = new UI();
			rand = new Random();
			addKeyListener(this);
			addMouseListener(this);
			initFrame();
			//inicializando objetos
			image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			entities = new ArrayList<Entity>();
			enemies = new ArrayList<Enemy>();
			bullets = new ArrayList<BulletShoot>();
			spritesheet = new Spritesheet("/spritesheet.png");
			player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 16, 16, 16));
			entities.add(player);
			world = new World("/level1.png");
			
			menu = new Menu();
		}
		
		public void initFrame() {
			this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			frame = new JFrame("PixelSurvival");
			frame.add(this);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		
		public synchronized void start() {
			thread = new Thread(this);
			isRunning = true;
			thread.start();
		}
		
		public synchronized void stop() {
			
		}
		
		public void tick() {
			if(gameState == "NORMAL") {
				this.restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			//System.out.println("Atualizando...");
			
			for(int i = 0; i<bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if(enemies.size() == 0) {
				//System.out.println("Proximo level");
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level" + CUR_LEVEL + ".png";
				world.restartGame(newWorld);
			}
		}else if(this.gameState == "MENU") {
			
			menu.tick();
			
		}else if(this.gameState == "GAME_OVER") {
				this.currentFramesGameOver++;
				if(this.currentFramesGameOver == this.maxFramesGameOver) {
					this.currentFramesGameOver = 0;
					if(this.showMessageGameOver) {
						this.showMessageGameOver = false;
					}else
						this.showMessageGameOver = true;
				}
			}
			
			if(restartGame == true && this.gameState == "GAME_OVER") {
				this.gameState = "NORMAL";
				this.restartGame = false;
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				world.restartGame(newWorld);
			}
			
	}
		
		public void render() {
			BufferStrategy bs = this.getBufferStrategy();
			if(bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			
			Graphics g = image.getGraphics();
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			world.render(g);
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.render(g);
			}
			
			for(int i = 0; i<bullets.size(); i++) {
				bullets.get(i).render(g);
			}
			if(gameState == "NORMAL") {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Arial", Font.ITALIC, 9));
			g.drawString(natela,WIDTH -35, HEIGHT);
			}
			ui.render(g);
			
			g.dispose();
			g = bs.getDrawGraphics();
			g.setColor(Color.white);
			g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Munição: "+ Player.ammoAtual + "/" + Player.ammoSafe, WIDTH*SCALE - 140, 20);
			
			if(gameState == "GAME_OVER") {
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
				
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 36));
				g.drawString("GAME OVER", (WIDTH*SCALE) / 2 - (36*SCALE ), (HEIGHT*SCALE) /2);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				if(this.showMessageGameOver) {
				g.drawString(">Pressione ENTER para continuar!",(WIDTH * SCALE) / 2 - (36 * SCALE) -70 , (HEIGHT*SCALE) / 2 + 40);
				}
			}else if(this.gameState == "MENU") {
				menu.render(g);
			}
			
			bs.show();
			//System.out.println("Renderizando...");
		}
		
		
		
		public void run() {
	
			requestFocus();
			long lastTime = System.nanoTime();
			double amountOFTicks = 60.0;
			double ns = 1000000000 / amountOFTicks;
			double delta = 0;
			
			double timer = System.currentTimeMillis();
		while(isRunning) {
			natela = "FPS: " + fpsAtual;
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta --;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				fpsAtual = frames;
			//	System.out.println("FPS: " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
			
			
			//System.out.println("jogo roda.");
		}
			
	}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.right = true;
				//System.out.println("direita");
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
				//System.out.println("esquerda");
			}
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
				//System.out.println("cima");
				
				if(gameState == "MENU") {
					menu.up = true;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
				//System.out.println("baixo");
				
				if(gameState == "MENU") {
					menu.down = true;
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_SHIFT ) {
				player.speed = 1.4;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.restartGame = true;
				if(gameState == "MENU") {
					menu.enter = true;
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState = "MENU";
				menu.pause = true;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.right = false;
				//System.out.println("direita");
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = false;
				//System.out.println("esquerda");
			}
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				player.up = false;
				//System.out.println("cima");
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = false;
				//System.out.println("baixo");
			}
			if(e.getKeyCode() == KeyEvent.VK_X ) {
				player.shoot = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_SHIFT ) {
				player.speed = 1.0;
			}
			
			
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			player.mouseShoot = true;
			player.mx = (e.getX() / SCALE);
			player.my = (e.getY() / SCALE);
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
				player.mouseShoot = false;	
			
	}

}
