// importando classe do mundo/level
package com.flstudios.world;

// importando bibliotecas java
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

// importando objetos próprios
import com.flstudios.entities.Bullet;
import com.flstudios.entities.Enemy;
import com.flstudios.entities.Entity;
import com.flstudios.entities.Lifepack;
import com.flstudios.entities.Player;
import com.flstudios.entities.Weapon;
import com.flstudios.graficos.Spritesheet;
import com.flstudios.main.Game;

/**
 * Representa o mundo (mapa) do jogo.
 *
 * O level é carregado a partir de uma imagem (PNG) onde a cor de cada pixel
 * define o tipo de tile e/ou spawn de entidades (player, inimigos e itens).
 */
public class World {

	// tiles e dimensões do mapa
	public static Tile[] tiles; // grid linearizado (x + y*WIDTH)
	public static int WIDTH, HEIGHT; // dimensões do mapa em tiles
	
	// constantes do mundo
	public static final int TILE_SIZE = 16; // tamanho de cada tile em pixels
	
	/**
	 * Carrega o mapa a partir do recurso informado (ex.: "/level1.png").
	 *
	 * @param path caminho do recurso do mapa no classpath
	 */
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			// varre o mapa pixel-a-pixel e cria tiles/entidades conforme a cor
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels [xx+yy*map.getWidth()];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						// Floor (preto)
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF) {
						// Wall / parede (branco)
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16,yy*16, Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF0026FF) {
						// Player (azul)
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFFFF0000) {
						// Enemy (vermelho)	
						Enemy en = new Enemy(xx*16, yy*16, 16,16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if(pixelAtual == 0xFFFFD800){
						// Weapon (amarelo)
						Game.entities.add(new Weapon(xx*16, yy*16, 16,16, Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFFFF7F7F) {
						// Lifepack (rosa)
						Lifepack pack = new Lifepack(xx*16, yy*16, 16,16, Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					}else if(pixelAtual == 0xFFFF6A00) {
						// Bullet (laranja)
						Game.entities.add(new Bullet(xx*16, yy*16, 16,16, Entity.BULLET_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Verifica se uma posição (x,y) é livre para movimentação (sem colisão com parede).
	 * A checagem considera os quatro cantos do retângulo do player.
	 */
	public static boolean isFree(int xnext, int ynext){
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4= (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	/**
	 * Reinicia o jogo/mundo carregando um novo level.
	 *
	 * Observação: recria listas e objetos globais (entities/enemies/player/world).
	 *
	 * @param level nome do arquivo (ex.: "level1.png")
	 */
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
	}
	
	/**
	 * Renderiza apenas a área visível do mundo (culling simples) com base na câmera.
	 */
	public void render(Graphics g) {
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)	
					continue;
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
