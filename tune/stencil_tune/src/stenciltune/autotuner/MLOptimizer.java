package stenciltune.autotuner;

import ch.unibas.cs.hpwc.patus.autotuner.AbstractOptimizer;
import ch.unibas.cs.hpwc.patus.autotuner.IRunExecutable;
import ch.unibas.cs.hpwc.patus.util.DomainPointEnumerator;

public class MLOptimizer extends AbstractOptimizer {
	
	@Override
	public void optimize (IRunExecutable run)
	{		
		
		System.out.println("Properties");		
		
		int nParamsCount = run.getParametersCount ();
		StringBuilder sbResult = new StringBuilder ();

		DomainPointEnumerator dpe = new DomainPointEnumerator ();
		for (int j = 0; j < nParamsCount; j++){
			System.out.println(j + ") from " + run.getParameterValueLowerBounds() + " to " + run.getParameterValueUpperBounds());			
			dpe.addDimension (run.getParameterLowerBounds ()[j], run.getParameterUpperBounds ()[j]);			
		}

		for (int[] rgParams : dpe)
		{
			// execute
			sbResult.setLength (0);
			double fRuntime = run.execute (rgParams, sbResult, checkBounds ());

			// check whether the runtime is better
			if (fRuntime < getResultTiming ())
			{
				setResultTiming (fRuntime);
				setResultParameters (rgParams);
				setProgramOutput (sbResult);
			}
		}
	}

	@Override
	public String getName ()
	{
		return "ML Optimizer";
	}

}
