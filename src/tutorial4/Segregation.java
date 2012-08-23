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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

class Place
{
	private static int counter = 0;
	public String id;
	public int population;
	public int i;
	public int j;

	Place(int population, int i, int j)
	{
		this.population = population;
		this.i = i;
		this.j = j;
		id = population + "-" + counter;
		counter++;
	}
}

public class Segregation
{
    java.util.Random random = new Random();
	static Place FREE = new Place(0, -1, -1);
	static int POP1 = 1;
	static int POP2 = 2;

	int dimension = 30;

	Place[][] places;

	float population1Density = 0.5f;

	float population2Density = 0.495f;

	int threshold = 5;

	long sleep = 200;
	
	private float epsilon = 0.1f;

	Window window;
	
	private ArrayList<Place> population1;
	private ArrayList<Place> population2;

	Graph graph;

	public static void main(String[] args)
	{
		new Segregation();
	}

	public Segregation()
	{
		places = new Place[dimension][dimension];
		population1 = new ArrayList<Place>();
		population2 = new ArrayList<Place>();

		initGraph();
		initGUI();
		initPlaces();
		sendState();
		
		go();

	}

	private void initGraph()
	{
		graph = new DefaultGraph("Segregation Model Graph");
		
		// Add code here
		
	}

	private void go()
	{
		while (true)
		{

			Iterator<Place> pop1_iterator = population1.iterator();
			Iterator<Place> pop2_iterator = population2.iterator();
			int size = population1.size() + population2.size();
			for (int k = 0; k < size; k++)
			{
				if (pop1_iterator.hasNext())
				{
					checkNeighbors(pop1_iterator.next());
				}
				if (pop2_iterator.hasNext())
				{
					checkNeighbors(pop2_iterator.next());
				}

			}
			sleep();

		}
	}

	private void pause()
	{
		try
		{
			System.in.read();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private void checkNeighbors(Place place)
	{
		Node n = graph.getNode(places[place.i][place.j].id);
		// Add code here
		if (n.getDegree() < threshold)
		{
			move(n, place);
			// Add code here
		} else
		{
			// Add code here
		}
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(sleep);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void move(Node n, Place place)
	{
		int x = place.i;
		int y = place.j;
		int i = (int) ( random.nextDouble() * dimension);
		int j = (int) (random.nextDouble() * dimension);
		while (places[i][j] != FREE)
		{
			i = (int) (random.nextDouble() * dimension);
			j = (int) (random.nextDouble() * dimension);
		}

		places[i][j] = places[x][y];
		places[i][j].i = i;
		places[i][j].j = j;

		// Add code here

		places[x][y] = FREE;

		Object[] o = { i, j, places[i][j].population };
		Object[] o2 = { x, y, FREE.population };
		window.mbox.post("Segregation", o);
		window.mbox.post("Segregation", o2);

		// remove edges : bye bye old neighbors
		ArrayList<String> linksToRemove = new ArrayList<String>();
		for (Edge e : n.getEdgeSet())
		{
			linksToRemove.add(e.getId());
		}
		for (String id : linksToRemove)
		{
			graph.removeEdge(id);
		}

		// add new neighbors
		if (i > 0)
		{
			if (j > 0)
			{
				if (places[i - 1][j - 1].population == places[i][j].population)
				{
					graph.addEdge((i - 1) + "-" + (j - 1) + "_" + i + "-" + j, places[i][j].id,
							places[i - 1][j - 1].id);

				}
			}
			if (places[i - 1][j].population == places[i][j].population)
			{

				graph.addEdge((i - 1) + "-" + (j) + "_" + i + "-" + j, places[i][j].id,
						places[i - 1][j].id);

			}
			if (j < dimension - 1)
			{
				if (places[i - 1][j + 1].population == places[i][j].population)
				{
					graph.addEdge((i - 1) + "-" + (j + 1) + "_" + i + "-" + j, places[i][j].id,
							places[i - 1][j + 1].id);
				}
			}
		}
		if (j < dimension - 1)
		{
			if (places[i][j + 1].population == places[i][j].population)
			{
				graph.addEdge((i) + "-" + (j) + "_" + i + "-" + (j + 1), places[i][j].id,
						places[i][j + 1].id);

			}
			if (i < dimension - 1)
			{
				if (places[i + 1][j + 1].population == places[i][j].population)
				{
					graph.addEdge((i) + "-" + (j) + "_" + (i + 1) + "-" + (j + 1), places[i][j].id,
							places[i + 1][j + 1].id);

				}
			}
		}
		if (i < dimension - 1)
		{
			if (places[i + 1][j].population == places[i][j].population)
			{
				graph.addEdge((i) + "-" + (j) + "_" + (i + 1) + "-" + j, places[i][j].id,
						places[i + 1][j].id);
			}
			if (j > 0)
			{
				if (places[i + 1][j - 1].population == places[i][j].population)
				{
					graph.addEdge((i) + "-" + (j) + "_" + (i + 1) + "-" + (j - 1), places[i][j].id,
							places[i + 1][j - 1].id);
				}
			}
		}
		if (j > 0)
		{
			if (places[i][j - 1].population == places[i][j].population)
			{
				graph.addEdge((i) + "-" + (j - 1) + "_" + i + "-" + j, places[i][j].id,
						places[i][j - 1].id);
			}
		}

	}


	public Place[][] getPlaces()
	{
		return places;
	}

	public float getPopulation1Density()
	{
		return population1Density;
	}

	public float getPopulation2Density()
	{
		return population2Density;
	}

	public int getThreshold()
	{
		return threshold;
	}

	private void initGUI()
	{
		window = new Window(this);

	}

	private void initPlaces()

	{
		for (Place[] line : places)
		{
			Arrays.fill(line, FREE);
		}
		for (int n = 0; n < (int) (population1Density * dimension * dimension); n++)
		{
			int i = (int) (random.nextDouble() * dimension);
			int j = (int) (random.nextDouble() * dimension);
			while (places[i][j] != FREE)
			{
				i = (int) (random.nextDouble() * dimension);
				j = (int) (random.nextDouble() * dimension);
			}
			places[i][j] = new Place(POP1, i, j);
			population1.add(places[i][j]);
			Node n1 = graph.addNode(places[i][j].id);
			
			// Add code here
			
		}
		for (int n = 0; n < (int) (population2Density * dimension * dimension); n++)
		{
			int i = (int) (random.nextDouble() * dimension);
			int j = (int) (random.nextDouble() * dimension);
			while (places[i][j] != FREE)
			{
				i = (int) (random.nextDouble() * dimension);
				j = (int) (random.nextDouble() * dimension);
			}
			places[i][j] = new Place(POP2, i, j);
			population2.add(places[i][j]);
			Node n2 = graph.addNode(places[i][j].id);

			// Add code here
			
		}
	}

	private void sendState()
	{
		for (int i = 0; i < dimension; i++)
		{
			for (int j = 0; j < dimension; j++)
			{
				Object[] o = { i, j, places[i][j].population };
				window.mbox.post("Segregation", o);
			}
		}
	}

	public int getDimension()
	{
		return dimension;
	}

	protected static final String styleSheet = ""
		+ "graph {"
		+ "	padding: 10px;" 
		+ "}" 
		+ "node {"
		+ "	fill-mode: dyn-plain;" 
		+ "	fill-color: gray, red;" 
		+ "}";

}
