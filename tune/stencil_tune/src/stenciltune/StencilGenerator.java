package stenciltune;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


/**
 *	Automatic generation of stencil patterns and stencil code. 
 *	Each pattern is a collection of points (2d or 3d) following common stencil pattern, 
 *	such as Laplacian, hypercube, hyperplane and lines.    
 *	Additional information about feature encoding is in the file feature_encoding.txt.
 *	@author Biagio
 */

public class StencilGenerator {

	public static String outputFolderString = "temp/ml_stencil_code";	
	public static File outputFolder;
			
	/* static initialization block */
	static {
		// creating an output dir
		outputFolder = new File(outputFolderString);
		if(outputFolder.mkdirs()){
			System.err.println("Error while creating the autogen output folder: " + outputFolder);
		}	
		
	}
			
	private PrintStream out;
	
	
	public StencilGenerator(){
		this.out = System.out;
	}
	
	public StencilGenerator(PrintStream ps){
		this.out = ps;
	}

	public StencilGenerator(String stencilName) throws IOException {				 
		File stencilFile = new File(outputFolder, stencilName);
		stencilFile.createNewFile();
		this.out = new PrintStream(stencilFile);				
	}

	public void close(){
		this.out.close();
	}
	
	/**
  
       *
       * 
	 **W** LaPlacian 
	   *
	   *
	   
	 */
	public StencilPattern getLaPlacian2D(int size){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		sp.add(new Point2D(0, 0));
		for(int i= -size; i<=size; i++)
			if(i!=0)
				sp.add(new Point2D( i, 0));				
		for(int i= -size; i<=size; i++)
			if(i!=0)
				sp.add(new Point2D( 0, i));
		sp.sort();
		return sp;
	}
	
	public StencilPattern getLaPlacian3D(int size){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		sp.add(new Point3D(0, 0, 0));
		for(int i= -size; i<=size; i++)
			if(i!=0)
				sp.add(new Point3D( i, 0, 0));				
		for(int i= -size; i<=size; i++)
			if(i!=0)
				sp.add(new Point3D( 0, i, 0));
		for(int i= -size; i<=size; i++)
			if(i!=0)
				sp.add(new Point3D( 0, 0, i));		
		sp.sort();
		return sp;
	}
	
	
	/**
	  
	   * 
	   *  
	   W   Line (spanning only on one dimension)
	   * 
	   *
		   
	*/
	public StencilPattern getLine2D(int size, int dim){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		sp.add(new Point2D(0, 0));
		if(dim == 0)
			for(int i=1; i<=size; i++){
				sp.add(new Point2D( i, 0));
				sp.add(new Point2D(-i, 0));
			}		
		else
		if (dim == 1)
			for(int i=1; i<=size; i++){
				sp.add(new Point2D( 0, i));
				sp.add(new Point2D( 0,-i));
			}
		else throw new RuntimeException("Dimension not supported (only 0 and 1) instead of "+dim);
		sp.sort();
		return sp;
	}
	
	public StencilPattern getLine3D(int size, int dim){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		sp.add(new Point3D(0, 0,0));
		switch(dim){
		case 0:
			for(int i=1; i<=size; i++){
				sp.add(new Point3D( i, 0, 0));
				sp.add(new Point3D(-i, 0, 0));
			}	
			break;
		case 1:		
			for(int i=1; i<=size; i++){
				sp.add(new Point3D( 0, i, 0));
				sp.add(new Point3D( 0,-i, 0));
			}
			break;
		case 2:
			for(int i=1; i<=size; i++){
				sp.add(new Point3D( 0, 0, i));
				sp.add(new Point3D( 0, 0,-i));
			}
			break;
		default:
			throw new RuntimeException("Dimension not supported (only 0, 1 and 2) instead of "+dim);
		}
		sp.sort();
		return sp;
	}
	
	
	/**
	  
	   * 
	   *  
	   W   Hyper-plane (only 3d, spans one 2 of 3 dimensions) 
	   * 
	   *
		   
	*/	
	/* dim indicates the free dimension */
	public StencilPattern getHyperplane3D(int size, int dim){		
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		// sp.add(new Point3D(0, 0, 0));
		switch(dim){
		case 0:
			for(int i=-size; i<=size; i++)
				for(int j=-size; j<=size; j++)
			{
				sp.add(new Point3D( i, j, 0));			
			}
			break;
		case 1:
			for(int i=-size; i<=size; i++)
				for(int j=-size; j<=size; j++)
			{
				sp.add(new Point3D( i, 0, j));			
			}	
			break;
		case 2:		
			for(int i=-size; i<=size; i++)
				for(int j=-size; j<=size; j++)
			{
				sp.add(new Point3D( 0, i, j));			
			}
			break;
		default: 
			throw new RuntimeException("Dimension not supported (only 0, 1 and 2) instead of "+dim);
		}
		sp.sort();
		return sp;		
	}
	
	
	/**
	    ***** 
	    *****
	    **W**   Hyper-cube
	    *****
	    *****
	 */
	public StencilPattern getHypercube2D(int size){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		for(int i= -size; i<=size; i++)
			for(int j= -size; j<=size; j++)
		{
			sp.add(new Point2D( i, j));			
		}
		sp.sort();
		return sp;
	}
	
	public StencilPattern getHypercube3D(int size){
		if(size == 0) throw new RuntimeException("Size has to be >0");
		StencilPattern sp = new StencilPattern();
		for(int i= -size; i<=size; i++)
			for(int j= -size; j<=size; j++)
				for(int k= -size; k<=size; k++)
		{
			sp.add(new Point3D( i, j, k));			
		}
		sp.sort();
		return sp;
	}
	
	
	/**
	 * 	Generate Patus' DSL code from an input 2D stencil pattern.
	 *	The code is printed into a PrintStream.
	 * 
	 *	IMPORTANT NOTE: current implementation only supports <bufferNum> = 1
	 */		
	public void generate2DStencilCode(InputParams ip){
			
		String typeName = (ip.getTypeSize()==0) ? "float" : "double";
		StencilPattern sp = ip.getPattern();
		
		out.println("/* auto-gen 2d stencil for pattern: ");
		out.println("  "+ ip.getPattern().toString() );
		// Note: input size is not encoded here (is not in InputParams), as it changes at execution time
		out.println("   "+ ip.toEncodedString() );		
		out.println("*/");
		out.println("stencil autogen2d (");
		
		// --- case bufNum is 1
		if(ip.getBufferNum() == 1){
			// arguments
			out.println("  "+ typeName +" grid U) {");
			// body
			out.println("  domainsize = ("+sp.getMaxRadius()+" .. width-"+sp.getMaxRadius()+", "+sp.getMaxRadius()+" .. height-"+sp.getMaxRadius()+");");
			out.println();
			out.println("  operation {");
			//out.println("    "+ typeName +" t =                                ");
			out.println("		U[x, y; t+1] = ");
			out.print("      ");
			int count = 1;			
			for(StencilPoint p : sp){
				String aConst = ""+(count*7); // just a constant, per-sample value			
			    String xs = (p.getX()>=0) ? ("+"+p.getX()) : (""+p.getX());
			    String ys = (p.getY()>=0) ? ("+"+p.getY()) : (""+p.getY());
				out.print("U[x"+xs+", y"+ys+"; t] * "+ aConst);
				if(count == sp.size() ) out.println(";"); // Note: <count> starts from 1
				else out.print(" + ");
				count++;
			}			
			out.println("  }                                      ");
			out.println("}                                        ");			
		}		
		// --- case bufNum > 1
		else {
			/*
			 * Note(Biagio): we do not support (i.e., we do no generate code pattern for) 2D stencil with more than one read buffer.
			 * The reason is that is very hard to have similar code working on Patus due to a nasty front-end bug.
			 * Nevertheless, there are no codes like that in Patus test benchmarks, and likely this is the reason such bug was unspotted so far. 
			 */
			throw new RuntimeException("Stencil code generation of 2d code with more than one buffer is not currently supported.");
		} // --- case bufNum > 1
	}

	/**
	 * Generate Patus' dsl code from an input 3D stencil pattern.
	 * The code is printed into a PrintStream.
	 * 
	 *	IMPORTANT NOTE: to overcome a nasty bug in Patus compiler with input stencil
	 *	having more than one buffer, the code generator follow two different approaches
	 *	1.	if <bufferNum> is 1, we have only one read buffer and one write buffer
	 *		therefore we use the same buffer for input and output (overall only one)
	 *	2.	if <bufferNum> is bigger than 1, we have 2 (or 3) read buffers and one write buffer
	 *		therefore we use 2 (or 3) additional buffers, for a total of 3 (or 4). 
	 */
	public void generate3DStencilCode(InputParams ip){
		
		String typeName = (ip.getTypeSize()==0) ? "float" : "double";
		StencilPattern sp = ip.getPattern();
		
		out.println("/* auto-gen 3d stencil for pattern: ");
		out.println("   "+ ip.getPattern().toString() );
		// Note: input size is not encoded here (is not in InputParams), as it changes at execution time
		out.println("   "+ ip.toEncodedString() );				
		out.println("*/");


		final int bOffset = sp.getMaxRadius();
		String bufferSize = "(0 .. x_max+"+bOffset+", 0 .. y_max+"+bOffset+", 0 .. z_max+"+bOffset+")";
		String domainSize = "("+bOffset+" .. x_max, "+bOffset+" .. y_max, "+bOffset+" .. z_max)";

		// --- special case:  hyper-cube 3d
		// Unfortunately, Patus compilation takes too long time (and often goes out-of-memory)
		// for dense patterns such as 3d hypercube. A way to fix that in Patus is to use
		// its multi-dimensional language feature (see tricubic).
		if(sp.isCube()){
			out.println("// code path 1");
			out.println("stencil autogen3d (");
			// arguments
			out.println("         "+ typeName +" grid V" + bufferSize + ",");	// in 
			for(int bN = 0; bN<ip.getBufferNum(); bN++){				// out
				out.print("   const "+ typeName +" grid U" + bN + bufferSize);
				if(bN != ip.getBufferNum()-1)	out.println(",");
			}			
			out.println(")");
			out.println("{");			
			// body
			int radius = sp.getCube(); 
			String range = "-"+radius+".."+radius;			
			out.println("   domainsize = "+ domainSize + ";");
			out.println();
			out.println("   operation {");  
			for(int bN = 0; bN<ip.getBufferNum(); bN++){
				out.println("    "+ typeName +" t" + bN +"["+ range +"] = {");
				for(int k = -radius; k<=radius; k++){
					String aConst = ""+(k*7); // just a constant, per-sample value
					out.print("        "+aConst + " * U"+bN+"[x,y,z]");
					if(k<radius) out.println(",");
					else out.println();
				}				
				out.println("      };");
			}	
			
			// final gathering from multiple buffers
			out.println("");
			out.print  ("      V[x, y, z; t+1] = ");			
			if(ip.getBufferNum() == 1){
				out.print("{ i="+range+" } sum(");
				out.print("t0[i] * ");
				out.println("V[x+i, y, z; t]);");
			}
			else if(ip.getBufferNum() == 2){
				out.print("{ i="+range+", j="+range+" } sum(");
				out.print("t0[i] * t1[j] * ");
				out.println("V[x+i, y+j, z; t]);");
			}
			else if(ip.getBufferNum() == 3){
				out.print("{ i="+range+", j="+range+", k="+range+" } sum(");
				out.print("t0[i] * t1[j] * t2[k] * ");
				out.println("V[x+i, y+j, z+k; t]);");
			}
			out.println();
			out.println("  }");		
			out.println("}");
		}		
		// if is not an hypercube, we generate the stencil point-by-point
		// --- case bufNum is 1
		else
		if(ip.getBufferNum() == 1){
			out.println("// code path 2");
			out.println("stencil autogen3d ("+typeName +" grid U) {");
			//out.println("  domainsize = (1 .. x_max, 1 .. y_max, 1 .. z_max);");
			out.println("  domainsize = "+ domainSize + ";");
			out.println();
			out.println("  operation {");		
			out.println("    U[x, y, z; t+1] = ");
			out.print(  "      ");
			int count = 1;
		
			for(StencilPoint p : sp){
				String aConst = ""+(count*7); // just a constant, per-sample value	
			    String xs = (p.getX()>=0) ? ("+"+p.getX()) : (""+p.getX());
			    String ys = (p.getY()>=0) ? ("+"+p.getY()) : (""+p.getY());
			    String zs = (p.getZ()>=0) ? ("+"+p.getZ()) : (""+p.getZ());
				out.print("U[x"+xs+", y"+ys+", z"+zs+"; t] * "+ aConst);
				if(count == sp.size() ) out.println(";"); // Note: <count> starts from 1
				else out.print(" + ");
				count++;
			}		
			out.println("  }                                      ");
			out.println("}                                        ");
		}
		// --- case bufNum > 1
		else {
			out.println("// code path 3");
			// code-gen. similar to gradient, but with a temporary variable
			out.println("stencil autogen3d (");

			// arguments			
			out.println("         "+ typeName +" grid V" + bufferSize + ",");	// in 
			for(int bN = 0; bN<ip.getBufferNum(); bN++){				// out
				out.print("   const "+ typeName +" grid U" + bN + bufferSize);
				if(bN != ip.getBufferNum()-1)	out.println(",");
			}			
			out.println(")");
			out.println("{");
			// body
			out.println("   domainsize = "+ domainSize + ";");
			out.println();
			out.println("   operation {");  
			for(int bN = 0; bN<ip.getBufferNum(); bN++){
				out.println("    "+ typeName +" t" + bN + " = ");
				out.println("      (");
				out.println("        ");
				int count = 1;			
				for(StencilPoint p : sp){
					String aConst = ""+(count*7); // just a constant, per-sample value			
				    String xs = (p.getX()>=0) ? ("+"+p.getX()) : (""+p.getX());
				    String ys = (p.getY()>=0) ? ("+"+p.getY()) : (""+p.getY());
				    String zs = (p.getZ()>=0) ? ("+"+p.getZ()) : (""+p.getZ());
					//out.print("U"+bN+"[x"+xs+", y"+ys+", z"+zs+"; t] * "+ aConst);
					out.print("U"+bN+"[x"+xs+", y"+ys+", z"+zs+"] * "+ aConst);
					if(count == sp.size() ) out.println("\n      );"); // Note: <count> starts from 1 
					else out.print(" + ");
					count++;
				}
			}
			// gathering from the <bufferNum> buffers
			out.print("    V[x, y, z; t] = ");
			for(int bN = 0; bN<ip.getBufferNum(); bN++){ 
				out.print(" t" + bN);
				out.print( (bN==ip.getBufferNum()-1)?";":" + " );
			}				
			out.println();
			out.println("  }");		
			out.println("}");
		}
		out.println();
		out.println();
	}
	
	
	
	/**
	 * Automatically generates stencil codes for all supported patterns, with different buffer num and
	 * up to a given pattern radius. 
	 * @param args
	 * @throws IOException
	 */
	public static final void main(String args[]) throws IOException {		

		// parameters				
		final int maxPatternRadius = 3; // final version has to be from 1 to (maybe)3
		//StencilPoint.MAX_STENCIL_SIZE = 10;//maxPatternRadius*2+1;				
		
		int genBufferNum[] = { 1, 3}; // { 1, 2, 3};
		int maxTypeSize = 1; // type size is 0 for 3d-bit types (float and int), or 1 for 64 (double)  
		
		
		System.out.println("Automatic stencil code generation in " + outputFolderString);

		// Important note: (some) 3D kernels are trained with 1 and 3 buffers, but 2D kernels only 1 buffer (there is a problem with the Patus' backed compiler)		

//	//	// 2D kernels here
 
		System.out.println();
		System.out.println("LaPlacian 2D");
		for(int size = 1; size <= maxPatternRadius; size ++)
			//for(int buffer = 1; buffer <= maxBufferNum; buffer ++)
				for(int type = 0; type <= maxTypeSize; type ++)
		{	
			int buffer = 1;
			String stencilName = "autogen_laplace2d_s" + size + "_t" + type + ".stc"; 

			StencilGenerator sg = new StencilGenerator(stencilName);			
			StencilPattern sp = sg.getLaPlacian2D(size);			
			InputParams ip = new InputParams(sp, buffer, type);
			
			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate2DStencilCode(ip);						
			sg.close();
		}
		
		
		System.out.println();
		System.out.println("Line 2D");
		for(int size : new int[]{ 1, 3}) //	for(int size = 1; size <= maxPatternRadius; size ++)
			//for(int buffer = 1; buffer <= maxBufferNum; buffer *= 2)
				for(int dim = 0; dim <= 1; dim++)
					for(int type = 0; type <= maxTypeSize; type ++)
		{
			int buffer = 1;			
			String stencilName = "autogen_line2d_s" + size + "_t" + type + "_d" + dim + ".stc";
			
			StencilGenerator sg = new StencilGenerator(stencilName);			
			StencilPattern sp = sg.getLine2D(size,dim);			
			InputParams ip = new InputParams(sp, buffer, type);

			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate2DStencilCode(ip);						
			sg.close();
		}
		
		
		System.out.println();
		System.out.println("Hypercube 2D");
		for(int size : new int[]{ 1, 2, 3})
			//for(int buffer = 1; buffer <= maxBufferNum; buffer *= 2)
				for(int type = 0; type <= maxTypeSize; type ++)
		{
			final int buffer = 1;
			String stencilName = "autogen_hypercube2d_s" + size + "_t"  + type + ".stc";			
			
			StencilGenerator sg = new StencilGenerator(stencilName);			
			StencilPattern sp = sg.getHypercube2D(size);			
			InputParams ip = new InputParams(sp, buffer, type);

			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate2DStencilCode(ip);						
			sg.close();
		}		
		
//	//	// 3D kernels here
		
		System.out.println();
		System.out.println("LaPlacian 3D");		
		for(int size = 1; size <= maxPatternRadius; size ++)			
			for(int buffer : new int[]{ 1, 2, 3})  	
				for(int type = 0; type <= maxTypeSize; type ++)
		{	
//			if(buffer == 3 && size >1) continue; // NOTE: unfortunately, we had memory issues with 3 buffers and many points with Patus onXeon E5
			
			String stencilName = "autogen_laplace3d_s"+ size + "_b" + buffer + "_t" + type + ".stc";		
			
			StencilGenerator sg = new StencilGenerator(stencilName);			
			StencilPattern sp = sg.getLaPlacian3D(size);			
			InputParams ip = new InputParams(sp, buffer, type);
				
			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate3DStencilCode(ip);						
			sg.close();
		}
	
		
		// Note: technically, some line3d codes are similar to line 2d
		System.out.println();
		System.out.println("Line 3D");  
		for(int size : new int[]{ 1}) // for(int size = 1; size <= maxPatternRadius; size ++)
			// for(int buffer : genBufferNum)			
				for(int dim = 0; dim <= 2; dim++)
					for(int type = 0; type <= maxTypeSize; type ++)
		{
			final int buffer = 1;
			String stencilName = "autogen_line3d_s" + size + "_b" + buffer + "_t" + type + "_d" + dim + ".stc";		

			StencilGenerator sg = new StencilGenerator(stencilName);			
			StencilPattern sp = sg.getLine3D(size,dim);			
			InputParams ip = new InputParams(sp, buffer, type);

			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate3DStencilCode(ip);						
			sg.close();
		}

		
		System.out.println();
		System.out.println("Hyperplane 3D");
		for(int size : new int[]{ 1, 2}) //	for(int size = 1; size <= maxPatternRadius; size ++)
			//for(int buffer : genBufferNum)				
				for(int dim = 0; dim <= 2; dim++)
					for(int type = 0; type <= maxTypeSize; type ++)
		{
			final int buffer = 1;
			String stencilName = "autogen_hyperplane3d_s" + size + "_b" + buffer + "_t" + type + "_d" + dim + ".stc";		

			StencilGenerator sg = new StencilGenerator(stencilName);
			StencilPattern sp = sg.getHyperplane3D(size, dim);
			InputParams ip = new InputParams(sp, buffer, type);

			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate3DStencilCode(ip);						
			sg.close();
		}		

		System.out.println();
		System.out.println("Hypercube 3D"); 
		for(int size : new int[]{ 1, 2}) 		//for(int size = 1; size <= maxPatternRadius; size ++)
			for(int buffer : genBufferNum)			
				for(int type = 0; type <= maxTypeSize; type ++)
		{
			if(size == 3 && buffer == 3) continue; // NOTE: unfortunately, this configuration had memory issues on Patus/Xeon E5

			String stencilName = "autogen_hypercube3d_s" + size + "_b" + buffer + "_t" + type + ".stc";
			
			StencilGenerator sg = new StencilGenerator(stencilName);
			StencilPattern sp = sg.getHypercube3D(size);
			InputParams ip = new InputParams(sp, buffer, type);

			System.out.print("   " + ip + " / normalized: " + ip.toEncodedString());
			sg.generate3DStencilCode(ip);						
			sg.close();
		}
		
		System.out.println();
		System.out.println("Stencil kernels generated into the folder: " + outputFolder.getAbsolutePath());
		System.out.println();	
	}
	
}
