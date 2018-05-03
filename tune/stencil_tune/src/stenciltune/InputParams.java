package stenciltune;


/**
 * Static input parameters (K_s): 
 * stencil pattern, buffer num, type size, and input sizes. 
 */
public class InputParams {
	
	StencilPattern pattern; // 0 to 2220
	Parameter bufferNum;    // 2221
	Parameter typeSize;     // 2222	
	
	/** Help function: throws a runtime exception whether an encoded value has a wrong value
	 *  e.g. a NaN or is out of the range [0,1]. 
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
		bufferNum = new Parameter(new int[]{1,2,3});
		typeSize  = new Parameter(new int[]{0,1});		
	}	
	
	
	public InputParams(StencilPattern _pattern, int _bufNum, int _typeSize){
		// stencil shape
		pattern = _pattern; 	
		// list of supported parameters
		initParam();
		
		bufferNum.setVal(_bufNum);
		typeSize.setVal(_typeSize);		
	}	
	
	
	public FeatureVector toFeatureVector(){		
		// pattern to vector
		FeatureVector fv = pattern.toFeatureVector();		
		
		// buffer num (1 to 3), linear encoding	(e.g., 0.0-0.5-1.0)	
		double bn = (bufferNum.val()-1.0) / 2.0;
		runtimeCheck(bn);
		fv.put(2221,bn);
		
		// type size (float or double => 0.0 or 1.0)
		if(typeSize.val() == 0)
			fv.put(2222,0.0);
		else if(typeSize.val() == 1)
			fv.put(2222,1.0);
		else
			throw new RuntimeException("Type size, value not supported!");
		
		return fv;
	}	

	
	/** 
	 *	Return a feature vector representing only the static features, e.g. stencil pattern, buffer num, and type size, 
	 * 	but NOT input sizes.	
	public FeatureVector toStaticFeatureVector(){
		FeatureVector fv = toFeatureVector();
		fv.remove(2223);
		fv.remove(2224);
		fv.remove(2225);
		return fv;
	}
	*/
	
	public String toEncodedString(){
		return toFeatureVector().toEncodedString();
	}	
 
	public String toString(){
		return   " shape("+ pattern.toString() + ") bufNum(" + bufferNum + ") typeSize(" + typeSize + ")";
	}
	
	
	/** Getter */
	public StencilPattern getPattern(){
		return pattern;		
	}
	public int getBufferNum(){
		return bufferNum.val();
	}
	public int getTypeSize(){
		return typeSize.val();
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
		InputParams ip = new InputParams(sp, 2, 1);
		System.out.println("- - -");
		System.out.println(ip);
		System.out.println("- - -");
		System.out.println(ip.toEncodedString());		
	}
	
}
