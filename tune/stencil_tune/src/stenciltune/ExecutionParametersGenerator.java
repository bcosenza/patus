package stenciltune;

import java.util.*;

/**
 *	The ExecutionParametersGenerator randomly generates runtime/execution parameters.
 *	It creates exactly <max> parameters, all different (no duplicated parameters on it, 
 *  assured by a HashSet-based implementation).
 */
public class ExecutionParametersGenerator {
	
	private int max;	
	private int[] sizes;
	private boolean isTwoDim;
	
	private Random rand;
	protected HashSet<ExecutionParams> params;
	protected Iterator<ExecutionParams> pi; 
	
	public boolean hasNext(){
		return pi.hasNext();		
	}
		
	public ExecutionParams next(){
		ExecutionParams ep = (ExecutionParams) pi.next();
		return ep;
	}
	
	public ExecutionParametersGenerator(int _sizes[], int _max, boolean _isTwoDim){
		this.max = _max;
		this.sizes = _sizes;
		this.isTwoDim = _isTwoDim;
		
		// we do NOT set any seeds, despite un-deterministic behavior 
		rand = new Random();
		
		params = new HashSet<ExecutionParams>();		
		
		// precalculate <max> parameters
		precalculateParams();
		pi = params.iterator();
	}
	
	protected void precalculateParams(){
		while(params.size() < max){
			ExecutionParams ep = new ExecutionParams(sizes, rand);	
			//if(isTwoDim)				
			params.add(ep);			
		}
		//System.out.println("Generated " + params.size() + " params");
	}	

/*	
			// precompute params
			for(int bx : blockVal)
				for(int by : blockVal)
					for(int bz : zBlockVal) // block
						for(int c : chunkVal) // chunk
							for(int u : unrollVal) // unrolling								
								params.add(new ExecutionParams(new int[]{bx,by,bz},c,u));		}		
*/
	
	
	// TOFIX TODO need to handle differently 2d (z=1) and 3d
	
	
	/**
	 *	Code to generates <N> randomly sampled parameters with the search space.
	 *  Usage: 
	 *  > ExecutionParamtersGenerator <N> <sizeX> <sizeY> <sizeZ>
	 *  where <N> is the number of samples to be generated and <sizeZ> is optional (0 value, whether absent).
	 *  depending on the dimensionality of the the stencil kernel. 
	 */
	public static final void main(String args[]) throws Exception {		
		
		//args = new String[]{"20", "128", "128", "128"};
		//args = new String[]{"2", "128", "128"};		
		// System.out.println(args.length); 
		
		// Warning: Java arguments do not include the binary file name		
		int sizeX, sizeY, sizeZ = 1;
		boolean isTwoDim;
		
		if(args.length >= 3){
			// arguments processing
			int samplesNum = Integer.parseInt(args[0]);
			// System.out.println(samplesNum);
			sizeX = Integer.parseInt(args[1]);
			sizeY = Integer.parseInt(args[2]);			
			if(args.length >= 4) {				
				sizeZ = Integer.parseInt(args[3]);
				isTwoDim = false;
			}
			else {
				isTwoDim = true;
			}
			// generating parameters
			ExecutionParametersGenerator epg = new ExecutionParametersGenerator(new int[]{sizeX,sizeY,sizeZ}, samplesNum, isTwoDim);	
			
			while(epg.hasNext()){
				ExecutionParams ep = epg.next();
				if(isTwoDim)				
					System.out.println(ep.toPatus2DInputString() + " # " + ep.toFeatureVector());
				else
					System.out.println(ep.toPatus3DInputString() + " # " + ep.toFeatureVector());			
			}			
		}
		else {
			System.out.println("Code to generates <N> randomly sampled parameters with the search space.");
			System.out.println("Usage:"); 
			System.out.println("ExecutionParamtersGenerator <N> <sizeX> <sizeY> <sizeZ>"); 
			System.out.println("where <N> is the number of samples to be generated and <sizeZ> is optional,"); 
			System.out.println("depending on the dimensionality of the the stencil kernel ");
			
			//for(String t : args)	System.out.print(" _" + t + "_ " );
		}
		
		//test();
	}
		
	public static void test(){			
		ExecutionParametersGenerator epg = new ExecutionParametersGenerator(new int[]{128,128,128}, 100,false);	
		
		System.out.println("---");
		int i = 0;
		while(epg.hasNext()){
			ExecutionParams ep = epg.next();
			//System.out.println("" + ep + "\t ==> " + ep.toFeatureVector());
			System.out.println(i + ") " + ep.toPatus3DInputString() + "\n   ==> " + ep.toFeatureVector());
			i++;
		}
/*				
		ExecutionParametersGenerator pg3 = ExecutionParametersGenerator.get3DUniformGenerator(100);
		System.out.println("---");
		while(pg3.hasNext()){
			System.out.println(pg3.next());
		}
		
		ExecutionParametersGenerator pg2 = ExecutionParametersGenerator.get2DUniformGenerator(100);
		System.out.println("---");
		while(pg2.hasNext()){
			System.out.println(pg2.next());
		}
*/
		
	}

}
