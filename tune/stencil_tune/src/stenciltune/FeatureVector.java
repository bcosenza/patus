package stenciltune;

import java.util.TreeMap;

/**
 * A vector of features. Each feature has an index and an associated value. 
 * By default, all N features are initialized to zero, therefore only non-zero values are stored.
 * It is implemented as sorted map (TreeMap) the output string should be sorted, as requried by     
 */
public class FeatureVector {
    private final int N;             	  // length
    private TreeMap<Integer, Double> st;  // the vector, represented by index-value pairs

    // initialize the all 0s vector of length N
    public FeatureVector(int N) {
        this.N  = N;
        this.st = new TreeMap<Integer, Double>();
    }

    // put st[i] = value
    public void put(int i, double value) {
        if (i < 0 || i >= N) throw new RuntimeException("Illegal index");
        if (value == 0.0) ; // st.remove(i); -- do nothing
        else              st.put(i, value);
    }

    // remove a value
    public void remove(int i) {
    	st.remove(i);
    }
    
    // return st[i]
    public double get(int i) {
        if (i < 0 || i >= N) throw new RuntimeException("Illegal index");
        if (st.containsKey(i)) return st.get(i);
        else                return 0.0;
    }

    // return the number of nonzero entries
    public int nonZero() {
        return st.size();
    }

    // return the size of the vector
    public int size() {
        return N;
    }

    // return the dot product of this vector a with b
    public double dot(FeatureVector b) {
        FeatureVector a = this;
        if (a.N != b.N) throw new RuntimeException("Vector lengths disagree");
        double sum = 0.0;

        // iterate over the vector with the fewest nonzeros
        if (a.st.size() <= b.st.size()) {
            for (int i : a.st.keySet())
                if (b.st.containsKey(i)) sum += a.get(i) * b.get(i);
        }
        else  {
            for (int i : b.st.keySet())
                if (a.st.containsKey(i)) sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    // return the 2-norm
    public double norm() {
    	FeatureVector a = this;
        return Math.sqrt(a.dot(a));
    }

    // return alpha * a
    public FeatureVector scale(double alpha) {
    	FeatureVector a = this;
    	FeatureVector c = new FeatureVector(N);
        for (int i : a.st.keySet()) c.put(i, alpha * a.get(i));
        return c;
    }

    // return a + b
    public FeatureVector plus(FeatureVector b) {
    	FeatureVector a = this;
        if (a.N != b.N) throw new RuntimeException("Vector lengths disagree");
        FeatureVector c = new FeatureVector(N);
        for (int i : a.st.keySet()) c.put(i, a.get(i));                // c = a
        for (int i : b.st.keySet()) c.put(i, b.get(i) + c.get(i));     // c = c + b
        return c;
    }

    /** Return a string representation of the feature vector. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i : st.keySet()) {
            s.append(i + ":"+ st.get(i).doubleValue() + " ");
        }
        return s.toString();
    }

    /** Return a string representation; in this case the encoded version is the same of the normal one. */
    public String toEncodedString() {
    	return toString();
    }
    

    /// ---- test for feature vector --- ///
    public static void main(String[] args) {
    	FeatureVector a = new FeatureVector(10);
    	FeatureVector b = new FeatureVector(10);
        a.put(3, 0.50);
        a.put(9, 0.75);
        a.put(6, 0.11);
        a.put(6, 0.00);
        b.put(3, 0.60);
        b.put(4, 0.90);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a dot b = " + a.dot(b));
        System.out.println("a + b   = " + a.plus(b));
        
        System.out.println("");
        System.out.println("To string");
        System.out.println(a);
        System.out.println(b); // the output should be already sorted
        
        // try removal
        a.remove(3);
        a.remove(6);
        System.out.println("");
        System.out.println("a = " + a);
    }

}


