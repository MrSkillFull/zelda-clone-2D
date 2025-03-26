package com.flstudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.flstudios.entities.Player;
import com.flstudios.main.Game;
import com.flstudios.world.Camera;

public class UI {

	public void render(Graphics g) {
		int x = 8;
		int y = 10;
		int w = 50;
		int h = 8;
		g.setColor(Color.RED);
		g.fillRect(x, y,w, h);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, (int)((Game.player.life / Game.player.maxLife)*50), h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 10, 17);
		
	}
	
}
