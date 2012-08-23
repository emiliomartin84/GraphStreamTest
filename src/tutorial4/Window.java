/*
 * This file is part of GraphStream.
 * 
 * GraphStream is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GraphStream is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GraphStream.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2006 - 2010
 * 	Julien Baudry
 * 	Antoine Dutot
 * 	Yoann Pigné
 * 	Guilhelm Savin
 */

package tutorial4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.miv.mbox.MBoxListener;
import org.miv.mbox.MBoxStandalone;


public class Window implements ActionListener, MBoxListener
{
	int dimension;
	MBoxStandalone mbox = new MBoxStandalone(this);
	JPanel panel;
	int[][] places;
	/**
	 * Draw a frame once a while.
	 */
	Timer timer;

	JFrame window;

	Window(Segregation segregation)
	{
		dimension = segregation.getDimension();
		places = new int[dimension][dimension];

		panel = new PlacePanel(this);
		window = new JFrame("Segregation Model");
		window.setPreferredSize(new Dimension(600, 600));
		window.getContentPane().add(panel);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timer = new Timer(40, this);
		timer.setCoalesce(true);
		timer.setDelay(40);
		timer.setRepeats(true);
		timer.start();

	}

	public void actionPerformed(ActionEvent e)
	{
		if (!mbox.isMBoxEmpty())
		{
			mbox.processMessages();
			window.repaint();
		}

	}

	public void close()
	{
		timer.stop();

		if (window != null)
			window.setVisible(false);
	}



	public void processMessage(String from, Object[] data)
	{
		places[(Integer) data[0]][(Integer) data[1]] = (Integer) data[2];
	}
}

class PlacePanel extends JPanel
{

	Window window;

	public Color colorFree;
	public Color colorPOP1;
	public Color colorPOP2;

	public PlacePanel(Window window)
	{
		this.window = window;
		colorFree = Color.white;
		colorPOP1 = new Color(0x328ba8);
		colorPOP2 = new Color(0xedbcbc);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		int dim = getWidth() > getHeight() ? getHeight() : getWidth();
		int rectWidth = dim / window.dimension;
		for (int i = 0; i < window.dimension; i++)
		{
			for (int j = 0; j < window.dimension; j++)
			{
				switch (window.places[i][j])
				{
				case 0:
					g2.setColor(colorFree);
					break;
				case 1:
					g2.setColor(colorPOP1);
					break;
				case 2:
					g2.setColor(colorPOP2);
					break;
				}
				g2.fillRect(rectWidth * i + getWidth() / 2 - dim / 2, rectWidth * j + getHeight()
						/ 2 - dim / 2, rectWidth, rectWidth);
			}
		}
	}
}

