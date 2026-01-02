// importando classe de gráficos
package com.flstudios.graficos;

// importando bibliotecas java
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

// importando objetos próprios
import com.flstudios.main.Game;

/**
 * UI/HUD do jogo.
 *
 * Responsável por desenhar informações do jogador na tela
 * (ex.: barra de vida e texto).
 */
public class UI {

	/**
	 * Renderiza a HUD.
	 */
	public void render(Graphics g) {
		// parâmetros de layout da barra de vida
		int x = 8;
		int y = 10;
		int w = 50;
		int h = 8;
		
		// fundo (vida total)
		g.setColor(Color.RED);
		g.fillRect(x, y,w, h);
		
		// preenchimento proporcional à vida atual
		g.setColor(Color.GREEN);
		g.fillRect(x, y, (int)((Game.player.life / Game.player.maxLife)*50), h);
		
		// texto numérico
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 10, 17);
		
	}
}
