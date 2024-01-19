package jGolSA1;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

import java.util.ArrayList;
import java.util.List;

public class Cell {
	
	private Grid<Object> grid;
	private boolean futureState;
	private List<int[]> toUpdateCellsCoordinates;
	
	public Cell (Grid<Object> grid) {
		this.grid = grid;
		futureState = true;
		toUpdateCellsCoordinates = new ArrayList<int[]>();
	}
	
	@ScheduledMethod(start = 1,interval = 1, priority=2)
	public void step()
	{		
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Cell> nghCellNgh = new GridCellNgh<>(grid, pt, Cell.class, 1, 1);
		List<GridCell<Cell>> gridCells = nghCellNgh.getNeighborhood(true);
		
		int vivi = -1; //La cella stessa è considerata nel neighborhood...
		
		for (GridCell<Cell> e : gridCells) {
			if(e.size() != 0) {
				vivi++;
			} else {
				//Se la cella è vuota
				GridPoint emptyCell = e.getPoint();
				GridCellNgh<Cell> emptyCellNgh = new GridCellNgh<>(grid, emptyCell, Cell.class, 1, 1);
				List<GridCell<Cell>> emptyGridCells = emptyCellNgh.getNeighborhood(true);
				int countAlive = 0;
				for (GridCell<Cell> c : emptyGridCells) {
					if(c.size() != 0)
						countAlive++;
				}
				if(countAlive == 3) {
					int[] coordinates = {emptyCell.getX(), emptyCell.getY()};
					toUpdateCellsCoordinates.add(coordinates);
				}		
			}
		}
		
		// se deve morire, altrimenti rimane viva
		if(vivi < 2 || vivi > 3) {
			futureState = false;
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void render() {
	    while (!toUpdateCellsCoordinates.isEmpty()) {
	        int[] coordinates = toUpdateCellsCoordinates.remove(0);

	        // Check if the square is empty
	        if (grid.getObjectAt(coordinates[0], coordinates[1]) == null) {
	            Cell agent = new Cell(grid);
	            ContextUtils.getContext(this).add(agent);
	            grid.moveTo(agent, coordinates[0], coordinates[1]);
	        }
	    }

	    if (futureState == false) {
	        ContextUtils.getContext(this).remove(this);
	    }
	}
}
