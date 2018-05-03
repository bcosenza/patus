package stenciltune;

import java.util.ArrayList;
import java.util.Collections;

import stenciltune.StencilPoint;

/**
 * A StencilPattern is an array of 2d/3d points. 
 */
public class StencilPattern extends ArrayList<StencilPoint> {

	private static final long serialVersionUID = 1L;
	

	public int getMaxRadius(){		
		StencilPoint first = this.get(0);
		if(first == null) return 0;
		int max = first.getMaxRadius();  
		
		for(StencilPoint p : this){
			max = Math.max(max, p.getMaxRadius());			
		}
		return max;
	}
	
	void addPoint(StencilPoint p){
		this.add(p);
	}
	
	/** Returns a human-readable string representing the stencil pattern, i.e.  <id>:<integervalue> */
	public String toString(){
		String s = "";
		for(StencilPoint sp : this){
			s += " " + sp;
		}
		return s;
	}
	
	
	/** Returns a string representing the encoded stencil pattern, i.e.  <id>:<floatvalue> */
	public String toEncodedString(){ 
		String s = "";
		for(StencilPoint sp : this){
			s += sp.toEncodedString() + " ";
		}
		return s; 
	}
	
	
	public void sort(){		
		Collections.sort(this);		
	}
	
	public void draw2D(int squareSize){			
		for(int y = -squareSize; y<=squareSize; y++){
			for(int x = -squareSize; x<=squareSize; x++){
				boolean found = false;
				points:
				for(StencilPoint p : this){
					if(p.getX() == x && p.getY() == y){
						System.out.print("+");
						found = true;
						break points;
					}											
				}
				if(!found)
					System.out.print(" ");
			}
			System.out.println();
		}		
	}
	
	public void draw3D(int squareSize){
		for(int z = -squareSize; z<=squareSize; z++){
			System.out.println("z="+z);
			for(int y = -squareSize; y<=squareSize; y++){				
				for(int x = -squareSize; x<=squareSize; x++){					
					boolean found = false;
					points:
					for(StencilPoint p : this){
						if(p.getX() == x && p.getY() == y && p.getZ() == z){
							System.out.print("+");
							found = true;
							break points;
						}											
					}
					if(!found)
						System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println();
		}			
		
	}
	
	/** From stencil pattern to feature vector */
	public FeatureVector toFeatureVector(){
		FeatureVector fv = new FeatureVector(2231);
		for(StencilPoint p : this){
			int index = p.linearize();
			fv.put(index, 1.0);
		}
		return fv;
	}
	
	/** Checks whether the stencil pattern is a 3d hyper-cube. Warning: very naive test! */
	public boolean isCube(){		
		return getCube()>0;
	} 
	
	public int getCube(){
		int size = 0;
		for(int i=1; i<StencilPoint.MAX_STENCIL_SIZE; i++){			
			Point3D q = new Point3D( i, 0, 0);
			if(!contains(q)) 
				break;
			else
				size++;
		}		
		//System.out.println("may be " + size);
		
		//outer:
		for(int i= -size; i<=size; i++)
			for(int j= -size; j<=size; j++)
				for(int k= -size; k<=size; k++){
					Point3D q = new Point3D( i, j, k);					
					if(!contains(q)){
						//System.out.println("> "+i+","+j+","+k);
						return 0;
					} 
				}		
		return size;		
	}
	
	/** Testing some stencil patterns */
	public static final void main(String args[]) {
		StencilGenerator sg = new StencilGenerator();		
		
		System.out.println("Laplacian");
		StencilPattern lap2d = sg.getLaPlacian2D(1);  
		lap2d .draw2D(1);
		System.out.println("- " + lap2d.toEncodedString());
		System.out.println("- " + lap2d.toString());		
		
		System.out.println(" *** 2D patterns ***");
		System.out.println("Laplacian");
		sg.getLaPlacian2D(5).draw2D(5);
		System.out.println("Hypercube");
		sg.getHypercube2D(5).draw2D(5);
		System.out.println("Line 1");	
		sg.getLine2D(5,0).draw2D(5);
		System.out.println("Line 2");
		sg.getLine2D(5,1).draw2D(5);
		System.out.println();		
		
		System.out.println(" *** 3D patterns ***");		
		System.out.println("Laplacian");
		sg.getLaPlacian3D(3).draw3D(3);		
		System.out.println("Hypercube");
		sg.getHypercube3D(3).draw3D(3);				
		System.out.println("Line 1");		
		sg.getLine3D(3,0).draw3D(3);		
		System.out.println("Line 2");	
		sg.getLine3D(3,1).draw3D(3);
		System.out.println("Line 3");	
		sg.getLine3D(3,2).draw3D(3);
		System.out.println("Hyperplane 1");
		sg.getHyperplane3D(3, 0).draw3D(3);
		System.out.println("Hyperplane 2");
		sg.getHyperplane3D(3, 1).draw3D(3);
		System.out.println("Hyperplane 3");
		sg.getHyperplane3D(3, 2).draw3D(3);		
		
		System.out.println("*** hypercube check ***");
		System.out.println(sg.getLaPlacian2D(5).isCube());
		System.out.println(sg.getHypercube2D(5).isCube());
		System.out.println(sg.getLine2D(5,0).isCube());
		System.out.println(sg.getLine2D(5,1).isCube());
		System.out.println("-");
		System.out.println(sg.getLaPlacian3D(3).isCube());
		System.out.println(sg.getHypercube3D(3).isCube()); // this is true!
		System.out.println(sg.getLine3D(3,0).isCube());
		System.out.println(sg.getLine3D(3,1).isCube());
		System.out.println(sg.getLine3D(3,2).isCube());
		System.out.println(sg.getHyperplane3D(3,0).isCube());
		System.out.println(sg.getHyperplane3D(3,1).isCube());
		System.out.println(sg.getHyperplane3D(3,2).isCube());
		
	}
}

