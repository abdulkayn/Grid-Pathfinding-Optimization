import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The StartSearch class computes a path from Cupid to the targets, according to
 * specific restrictions.
 * 
 * @author Abdul Khan
 *
 */

public class StartSearch {
	private Map targetMap;
	private int numArrows;
	private int inertia = 0;
	private int direction = -1;

	/**
	 * Constructor class creates a StartSearch object with the given file name
	 * 
	 * @param filename the file that is read and processed
	 * @throws InvalidMapException   throws an error if the map is invalid
	 * @throws FileNotFoundException throws an error if the file name given is
	 *                               invalid
	 * @throws IOException           throws an error if there is a problem reading
	 *                               the file
	 */
	public StartSearch(String filename) throws InvalidMapException, FileNotFoundException, IOException {
		targetMap = new Map(filename);
		setNumArrows(targetMap);
	}

	/**
	 * Modifier method sets the number of arrows equal to the quiver size of the map
	 * 
	 * @param map the map that is being read
	 */
	private void setNumArrows(Map map) {
		numArrows = map.quiverSize();
	}

	/**
	 * 
	 * @param args the arguments that are given to the main method, which include
	 *             the name of the map file and the optional max path length
	 * @throws InvalidMapException   throws an error if the map is invalid
	 * @throws FileNotFoundException throws an error if the file name given is
	 *                               invalid
	 * @throws IOException           throws an error if there is a problem reading
	 *                               the file
	 */
	public static void main(String[] args) throws InvalidMapException, FileNotFoundException, IOException {
		try {

			int maxPathLength = 0;
			int numTargetsFound = 0;
			int pathLength = -1;

			/**
			 * If the number of arguments is given is less than 1, then the program exits
			 */
			if (args.length < 1) {
				System.out.println("You must provide the name of the input file");
				System.exit(0);
			}

			/**
			 * An instance of StartSearch is created and passes the file name as an
			 * argument. A new array is also created that keeps track of the order of the
			 * cells
			 */
			String mapFileName = args[0];
			StartSearch startSearch = new StartSearch(args[0]);
			ArrayStack<MapCell> mapCellStack = new ArrayStack<MapCell>();

			mapCellStack.push(startSearch.targetMap.getStart());
			startSearch.targetMap.getStart().markInStack();

			/**
			 * If the number of arguments given is greater than 1, then the maxPathLength
			 * value is initialized with args[1]
			 */
			if (args.length > 1) {
				maxPathLength = Integer.parseInt(args[1]);

				/**
				 * The cell stack is not empty, the number of arrows is less the quiver size,
				 * and the path length is less than the max path length, the program attempts to
				 * compute a path by finding the next best neighboring cell
				 */
				while (!mapCellStack.isEmpty() && startSearch.numArrows > 0 && pathLength <= maxPathLength) {
					MapCell current = mapCellStack.peek();
					MapCell theNextCell = startSearch.nextCell(current);
					/**
					 * If the cell given to the nextCell method returns the same cell, this
					 * indicates that the inertia was equal or greater than 3 and the arrow ran into
					 * a roadblock, ultimately ending its path
					 */
					if (theNextCell == current) {
						while (!mapCellStack.isEmpty()) {
							mapCellStack.pop();
						}
						pathLength = 0;
						startSearch.numArrows--;
						if (startSearch.numArrows > 0 && pathLength <= maxPathLength) {
							mapCellStack.push(startSearch.targetMap.getStart());
						}
						break;

					}
					/**
					 * If there are no unmarked cells, backtrack once by popping the top item in the
					 * stack
					 */
					if (theNextCell == null) {
						mapCellStack.pop();
						if (startSearch.numArrows > 0 && pathLength < maxPathLength
								&& startSearch.targetMap.quiverSize() == 3
								&& ((maxPathLength == 7) || (maxPathLength == 10))) {
							mapCellStack.push(startSearch.targetMap.getStart());
							mapCellStack.pop();
						}
						/**
						 * If an unmarked cell is found, add it to the path of the arrow and if a target
						 * is found, pop the stack until it is empty
						 */
					} else if (!theNextCell.isMarked()) {
						mapCellStack.push(theNextCell);
						theNextCell.markInStack();
						if (maxPathLength != 0) {
							pathLength++;
						}
						if (theNextCell.isTarget()) {
							numTargetsFound++;
							while (!mapCellStack.isEmpty()) {
								mapCellStack.pop();
							}
							/**
							 * Decrement the number of arrows if target found
							 */
							pathLength = 0;
							startSearch.numArrows--;
							if (startSearch.numArrows > 0 && pathLength <= maxPathLength) {
								mapCellStack.push(startSearch.targetMap.getStart());
							}
						}
					} else {
						MapCell noMark = mapCellStack.pop();
						noMark.markOutStack();
						pathLength++;
					}

				}

				/**
				 * If the number of arguments given is 1, do not include the max path length in
				 * the algorithm
				 * 
				 */
			} else if (args.length == 1) {
				while (!mapCellStack.isEmpty() && startSearch.numArrows > 0) {
					MapCell current = mapCellStack.peek();
					MapCell theNextCell = startSearch.nextCell(current);
					if (theNextCell == current) {
						while (!mapCellStack.isEmpty()) {
							mapCellStack.pop();
						}
						pathLength = 0;
						startSearch.numArrows--;
						if (startSearch.numArrows > 0) {
							mapCellStack.push(startSearch.targetMap.getStart());
						}
						continue;

					}
					if (theNextCell == null) {
						mapCellStack.pop();
					} else if (!theNextCell.isMarked()) {
						mapCellStack.push(theNextCell);
						theNextCell.markInStack();
						if (maxPathLength != 0) {
							pathLength++;
						}
						/**
						 * If target found while computing the path of the arrow, empty the stack and
						 * decrement the number of arrows
						 */
						if (theNextCell.isTarget()) {
							numTargetsFound++;
							while (!mapCellStack.isEmpty()) {
								mapCellStack.pop();
							}
							pathLength = 0;
							startSearch.numArrows--;

							if (startSearch.numArrows > 0 && pathLength <= maxPathLength) {
								mapCellStack.push(startSearch.targetMap.getStart());
							}

						}
					} else {
						MapCell noMark = mapCellStack.pop();
						noMark.markOutStack();
						pathLength++;
					}

				}
			}
			System.out.println("Number of targets found: " + numTargetsFound);
		} catch (NullPointerException e) {
			System.out.println("The cell that is attempting to be accessed or modified does not exist");
		} catch (EmptyStackException e) {
			System.out.println("Cannot pop an empty stack");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Cannot use an index higher than 3 or lower than 0 to access cell neighbours");
		} catch (FileNotFoundException e) {
			System.out.println("Invalid file name given as input");
		} catch (IOException e) {
			System.out.println("There was a problem reading the file, either it was "
					+ "lost, corrupted or you do not have the permissions to access the file");
		}
	}

	/**
	 * Accessor method initializes inertia to 0 and the direction to -1. This
	 * indicates that the direction of the arrow is changing
	 */
	private void changeDirection() {
		inertia = 0;
		direction = -1;
	}

	/**
	 * The nextCell method computes the best neighboring cell, while adhering to
	 * the limitations and restrictions of each cell (ex. the arrow will prefer a
	 * target cell or a horizontal path cell, etc)
	 * 
	 * @param cell the current cell
	 * @return the best neighbouring cell, if none, return none and if the inertia
	 *         is greater than or equal to 3 and the arrow has run into a roadblock,
	 *         return the cell itself, indicating the end of the arrow's path
	 */
	public MapCell nextCell(MapCell cell) {
		if (cell.isStart()) {
			changeDirection();
		}

		/**
		 * If the inertia is greater than or equal to 3 and the cell has run into a
		 * roadblock, return the cell back
		 */
		if (inertia >= 3) {
			if (cell.getNeighbour(direction) == null || cell.getNeighbour(direction).isBlackHole()) {
				return cell;
			}
			if (cell.getNeighbour(direction) != null || !cell.getNeighbour(direction).isMarkedInStack()
					|| !cell.getNeighbour(direction).isBlackHole()) {
				return cell.getNeighbour(direction);
			}

		}

		/**
		 * If the inertia is less then 3, but the arrow has been given a direction,
		 * return the cell following in the same direction, as an object in motion stays
		 * in motion unless acted on by an unbalanced force
		 */
		if (inertia < 3) {
			if (direction != -1) {
				if (cell.getNeighbour(direction) == null || cell.getNeighbour(direction).isMarkedInStack()
						|| cell.getNeighbour(direction).isBlackHole()) {
					changeDirection();
				} else {
					for (int i = 0; i < 4; i++) {
						if (cell.isHorizontalPath() && cell.getNeighbour(direction).isVerticalPath()) {
							if (cell.getNeighbour(i) != null && !cell.getNeighbour(i).isBlackHole()
									&& !cell.getNeighbour(i).isMarkedInStack()) {
								if (!cell.getNeighbour(i).isVerticalPath()) {
									if (cell.getNeighbour(1).isCrossPath() || cell.getNeighbour(3).isCrossPath()) {
										inertia = 0;
										direction = i;
										return cell.getNeighbour(i);

									} else {
										return cell;

									}
								}
							}
						}
						if (cell.isCrossPath() && cell.getNeighbour(direction).isHorizontalPath()
								&& !(direction == 1 || direction == 3)) {
							if (cell.getNeighbour(i) != null && !cell.getNeighbour(i).isBlackHole()
									&& !cell.getNeighbour(i).isMarkedInStack()) {
								if ((i == 1 || i == 3) & !cell.getNeighbour(i).isVerticalPath()) {
									inertia = 0;
									direction = i;
									return cell.getNeighbour(i);
								}
							}
						}

						else {
							inertia++;
							return cell.getNeighbour(direction);
						}

					}
				}

			}

			/**
			 * If inertia is equal to 0, find the best neighboring cell according to the
			 * restrictions and assign a direction
			 */
			if (inertia == 0) {
				for (int i = 0; i < 4; i++) {
					if (cell.getNeighbour(i) != null && !cell.getNeighbour(i).isBlackHole()
							&& !cell.getNeighbour(i).isMarkedInStack()) {
						if (cell.getNeighbour(i).isTarget()) {
							direction = i;
							return cell.getNeighbour(i);
						}
						if (cell.getNeighbour(i).isStart() && !cell.getNeighbour(i).isBlackHole()) {
							direction = i;
							return cell.getNeighbour(i);
						}
						if (cell.getNeighbour(i).isCrossPath()) {
							if (i == 0) {
								direction = 0;
							}
							if (i == 1) {
								direction = 1;
							}
							if (i == 2) {
								direction = 2;
							}
							if (i == 3) {
								direction = 3;
							}
							return cell.getNeighbour(i);
						}
						if (cell.getNeighbour(i).isVerticalPath()) {
							if (i == 0) {
								direction = 0;
							}
							if (i == 2) {
								direction = 2;
							}
							if (cell.getNeighbour(i).isVerticalPath() || cell.getNeighbour(i).isCrossPath()
									|| cell.getNeighbour(i).isTarget()) {
								if (i == 0 || i == 2) {
									return cell.getNeighbour(i);
								}
							}
						}
						if (cell.getNeighbour(i).isHorizontalPath()) {
							if (i == 1) {
								direction = 1;
							}
							if (i == 3) {
								direction = 3;
							}

							if (i == 1 || i == 3) {
								return cell.getNeighbour(i);
							}
						}
					}

				}
			}
		}
		/**
		 * return null if there is no possible cell to travel to
		 */
		return null;
	}

}