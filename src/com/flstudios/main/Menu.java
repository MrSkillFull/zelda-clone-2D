package com.flstudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = {"novo jogo", "carregar", "sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
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
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
			}else if(options[currentOption] == "sair") {
				System.exit(1);
			}
		}
		
	}
	
	public void render(Graphics g) {
		//layer
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		//titulo
		g.setColor(Color.green);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString(">Pixel Survival<", Game.WIDTH, Game.HEIGHT /2);
		
		//menu options
			//novo jogo
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		if(pause == false) {
		g.drawString("Novo Jogo", Game.WIDTH /2, Game.HEIGHT *2);
		}else
			g.drawString("Continue", Game.WIDTH /2, Game.HEIGHT *2);
		
		//Carregar jogo
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Carregar", (Game.WIDTH /2) + 30, (Game.HEIGHT *2) + 30);
		
		//sair do jogo
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Sair", (Game.WIDTH /2) + 30*2, (Game.HEIGHT *2) + 30*2);
		
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
