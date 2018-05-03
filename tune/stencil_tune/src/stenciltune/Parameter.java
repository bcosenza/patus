package stenciltune;

import java.util.Random;

/**  
 *	A stencil parameter is an integer value, whose supported values are specified at creation time (e.g., you can specify only power-of-two values).
 */
public class Parameter {	
	
	private int value;	
	private int supportedValues[];
	private int min;
	private int max;
	
	public Parameter(int _value, int _supportedValues[]){		
		supportedValues = _supportedValues;
		updateMinMax();
		setVal(_value);
	}
	
	public Parameter(int _supportedValues[]){		
		supportedValues = _supportedValues;
		updateMinMax();
		setVal(min);		
	}
	
	private void updateMinMax(){
		min = supportedValues[0];
		max = supportedValues[0];
		for(int v : supportedValues){
			min = Math.min(min, v);
			max = Math.max(max, v);
		}				
	}

	public void setVal(int _value){
		if(_value < min) throw new RuntimeException("Parameter.setVal() smaller than the supported minimum");
		if(_value > max) throw new RuntimeException("Parameter.setVal() bigger than the supported maximum");				
		if(Double.isNaN(_value)) throw new RuntimeException("Parameter.setVal() is NaN");;
		value = _value;
	}
	
	public void randomize(Random rand){
		int index = rand.nextInt(supportedValues.length);		
		setVal(supportedValues[index]);
	}
	
	public int val(){
		return value;
	}

	public int min(){
		return min;
	}
	
	public int max(){
		return max;
	}
	
	public int[] getSupportedValues(){
		return supportedValues;
	} 

	/** Returns a string with the parameter value */
	public String toString(){
		return ""+value;
	}	

	
	/// --- test for Parameter --- /// 
    public static void main(String[] args) {
    	Parameter bn = new Parameter(new int[]{1,2,3});
    	System.out.println("value "+ bn.val());
    	System.out.println("min "+ bn.min());
    	System.out.println("max "+ bn.max());
    	
    	bn = new Parameter(2,new int[]{1,2,3});
    	System.out.println("value "+ bn.val());
    	System.out.println("min "+ bn.min());
    	System.out.println("max "+ bn.max());
    	
    	bn = new Parameter(3,new int[]{1,2,3});
    	System.out.println("value "+ bn.val());
    	System.out.println("min "+ bn.min());
    	System.out.println("max "+ bn.max());
    	
    	
    	/*
    	bn = new Parameter(4, new int[]{1,2,3}); // Runtime error!
    	System.out.println("value "+ bn.val());
    	System.out.println("min "+ bn.min());
    	System.out.println("max "+ bn.max());
    	*/

    }
	
}
