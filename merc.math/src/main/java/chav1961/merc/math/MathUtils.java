package chav1961.merc.math;

import chav1961.merc.api.Point;
import chav1961.merc.api.Track;
import chav1961.merc.api.interfaces.front.World;

public class MathUtils {
	private static final int[]	DISPL_LEA = {-1,-1,0,-1,1,-1,-1,0,1,0,-1,1,0,1,1,1};
	private static final int[]	DISPL_NOIMANN = {-1,0,1,0,0,-1,0,1};
	
	public static Track buildLea(final World world, final Point from, final Point to) {
		return null;
	}
	
	public static Point[] buildLea(final Track world, final Point from, final Point to) {
		return null;
	}
	
	public static float[] buildPipeMatrix(final World world) {
		return null;
	}
	
	static int[] buildLea(final short[][] cells, final int width, final int height, final int fromX, final int fromY, final int toX, final int toY, final int[][] displacements) {
		if (cells[fromY][fromX] == -1) {
			throw new IllegalArgumentException("Start point is busy"); 
		}
		else if (cells[toY][toX] == -1) {
			throw new IllegalArgumentException("End point is busy"); 
		}
		else if (fromX == toX && fromY == toY) {
			return new int[0];
		}
		else {
			short 	depth;
			boolean	stop;

			cells[fromY][fromX] = depth = 1;
			do {stop = true;
			    for (int y = 0; y < height; y++) {
				    for (int x = 0; x < width; ++x ) {
				    	if (cells[y][x] == depth) {
					        for (int around = 0, maxIndex = displacements.length/2; around < maxIndex; around++) {
					             final int 	iy = y + displacements[around][0], ix = x + displacements[around][1];
					             
					             if (iy >= 0 && iy < height && ix >= 0 && ix < width && cells[iy][ix] <= 0) {
						             cells[iy][ix] = (short) (depth + 1);
						             stop = false;
					             }
					        }
				    	}
				    }
			    }
			    depth++;
			} while (!stop && cells[toY][toX] <= 0);

			if (cells[toY][toX] <= 0) {
				return null;
			}
			else {
				final int	length = cells[toY][toX];
				final int[]	result = new int[2*length];
				int			y = toY, x = toX;
			  
				for (int index = 0, current = length, maxIndex = result.length; index < maxIndex; index += 2, current--) {
					result[index] = x;
					result[index+1] = y;
					
			        for (int around = 0, maxAround= displacements.length/2; around < maxAround; around++) {
			        	final int 	iy = y + displacements[around][0], ix = x + displacements[around][1];
		             
			        	if (iy >= 0 && iy < height && ix >= 0 && ix < width && cells[iy][ix] == current) {
			        		x = ix;
			        		y = iy;
			        		break;
			        	}
			      }
			  }
			  return result;
			}
		}
	}
	
}
