package stenciltune;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

/**
 *	Semi-automatic feature generation for the test benchmarks in Patus.  
 *	For each code, it generates a number of configuration to be ranked.
 */
public class ExampleGenerator {

	
	static StencilGenerator sg = new StencilGenerator();

	/**
	 * See also the feature_encoding.txt to know more about the encoding.
	 * The boolean <is3d> applies to the stencil pattern (e.g., there can be a 3d stencil using a 2d pattern) 
	 */ 
	public static void printTuningFeatures(String prefix, int size[], boolean is3d){ 	

		// stencil (tuning) features to be evaluated
		ExecutionParams ep = new ExecutionParams(size, new Random()); // this instance is created only to import the supported values		
				
		// z-value for blocking
		int zBlockVal[];
		
		if(is3d){
			zBlockVal  = ep.getBlockZSupportedValues();
		}
		else
		{
			zBlockVal = new int[]{ 1 };
		}
		
		int count = 1;					
		for(int bx : ep.getBlockXSupportedValues())
			for(int by : ep.getBlockYSupportedValues())
				for(int bz : zBlockVal) // block
					for(int c : ep.getChunkSupportedValues()) // chunk
						for(int u : ep.getUnrollSupportedValues()) // unrolling								
		{
			if (size[0] < bx) continue;
			if (size[1] < by) continue;
			if (size[2] < bz) continue; // this does not include when both are 1 (e.g., 2d)			
							
            System.out.print(count + " " + prefix + " ");
            
            ExecutionParams param = new ExecutionParams(size, new int[]{bx, by, bz},c,u);               
            System.out.print(param.toEncodedString());            
			
			// a human-readable configuration is printed as comment
            if(is3d)
            	System.out.println(" \t\t # " + /* size[0] + " "+ size[1] + " " + size[2] + "  " + */ param.toPatus3DInputString());
            else
            	System.out.println(" \t\t # " + /* size[0] + " "+ size[1] + "  " + */ param.toPatus2DInputString());
			
			count ++;
		}		
		
	}
	

///////////////////////////////////////////////////////////////////////////////////////////

	
	/*	Blur goes from -2 to +2, 5x5 samples. It uses grid, but accesses are 2d	*/
	public static void generateBlur(boolean useDouble, int sizex, int sizey){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getHypercube2D(2);
		//sp.draw2D(5);		
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# blur 2d with size " + sizex + "x" + sizey);		
		printTuningFeatures("qid:1 " + ip.toEncodedString(), new int[]{sizex,sizey, 1}, false);
	}
		
	
	/* Wave1 is 3d, laplacian with a t-2 dependence */
	public static void generateWave1(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getLaPlacian3D(2);
		//sp.draw3D(5);		
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# wave1 3d with size " + size);
		printTuningFeatures("qid:2 " +  ip.toEncodedString(), new int[]{size,size,size}, true);
		//System.out.println(sp.size()); // 13
	}
	
	
	/* Wave2 is like wave1, but it uses the sum() function to sum up closest points, which is not currently encoded as feature. */
	public static void generateWave2(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getLaPlacian3D(2);
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# wave2 3d with size " + size);		
		printTuningFeatures("qid:3 " + ip.toEncodedString(), new int[]{size, size, size}, true);
		//printTuningFeatures("qid:3 " + sp.toNormalizedString() + "  " + printBufferNum(bufferNum) + "  " + printTypeSize(type) + "  " + printSizes(size, size, size) + "  ", true);		
	}
	
	
	/* Tricubic is 3d, uses three in-buffers, and hypercube-like pattern */
	public static void generateTricubic(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 3; // 3 reads + 1 write
		
		// tricubic's shape is a cube from -1 to 2
		StencilPattern sp = new StencilPattern();
		for(int i= -1; i<=2; i++)
			for(int j= -1; j<=2; j++)
				for(int k= -1; k<=2; k++)
		{
			sp.add(new Point3D( i, j, k));			
		}
		sp.sort();	
		//System.out.println(sp.size()); 64 points here
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# tricubic 3d with size " + size);
		printTuningFeatures("qid:4 " + ip.toEncodedString(), new int[]{size, size, size}, true);
		//printTuningFeatures("qid:4 " + sp.toNormalizedString() + "  " + printBufferNum(bufferNum) + "  " + printTypeSize(type) + "  " + printSizes(size, size, size) + "  ", true);		
	}
	
	
	/* Edge is 2d hypercube of size 1 (a square) */
	public static void generateEdge(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;	
		
		StencilPattern sp = sg.getHypercube2D(1);
		//System.out.println(sp.size()); // 9 points here
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# edge 2d with size " + size);
		printTuningFeatures("qid:5 " + ip.toEncodedString(), new int[]{size, size, 1}, false);			
	}
	
	
	/* Game-of-life is similar to edge */
	public static void generateGameOfLife(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1; 
		
		StencilPattern sp = sg.getHypercube2D(1);
		//System.out.println(sp.size()); // 9 points here
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# game-of-life 2d with size " + size);
		printTuningFeatures("qid:6 " + ip.toEncodedString(), new int[]{size, size, 1}, false);		//			
	}
	
	
	/* Divergence is a 3d laplacian working on three in-buffers. Note: uses double. */
	public static void generateDivergence(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 3; // 3 in, 1 out 
		
		StencilPattern sp = sg.getLaPlacian3D(1);
		sp.remove(new Point3D( 0, 0, 0));  // we remove the point (0,0,0), not included in the divergence stencil code
		//System.out.println(sp);
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# divergence 3d with size " + size);
		printTuningFeatures("qid:7 " + ip.toEncodedString(), new int[]{size, size, size}, true);
		//System.out.println(sp.size()); // 6 points here		
	}	
			
	
	/*	Gradient has a 3d, laplacian shape. I write to 3 buffers and read from one. It uses double. */
	public static void generateGradient(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1; // reads 1, write 3
		
		StencilPattern sp = sg.getLaPlacian3D(1);
		sp.remove(new Point3D( 0, 0, 0));  // we remove the point (0,0,0), not included in the gradient stencil code
		//System.out.println(sp);
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# gradient 3d with size " + size);
		printTuningFeatures("qid:8 " + ip.toEncodedString() , new int[]{size, size, size}, true);
		//System.out.println(sp.size()); // 6 points here
	}
	
	
	/* Laplacian is a simple 3d, laplacian stencil using double */
	public static void generateLaplacian(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;
		StencilPattern sp = sg.getLaPlacian3D(1);
	
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# laplacian 3d with size " + size);		
		printTuningFeatures("qid:9 " + ip.toEncodedString(), new int[]{size, size, size}, true);		
		//System.out.println(sp.size()); // 6 points here
	}
	
	/* 6th order 3d laplacian stencil */
	public static void generateLaplacian6(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getLaPlacian3D(3);
		
		// System.out.println(sp.size()); // 19 points here		
		//System.out.println(sp);

		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# 6th order laplacian 3d with size " + size);		
		printTuningFeatures("qid:10 " + ip.toEncodedString(), new int[]{size, size, size}, true);			
	}		
	
	/* Horizontal interpolation kernel, from HEVC coder */
	public static void generateHorInterp(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getLine2D(4, 0);
		sp.remove(sp.size()-1);
		
//		System.out.println(sp.size()); // 8 points here		
//		System.out.println(sp);
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# hor. interpolation 2d with size " + size);		
		printTuningFeatures("qid:11 " + ip.toEncodedString(), new int[]{size, size, 1}, false);
	}
	
	/* Vertical interpolation kernel, from HEVC coder */
	public static void generateVerInterp(boolean useDouble, int size){
		int type = (useDouble ? 1 : 0);
		int bufferNum = 1;		
		StencilPattern sp = sg.getLine2D(4, 1);
		sp.remove(sp.size()-1);
		
//		System.out.println(sp.size()); // 8 points here		
//		System.out.println(sp);		
		
		InputParams ip = new InputParams(sp, bufferNum, type);
		
//		System.out.println("# ver. interpolation 2d with size " + size);		
		printTuningFeatures("qid:12 " + ip.toEncodedString(), new int[]{size, size, 1} , false);
	}
	
	
	public static final void main(String args[]) throws Exception
	{
		/// Test Benchmark Pattern Generation ///
//		generateBlur(128,128);		
//		generateWave1(128);		
//		generateWave2();
//		generateTricubic();
//		generateEdge();
//		generateGameOfLife();
//		generateDivergence(256);		
//		generateGradient(512);
//		generateLaplacian();
//		generateLaplacian6();
//		generateHorInterp(512);		
//		generateVerInterp(512);

		boolean withTricubic = false;
		if(Arrays.asList(args).contains("tricubic")) {
			withTricubic = true;
			System.out.println("Synthetic code generation includes tricubic-like sum patterns");
		}

		generateAll(withTricubic);
	}
	
	
	public static final void generateAll(boolean withTricubic) throws Exception {
		String basePath = "data/";

		PrintStream stdout = System.out;

		boolean isDouble[] = {true, false};
		for(boolean type : isDouble) {
			String infix = "-float";
			if(type) infix = "-double";

			System.setOut(new PrintStream(new FileOutputStream(basePath + "blur" + infix + "_1024_1024.test", false)));
			generateBlur(type,1024, 1024);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "blur" + infix + "_1024_768.test", false)));
			generateBlur(type,1024, 768);

			System.setOut(new PrintStream(new FileOutputStream(basePath + "wave-1" + infix + "_64_64_64.test", false)));
			generateWave1(type,64);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "wave-1" + infix + "_128_128_128.test", false)));
			generateWave1(type,128);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "wave-1" + infix + "_256_256_256.test", false)));
			generateWave1(type,256);

			if (withTricubic) {
				System.setOut(new PrintStream(new FileOutputStream(basePath + "tricubic" + infix + "_128_128_128.test", false)));
				generateTricubic(type,128);
				System.setOut(new PrintStream(new FileOutputStream(basePath + "tricubic" + infix + "_256_256_256.test", false)));
				generateTricubic(type,256);
			}

			System.setOut(new PrintStream(new FileOutputStream(basePath + "edge" + infix + "_512_512.test", false)));
			generateEdge(type,512);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "edge" + infix + "_1024_1024.test", false)));
			generateEdge(type,1024);

			System.setOut(new PrintStream(new FileOutputStream(basePath + "game-of-life" + infix + "_512_512.test", false)));
			generateGameOfLife(type,512);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "game-of-life" + infix + "_1024_1024.test", false)));
			generateGameOfLife(type,1024);


			System.setOut(new PrintStream(new FileOutputStream(basePath + "divergence" + infix + "_64_64_64.test", false)));
			generateDivergence(type,64);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "divergence" + infix + "_128_128_128.test", false)));
			generateDivergence(type,128);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "divergence" + infix + "_256_256_256.test", false)));
			generateDivergence(type,256);


			System.setOut(new PrintStream(new FileOutputStream(basePath + "gradient" + infix + "_128_128_128.test", false)));
			generateGradient(type,128);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "gradient" + infix + "_256_256_256.test", false)));
			generateGradient(type,256);

			System.setOut(new PrintStream(new FileOutputStream(basePath + "laplacian" + infix + "_128_128_128.test", false)));
			generateLaplacian(type,128);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "laplacian" + infix + "_256_256_256.test", false)));
			generateLaplacian(type,256);

			System.setOut(new PrintStream(new FileOutputStream(basePath + "laplacian6" + infix + "_128_128_128.test", false)));
			generateLaplacian6(type,128);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "laplacian6" + infix + "_256_256_256.test", false)));
			generateLaplacian6(type,256);


			System.setOut(new PrintStream(new FileOutputStream(basePath + "hinterp" + infix + "_1024_1024.test", false)));
			generateHorInterp(type,1024);
			System.setOut(new PrintStream(new FileOutputStream(basePath + "vinterp" + infix + "_1024_1024.test", false)));
			generateVerInterp(type,1024);

		}

		stdout.println("Test set features generated in " + basePath);
		
	}
	
}
