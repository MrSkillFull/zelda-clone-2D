// importando classe principal
package com.flstudios.main;

// importando bibliotecas java
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Menu principal do jogo.
 *
 * Responsabilidades:
 * - Controlar a seleção de opções (navegação via teclado)
 * - Renderizar a camada de menu por cima do jogo
 * - Direcionar o estado do jogo (ex.: iniciar/continuar ou sair)
 */
public class Menu {

	// opções e estado de seleção
	public String[] options = {"novo jogo", "carregar", "sair"}; // opções disponíveis no menu
	public int currentOption = 0; // índice da opção selecionada
	public int maxOption = options.length - 1; // índice máximo (última opção)
	
	// entrada (setada pelo Game via keyPressed)
	public boolean up, down, enter; // flags de navegação/seleção
	
	// controle de pausa (menu exibido ao apertar ESC)
	public boolean pause = false; // quando true, altera o texto de "novo jogo" para "continue"
	
	/**
	 * Atualiza o menu (navegação e seleção).
	 *
	 * Observação: as flags (up/down/enter) são consumidas aqui e resetadas.
	 */
	public void tick() {
		
		if(up) {
			currentOption--;
			up = false;
			
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			currentOption++;
			down = false;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[currentOption].equals("novo jogo") || options[currentOption].equals("continuar")) {
				Game.gameState = "NORMAL";
			}else if(options[currentOption].equals("sair")) {
				System.exit(1);
			}
		}
		
	}
	
	/**
	 * Renderiza o menu por cima da tela.
	 *
	 * @param g contexto gráfico de renderização
	 */
	public void render(Graphics g) {
		// layer (escurece o fundo)
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		// título
		g.setColor(Color.green);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString(">Pixel Survival<", Game.WIDTH, Game.HEIGHT /2);
		
		// opções do menu
			// novo jogo / continuar
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		if(pause == false) {
		g.drawString("Novo Jogo", Game.WIDTH /2, Game.HEIGHT *2);
		}else
			g.drawString("Continue", Game.WIDTH /2, Game.HEIGHT *2);
		
		// carregar jogo
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Carregar", (Game.WIDTH /2) + 30, (Game.HEIGHT *2) + 30);
		
		// sair do jogo
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Sair", (Game.WIDTH /2) + 30*2, (Game.HEIGHT *2) + 30*2);
		
		// indicador de seleção (seta)
		if(options[currentOption] == "novo jogo") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(">", Game.WIDTH / 3, Game.HEIGHT *2);
		}else if(options[currentOption] == "carregar") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(">", (Game.WIDTH /3) + 30, (Game.HEIGHT *2) + 30);
		}else if(options[currentOption] == "sair") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(">", (Game.WIDTH /3) + 30*2, (Game.HEIGHT *2) + 30*2);
			
		}
	}
	
}
