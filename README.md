# About Patus

In many numerical codes, ranging from simple PDE solvers to complex AMR and multigrid solvers, the class of stencil computations is a constituent class of kernels. Oftentimes, stencil computations comprise a dominant part of the compute time. Therefore, in order to minimize the time to solution, it is crucial that the stencil kernels make use of the available computing resources as efficiently as possible.

The Patus framework is a code generation and autotuning tool for the class of stencil computations. The idea behind the Patus framework is twofold: on the one hand, it provides a software infrastructure for generating architecture-specific (CPUs, GPUs) stencil code from a specification of the stencil incorporating domain-specific knowledge that enables optimizing the code beyond the abilities of current compilers. On the other hand, it aims at being an experimentation toolbox for parallelization and optimization strategies. Using a small domain specific language, the user can define the stencil kernel as shown in the example. Predefined strategies describe how the kernel is optimized and parallelized. Or custom strategies can be designed to experiment with other algorithms or to find a better mapping to the hardware.

The Patus compiler is a project by Matthias Christen. 


# About Patus-AA

This project extends the original Patus compiler inmultiple way:
First, it add a new auto-tuning framework based on machine learning. The framework introduces a training code generator that creates synthetic stencil codes to be used for model trainig, and use a mathematical formulation to encode input codes to b e sude for modeling model. The model used a novel structural approach based on ordinal regression, where code versions are ranked by performance, and top-ranked is chosen.
The second extension is a new backendd for ARM processor. The new code generator supports NEON vectorial instructions and integrates the existing multi-threading parallelization.

This extension of Patus, named Patus-AA, is a project by Biagio Cosenza (TU Berlin) in collaboration with Juan Durillo (LRZ Munich), Stefano Ermon (Stanford University) and Ben Juurlink (TU Berlin).

