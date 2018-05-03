package stenciltune;

/**
 *	A point of a stencil pattern. Current implementation support 2d and 3d coordinates.
 *	Future work: support the time parameter (t).
 */
abstract public class StencilPoint implements Comparable<StencilPoint> {

	public int getX(){ return 0; }
	public int getY(){ return 0; }
	public int getZ(){ return 0; }
	public int getT(){ return 0; }

	abstract public int getMaxRadius();
	abstract public int linearize();
	
	abstract public String toEncodedString(int maxSize); //{ return toString(); }
	public String toEncodedString(){ return toEncodedString(MAX_STENCIL_SIZE); }

	// We assume there cannot be any stencil pattern bigger than this value
	public final static int MAX_STENCIL_SIZE = 10;
	
	@Override
	public int compareTo(StencilPoint p2){
		return linearize() - p2.linearize();		
	}
	
	/** Testing function */
	public static void main(String args[]) {		
		
		Point3D a = new Point3D(0,0,0);
		Point3D b = new Point3D(3,3, 3);
		Point3D c = new Point3D(3,3,-3);
		Point3D e = new Point3D(1, 0, 0);
		Point3D f = new Point3D(-1,0,0);
		Point3D g = new Point3D(0,0,0);
				
		System.out.println(a.compareTo(b));
		System.out.println(a.compareTo(c));
		System.out.println(b.compareTo(c));
		System.out.print("is e < f? ");
		System.out.println(e.compareTo(f));
		System.out.print("is e > f? ");
		System.out.println(f.compareTo(e));
		System.out.println(a.compareTo(g));

	}

}
