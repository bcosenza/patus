package stenciltune;

import java.util.*;


/**
 * Parameters used at runtime (K_d): 
 * blocking sizes, chunking, and unroll factor. 
 */
public class ExecutionParams  {

	// the input size is an execution parameter but is not optimization dependent, is not randomized, 
	// as is used in the ranking algorithm as 'input'
	Parameter sizex;    	// 2223
	Parameter sizey;    	// 2224
	Parameter sizez;    	// 2225

	// the following parameters are true optimization-dependent parameter and can be randomized
	Parameter cbx;    // 2226
	Parameter cby;    // 2227
	Parameter cbz;    // 2228
	Parameter chunk;  // 2229
	Parameter unroll; // 2230
		
	/** Help function: throws a runtime exception whether an encoded value has a wrong value
	 *  e.g. a NaN, or is out of the range [0,1]. 
	 */
	private static void runtimeCheck(double x){
		if(x > 1.0) throw new RuntimeException("Error in InputParam: value bigger than 1!");
		if(x < 0.0) throw new RuntimeException("Error in InputParam: value smaller than 0!");
		if(Double.isNaN(x)) throw new RuntimeException("Error in InputParam: found a NaN!");
	}
	
	/** Help function: calculate the log2() */	
	private static final float log2(int x){		
		return (float) (Math.log(x) / Math.log(2));		 
	}
	
	private void initParam(){
		// list of supported parameters
		sizex     = new Parameter(new int[]{  64,128,256,512,1024,2048,8192});
		sizey  	  = new Parameter(new int[]{  64,128,256,512,1024,2048,8192});
		sizez	  = new Parameter(new int[]{1,64,128,256,512});
		
		cbx    = new Parameter(new int[]{      8,16,32,64,128,256,512,1024});
		cby    = new Parameter(new int[]{  2,4,8,16,32,64,128,256,512,1024});
		cbz    = new Parameter(new int[]{1,2,4,8,16,32,64,128,256,512});
		chunk  = new Parameter(new int[]{1,2,4,8});
		unroll = new Parameter(new int[]{0,1,2,4,8});		
	}
	
	/** Randomized constructor of execution parameters. */
	public ExecutionParams(int _size[], Random rand){
		initParam();
		setSizes(_size);
		randomize(rand);
	}
	
	/** Constructor of predefined execution parameters. */
	public ExecutionParams(int _size[], int _cb[], int _chunk, int _unroll){
		initParam();
		
		// setting values
		setSizes(_size);

		cbx.setVal(_cb[0]);
		cby.setVal(_cb[1]);
		cbz.setVal(_cb[2]);
		chunk.setVal(_chunk);
		unroll.setVal(_unroll);
	}	
	
	
	/** 
	 * Randomize blocking sizes, chunk and unroll factor. 
	 * Randomization checks whether blocking size are smaller than the input size. 
	 */
	public void randomize(Random rand){
		do { cbx.randomize(rand); }
		while (sizex.val() < cbx.val());
		
		do { cby.randomize(rand); }
		while(sizey.val() < cby.val());
				
		// if this is a 2d stencil, no randomization of cbz, we just set up it to 1
		if(sizez.val() == 1){
			cbz.setVal(1);
		}
		else {
			do { cbz.randomize(rand); }
			while(sizez.val() < cbz.val());
		}
			
		chunk.randomize(rand);
		unroll.randomize(rand);
	}
	
	public void setSizes(int _size[]){
		sizex.setVal(_size[0]);
		sizey.setVal(_size[1]);
		
		if(_size.length == 3)
			sizez.setVal(_size[2]);
		else 
			sizez.setVal(1);
	}
	
	public void set(int _cbx, int _cby, int _cbz, int _chunk, int _unroll){
		cbx.setVal(_cbx);
		cbx.setVal(_cby);
		cbx.setVal(_cbz);
		chunk.setVal(_chunk);
		unroll.setVal(_unroll);
	}	
		
	public FeatureVector toFeatureVector(){
		FeatureVector fv = new FeatureVector(2231);
		
		// input size
		double ix = (log2(sizex.val()) - log2(sizex.min())) / (log2(sizex.max()) - log2(sizex.min()));
		runtimeCheck(ix);
		double iy = (log2(sizey.val()) - log2(sizey.min())) / (log2(sizey.max()) - log2(sizey.min()));
		runtimeCheck(iy);
		double iz = log2(sizez.val()) / log2(sizez.max());
		runtimeCheck(iz);
		fv.put(2223,ix);
		fv.put(2224,iy);
		fv.put(2225,iz);

		
		// blocking, log-scale encoding
//		double fbx = (log2(cbx.val()) - log2(8)) / (log2(256) - log2(8));
//		double fby = (log2(cby.val()) - log2(8)) / (log2(256) - log2(8));
//		double fbz = log2(cbz.val()) / log2(256);
		double fbx = (log2(cbx.val()) - log2(cbx.min())) / (log2(cbx.max()) - log2(cbx.min()));
		runtimeCheck(fbx);
		double fby = (log2(cby.val()) - log2(cby.min())) / (log2(cby.max()) - log2(cby.min()));
		runtimeCheck(fby);
		double fbz = log2(cbz.val()) / log2(cbz.max());		
		runtimeCheck(fbz);	
		fv.put(2226,fbx);
		fv.put(2227,fby);
		fv.put(2228,fbz);		
		
		// chunking, log-scale encoding
		float fchunk = log2(chunk.val()) / log2(chunk.max());
		runtimeCheck(fchunk);
		fv.put(2229,fchunk);
		
		// unroll, log-scale encoding (but also 0 is supported)
		int u = unroll.val();
		float fu = (u==0) ? 0 : ( (log2(u)+1) / (log2(8)+1) );
		runtimeCheck(fu);
		fv.put(2230,fu);		
	
		return fv;
	}
	
	
	
	public String toPatus2DInputString(){
		return  sizex + " " + sizey + " " + 
				cbx   + " " + cby	+ " " +  
				chunk + " " + unroll;		
	}
	
	public String toPatus3DInputString(){
		return  sizex + " " + sizey + " " + sizez + " " +
				cbx   + " " + cby   + " " + cbz   + " " + 
				chunk + " " + unroll;		
	}
	
	public String toEncodedString(){
		return toFeatureVector().toEncodedString();
	}
	
	/**
	 * Human-readable string, also used in the prediction after the # marker.
	 */
	public String toString(){
		return  "size(" + sizex + "," + sizey + "," + sizez + ") " +
				"cb(" + cbx + "," + cby	 + "," + cbz + ") chunk(" + chunk + ") unroll(" + unroll + ")";
	}
	
	
	/** Getter */
	public int[] getSizes(){
		return new int[]{sizex.val(),sizey.val(),sizez.val()};
	}
	public int[] getBlocking(){
		return new int[]{cbx.val(),cby.val(),cbz.val()};
	}
	public int getChunk(){ return chunk.val(); }
	public int getUnroll(){ return unroll.val(); }
		
	/** Getter for parameter's supported values  */
	public int[] getSizeXSupportedValues(){ return sizex.getSupportedValues(); }
	public int[] getSizeYSupportedValues(){ return sizey.getSupportedValues(); }
	public int[] getSizeZSupportedValues(){ return sizez.getSupportedValues(); }
	public int[] getBlockXSupportedValues(){ return cbx.getSupportedValues(); }
	public int[] getBlockYSupportedValues(){ return cby.getSupportedValues(); }
	public int[] getBlockZSupportedValues(){ return cbz.getSupportedValues(); }
	public int[] getChunkSupportedValues(){ return chunk.getSupportedValues(); }
	public int[] getUnrollSupportedValues(){ return unroll.getSupportedValues(); }
	
	
	@Override
	public boolean equals(Object aThat) {
	    //check for self-comparison
	    if ( this == aThat ) return true;

	    //use instanceof instead of getClass here for two reasons
	    //1. if need be, it can match any supertype, and not just one class;
	    //2. it renders an explict check for "that == null" redundant, since
	    //it does the check for null already - "null instanceof [type]" always
	    //returns false. (See Effective Java by Joshua Bloch.)
	    if ( !(aThat instanceof ExecutionParams) ) return false;
	    //Alternative to the above line :
	    //if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

	    //cast to native object is now safe
	    ExecutionParams that = (ExecutionParams)aThat;

	    //now a proper field-by-field evaluation can be made
	    return
	    	Arrays.equals(this.getSizes(), that.getSizes()) &&
	    	Arrays.equals(this.getBlocking(), that.getBlocking()) &&	    
	    	this.getChunk() == that.getChunk() &&
	    	this.getUnroll() == that.getUnroll();	  
	}
	
	@Override
	public int hashCode(){		
		final int prime = 37;		
		int hc = 0;
	    hc = prime * hc + sizex.val();
	    hc = prime * hc + sizey.val();
	    hc = prime * hc + sizez.val();
	    hc = prime * hc + cbx.val();
	    hc = prime * hc + cby.val();
	    hc = prime * hc + cbz.val();	    
	    hc = prime * hc + chunk.val();
	    hc = prime * hc + unroll.val();		
		return hc;
	}
	
	// ---- test for input params --- ///
	public static void main(String[] args) {
		// tricubic pattern
		StencilPattern sp = new StencilPattern();
		for(int i= -1; i<=2; i++)
			for(int j= -1; j<=2; j++)
				for(int k= -1; k<=2; k++)
		{
			sp.add(new Point3D( i, j, k));			
		}
		sp.sort();	
		// input param		
		ExecutionParams ep1 = new ExecutionParams(new int[]{512,512}, new Random());
		System.out.println("- - -");
		System.out.println(ep1);
		System.out.println("- - -");
		System.out.println(ep1.toEncodedString());
		System.out.println();
		
		ExecutionParams ep2 = new ExecutionParams(new int[]{512,512,256}, new int []{16,16,1}, 4, 1);
		System.out.println("- - -");
		System.out.println(ep2);
		System.out.println("- - -");
		System.out.println(ep2.toEncodedString());
		
		// test for equals
		System.out.println("- - - testing equals()");
		System.out.println(ep1.equals(System.out));
		System.out.println(ep1.equals(ep2));
		System.out.println(ep1.equals(new ExecutionParams(new int[]{512,512}, new Random())));
		System.out.println(ep2.equals(new ExecutionParams(new int[]{512,512,256}, new int []{16,16,1}, 4, 1)));
		
		// test for hashcode
		System.out.println("- - - testing hashcode()");		
		System.out.println(ep1.hashCode() + " / " + ep2.hashCode());
		System.out.println(ep1.hashCode() + " / " + new ExecutionParams(new int[]{512,512}, new Random() ).hashCode());
		System.out.println(ep2.hashCode() + " / " + new ExecutionParams(new int[]{512,512,256}, new int []{16,16,1}, 4, 1).hashCode());

		System.out.println(ep1.hashCode() == ep2.hashCode());
		System.out.println(ep1.hashCode() == new ExecutionParams(new int[]{512,512}, new Random() ).hashCode());
		System.out.println(ep2.hashCode() == new ExecutionParams(new int[]{512,512,256}, new int []{16,16,1}, 4, 1).hashCode());

		
	}

}
