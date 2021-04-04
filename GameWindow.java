package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameWindow extends JFrame {
	private static GameWindow game_window;
	private static long last_frame_time;
	private static Image covid;
	private static Image vieww;
	private static Image game_over;
	private static float drop_left = 200; // хранит координату Х верхнего левого угла вируса
	private static float drop_top = -100; // хранит координату Y верхнего левого угла вируса
	private static  float drop_v = 200;
	private static  int score;

    public static void main(String[] args) throws IOException {
    	vieww = ImageIO.read(GameWindow.class.getResourceAsStream("vieww.png"));
		covid = ImageIO.read(GameWindow.class.getResourceAsStream("covid.png"));
		game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
	game_window = new GameWindow();
	game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	game_window.setSize(890, 500);
	game_window.setResizable(false);
	game_window.setLocation(200, 100);
	last_frame_time = System.nanoTime();
	GameField game_field = new GameField();
	game_field.addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			float drop_right = drop_left + covid.getWidth(null);
			float drop_bottom = drop_top + covid.getHeight(null);
			boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
			if(is_drop) {
				drop_top = -100;
				drop_left = (int)(Math.random()* (game_field.getWidth() - covid.getWidth(null)));
				drop_v = drop_v + 15;
				score++;
				game_window.setTitle("score: " + score);
			}
		}
	});

	game_window.add(game_field);
	game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
    	long current_time = System.nanoTime();
    	float delta_time = (current_time - last_frame_time) * 0.000000001f;
    	last_frame_time = current_time;

    	drop_top = drop_top + drop_v * delta_time;
    	g.drawImage(vieww, 0, 0 ,null );
    	g.drawImage(covid, (int)drop_left, (int)drop_top ,null );
    	if(drop_top > game_window.getHeight()) {
			g.drawImage(game_over, 290, 90, null);
			String restart = "Restart (press R)";
			Font f = new Font("Arial", Font.BOLD, 18);
			g.setColor(Color.black);
			g.setFont(f);
			g.drawString(restart, 370, 290);

		}
	}


	private static class GameField extends JPanel {

    	@Override
		protected void paintComponent (Graphics g){
    		super.paintComponent(g);
    		onRepaint(g);
    		repaint();
		}
    }

}