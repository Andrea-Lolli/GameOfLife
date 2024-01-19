package jGolSA1;

import java.util.Random;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class GolBuilder implements ContextBuilder<Object> {

	private int dimX = 180;
	private int dimY = 180;
	
	@Override
	public Context build(Context<Object> context) {
		context.setId("jGolSA1");

		// Creazione griglia
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);		
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new WrapAroundBorders(), new SimpleGridAdder<Object>(), false, dimX, dimY));
		
		Random random = new Random();
		
		for (int x = 0; x < dimX; x++) {
		    for (int y = 0; y < dimY; y++) {	
		        
		    	int seed = random.nextInt(2);
		    	if(seed == 1) {
		    		//Ogni cella ha un 50% di possibilità di generare un agente
		    		Cell agent = new Cell(grid);
		    		context.add(agent);
			        grid.moveTo(agent,x,y);
		    	}  
		    }
		}
			
		return context;
	}
}
