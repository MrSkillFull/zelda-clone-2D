// importando classe de gráficos
package com.flstudios.graficos;

// importando bibliotecas java
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Carregador de spritesheet.
 *
 * Responsabilidade única: carregar a imagem do classpath e fornecer recortes
 * (subimages) para entidades/tiles/UI.
 */
public class Spritesheet {

	// imagem base do spritesheet
	private BufferedImage spritesheet;
	
	/**
	 * @param path caminho do recurso no classpath (ex.: "/spritesheet.png")
	 */
	public Spritesheet(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retorna um recorte do spritesheet.
	 */
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
}
