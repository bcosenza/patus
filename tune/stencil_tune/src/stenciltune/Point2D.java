package stenciltune;

/**
 *	A two-dimensional point.
 *	It can be represented (and printed) as (x,y) coordinate or in a normalized form that is used for ML encoding.
 *	The normalized form is a sparse-vector where there are 1s for each non-zero point.
 *	To allow comparison against different patterns, indexes are linearized using a big-enough shape size.    
 */
public class Point2D extends StencilPoint {
	
	private int x, y;  
	
	public Point2D(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){ return x; }	
	public int getY(){ return y; }
	
	public int getMaxRadius(){
		int max = Math.abs(x);
		max = Math.max(max, Math.abs(y));	
		return max;
	}
	
	public String toString(){
		return x+":"+y;		
	}	
	
	public int linearize(){
		return linearize(MAX_STENCIL_SIZE);
	}
	
	public int linearize(int maxSize){
		// first, from (x,y) to a non-negative (x+maxSize,y+maxSize) 
		int nx = x+maxSize;
		int ny = y+maxSize;
		int nz = 0+maxSize; // we add this value in order to have the center of the 2D case matching the center of the 3d case
		// than linearize index		
		int index = nx + ny*maxSize + nz*maxSize*maxSize;
		return index;
	}
	
	public String toEncodedString(int maxSize){
		int index = linearize(maxSize);
		return index +":1";		
	}
		
}
