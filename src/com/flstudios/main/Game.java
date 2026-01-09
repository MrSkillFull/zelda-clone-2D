// importando classe principal
package com.flstudios.main;

// importando bibliotecas java
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

// importando objetos próprios
import com.flstudios.entities.BulletShoot;
import com.flstudios.entities.Enemy;
import com.flstudios.entities.Entity;
import com.flstudios.entities.Player;
import com.flstudios.graficos.Spritesheet;
import com.flstudios.graficos.UI;
import com.flstudios.world.World;

	// classe principal do jogo
	public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

		// constantes de tela
		public static final int WIDTH = 240, HEIGHT = 160, SCALE = 3;    // definindo largura, altura e escala da tela do jogo

		// exibição de FPS
		public static String strFPS;                                    // string para mostrar o FPS na tela
		private int frames = 0;                                         // contador de frames
		private int fpsAtual;                                           // frames por segundo atual

		// estado do jogo
		public static String gameState = "MENU";                      // estado atual do jogo

		// objetos estáticos compartilhados
		public static Random rand;                                      // objeto para geração de números randômicos
		private static JFrame frame;                                    // janela principal do jogo
		private Thread thread;                                          // thread principal para execução do loop do jogo
		private BufferedImage imgLayer;                                 // camada de imagem bufferizada para renderização

		// coleções de entidades e recursos
		public static List<Entity> entities;                            // lista de entidades do jogo
		public static List<Enemy> enemies;                              // lista de inimigos do jogo
		public static List<BulletShoot> bullets;                        // lista de projéteis do jogador
		public static Spritesheet spritesheet;                          // spritesheet contendo sprites do jogo

		// objetos de jogo (instância)
		public static Player player;                                    // objeto jogador
		public static World world;                                      // objeto de geração do mundo/level
		public UI ui;                                                   // interface do usuário
		public Menu menu;                                               // menu do jogo

		// controle de níveis e reinício
		private int CUR_LEVEL = 1, MAX_LEVEL = 4;                       // nível atual e nível máximo do jogo
		private int currentFramesGameOver = 0, maxFramesGameOver = 30;  // controle de animação da mensagem de game over (frames)
		private boolean restartGame = false;                            // indica se o jogo deve reiniciar

		// controle do laço principal
		private boolean isRunning = false;                              // controle do laço principal do jogo
		private static boolean showMessageGameOver = true;              // flag para alternar a exibição da mensagem de game over
		
		// método principal
		public static void main(String[] args) {
			Game game = new Game();
			game.start();
		}
		
		// Método construtor
		public Game() {
			// inicializa a janela do jogo e configurações básicas da tela
			// ( cria e adiciona este Canvas ao JFrame )
			initFrame();
			ui = new UI();
			rand = new Random();
			addKeyListener(this);
			addMouseListener(this);
			
			//inicializando objetos
			imgLayer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			entities = new ArrayList<Entity>();
			enemies = new ArrayList<Enemy>();
			bullets = new ArrayList<BulletShoot>();
			spritesheet = new Spritesheet("/spritesheet.png");
			player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 16, 16, 16));
			entities.add(player);
			world = new World("/level1.png");
			menu = new Menu();
			Sound.music.loop();
		}
		
		public void initFrame() {
			// configura as dimensões do Canvas e cria a janela principal
			// define tamanho, adiciona o Canvas, centraliza e exibe
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
			// inicia a thread principal do jogo e comuta o flag de execução
			thread = new Thread(this);
			isRunning = true;
			thread.start();
		}
		
		public synchronized void stop() 
		{
			// para a execução do laço principal de forma segura e aguarda a
			// finalização da thread antes de continuar
			isRunning = false;
			try 
			{
				thread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			} 	
		}
		
		public void tick() {
			// atualiza a lógica do jogo conforme o estado atual
			if(gameState.equals("NORMAL")) 
			{
				this.restartGame = false;
				for(int i = 0; i < entities.size(); i++) 
				{
					Entity e = entities.get(i);
					e.tick();
				}
			
				for(int i = 0; i<bullets.size(); i++) 
				{
					bullets.get(i).tick();
				}
			
				if(enemies.size() == 0) {
					CUR_LEVEL++;
					if(CUR_LEVEL > MAX_LEVEL) 
					{
						CUR_LEVEL = 1;
					}
					String newWorld = "level" + CUR_LEVEL + ".png";
					world.restartGame(newWorld);
				}
			}

			else if(this.gameState.equals("MENU")) 
			{
				// atualiza o menu quando o jogo está no estado de menu
				menu.tick();
			}

			else if(this.gameState.equals("GAME_OVER")) 
			{
				this.currentFramesGameOver++;
				if(this.currentFramesGameOver == this.maxFramesGameOver) 
				{
					this.currentFramesGameOver = 0;
					if(this.showMessageGameOver) {
						this.showMessageGameOver = false;
					}else
						this.showMessageGameOver = true;
				}
			}
			
			if(restartGame == true && this.gameState.equals("GAME_OVER")) 
			{
				// reinicia o jogo após a tela de GAME OVER quando solicitado
				this.gameState = "NORMAL";
				this.restartGame = false;
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				world.restartGame(newWorld);
			}
			
		}
		
		public void render() 
		{
			// gerencia a renderização do jogo conforme o estado atual
			BufferStrategy bs = this.getBufferStrategy();
			if(bs == null) 
			{
				this.createBufferStrategy(3);
				return;
			}
			
			Graphics g = imgLayer.getGraphics();
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			world.render(g);
			for(int i = 0; i < entities.size(); i++) 
			{
				Entity e = entities.get(i);
				e.render(g);
			}
			
			for(int i = 0; i<bullets.size(); i++) 
			{
				bullets.get(i).render(g);
			}
			if(gameState.equals("NORMAL"))
			{
				g.setColor(Color.GREEN);
				g.setFont(new Font("Arial", Font.ITALIC, 9));
				g.drawString(strFPS,WIDTH -35, HEIGHT);
			}
			ui.render(g);
			
			g.dispose();
			g = bs.getDrawGraphics();
			g.setColor(Color.white);
			g.drawImage(imgLayer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Muni\u00E7\u00E3o: "+ Player.ammoAtual + "/" + Player.ammoSafe, WIDTH*SCALE - 140, 20);
			
			if(gameState.equals("GAME_OVER")) {
				// desenha a sobreposição de GAME OVER e mensagem piscante
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 36));
				g.drawString("GAME OVER", (WIDTH*SCALE) / 2 - (36*SCALE ), (HEIGHT*SCALE) /2);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				if(this.showMessageGameOver) {
					// instrução para o jogador reiniciar
					g.drawString(">Pressione ENTER para continuar!",(WIDTH * SCALE) / 2 - (36 * SCALE) -70 , (HEIGHT*SCALE) / 2 + 40);
				}
			}else if(this.gameState.equals("MENU")) 
			{
				// quando em MENU, delega a renderização ao objeto Menu
				menu.render(g);
			}
			
			bs.show();
		}
		
		
		
		public void run() 
		{
			// solicita foco para receber eventos de teclado
			requestFocus();

			// inicialização do loop do jogo com controle de atualização
			long lastTime = System.nanoTime();
			double amountOFTicks = 60.0;
			double ns = 1000000000 / amountOFTicks;
			double delta = 0;
			double timer = System.currentTimeMillis();
		
			while(isRunning) {
				// atualiza string de FPS para exibição
				strFPS = "FPS: " + fpsAtual;
				long now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;
			
				// executa tick+render em taxa fixa (≈60Hz)
				if(delta >= 1) {
					tick();
					render();
					frames++;
					delta --;
				}
			
				// atualiza contador de FPS a cada segundo
				if(System.currentTimeMillis() - timer >= 1000) {
					fpsAtual = frames;
					frames = 0;
					timer = System.currentTimeMillis();
				}
			}
			
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) 
		{
			player.right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) 
		{
			player.up = true;
			
			if(gameState.equals("MENU")) {
				menu.up = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) 
		{
			player.down = true;
			if(gameState.equals("MENU")) {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_X ) 
		{
			player.shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT ) {
			player.speed = 1.4;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) 
		{
			this.restartGame = true;
			if(gameState.equals("MENU")) {
				menu.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
		{
			gameState = "MENU";
			menu.pause = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_R && player.hasGun == true && player.ammoAtual < 10 && player.ammoSafe > 0) 
		{
			player.reloading = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) 
		{
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) 
		{
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
		{
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) 
		{
			player.down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_X ) 
		{
			player.shoot = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT ) 
		{
			player.speed = 1.0;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		player.mouseShoot = true;
		player.mx = (e.getX() / SCALE);
		player.my = (e.getY() / SCALE);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
			player.mouseShoot = false;	
	}
}
