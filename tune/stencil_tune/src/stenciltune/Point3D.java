package stenciltune;

/**
 *	A three-dimensional stencil point. 
 *	It can be represented (and printed) as (x,y,z) coordinate or in a normalized form that used for ML encoding.
 *	The normalized form is a sparse-vector where there are 1s for each non-zero point.
 *	To allow comparison against different patterns, indexes are linearized using a big-enough shape size.    
 */
public class Point3D extends StencilPoint {
	
	private int x, y, z;

	
	public Point3D(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;			
	}
	
	public int getX(){ return x; }	
	public int getY(){ return y; }
	public int getZ(){ return z; }	

	public int getMaxRadius(){
		int max = Math.abs(x);
		max = Math.max(max, Math.abs(y));
		max = Math.max(max, Math.abs(z));
		return max;
	}
	
	public int linearize(){
		return linearize(MAX_STENCIL_SIZE);
	}
	
	public int linearize(int maxSize){
		// first, from (x,y) to a non-negative (x+maxSize,y+maxSize,z+maxSize) 
		int nx = x+maxSize;
		int ny = y+maxSize;
		int nz = z+maxSize;
		// than linearize index
		int index = nx + ny*maxSize + nz*maxSize*maxSize;		
		return index;
	}	
	
	public String toString(){
		return x+":"+y+":"+z;		
	}
	
	
	public String toEncodedString(int maxSize){
		int index = linearize(maxSize);
		return index +":1";		
	}	
	
	
	/** Testing function */
	public static final void main(String args[]) {		
		
		System.out.println("3D");		
		System.out.println(new Point3D(0,0,0).toEncodedString());
		System.out.println(new Point3D(3,3,0).toEncodedString());
		System.out.println(new Point3D(3,3,3).toEncodedString());
		System.out.println(new Point3D(3,3,-3).toEncodedString());
		System.out.println(new Point3D(-3,-3,0).toEncodedString());
		System.out.println("MIN: " + new Point3D(-10,-10,-10).toEncodedString());
		System.out.println("MAX: " + new Point3D(10,10,10).toEncodedString());
		
		System.out.println();
		
		System.out.println("2D");
		System.out.println(new Point2D(0,0).toEncodedString());
		System.out.println(new Point2D(3,3).toEncodedString());
		System.out.println(new Point2D(3,3).toEncodedString());
		System.out.println(new Point2D(3,3).toEncodedString());
		System.out.println(new Point2D(-3,-3).toEncodedString());		
		System.out.println("MIN: " + new Point2D(-10,-10).toEncodedString());
		System.out.println("MAX: " + new Point2D(10,10).toEncodedString());
		
		System.out.println();
		Point3D a = new Point3D(1,1,1);
		Point3D b = new Point3D(1,1,1);
		Point3D c = new Point3D(1,1,-1);
		System.out.println("is equal "+ (a.equals(b)));
		System.out.println("is equal "+ (a.equals(c)));
		System.out.println("is equal "+ (b.equals(c)));
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(obj instanceof Point3D) {
			Point3D p = (Point3D) obj;
			return x == p.x && y == p.y && z == p.z;
	    }
		return false;    
	}
	
	@Override
	public int hashCode(){
		return linearize(MAX_STENCIL_SIZE);
	}	
	
}
