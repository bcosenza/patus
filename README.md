# About Patus

In many numerical codes, ranging from simple PDE solvers to complex AMR and multigrid solvers, the class of stencil computations is a constituent class of kernels. Oftentimes, stencil computations comprise a dominant part of the compute time. Therefore, in order to minimize the time to solution, it is crucial that the stencil kernels make use of the available computing resources as efficiently as possible.

The Patus framework is a code generation and autotuning tool for the class of stencil computations. The idea behind the Patus framework is twofold: on the one hand, it provides a software infrastructure for generating architecture-specific (CPUs, GPUs) stencil code from a specification of the stencil incorporating domain-specific knowledge that enables optimizing the code beyond the abilities of current compilers. On the other hand, it aims at being an experimentation toolbox for parallelization and optimization strategies. Using a small domain specific language, the user can define the stencil kernel as shown in the example. Predefined strategies describe how the kernel is optimized and parallelized. Or custom strategies can be designed to experiment with other algorithms or to find a better mapping to the hardware.

The Patus compiler is a project by Matthias Christen. 


# About Patus-AA

The Patus-AA project extends the original Patus compiler in different ways.
First, it adds a new auto-tuning framework based on machine learning. The framework introduces a training code generator that creates synthetic stencil codes (used for model trainig), and a feature encoding part that translates input codes into feature vector (for modeling). The model uses a novel structural learning approach based on ordinal regression where code versions are ranked by performance, and the predicted top-ranked is chosen by the autotuner.

[Installing the machine learning-based autotuner](./tune)


The second extension is a new backend for ARM processor. This new code generator supports NEON vectorial instructions and integrates the existing multi-threading parallelization to obtain higher performance on ARM-based embedded system such as Raspberry Pi.

[Using the ARM NEON backend](./arm-neon.md)


This extension of Patus, named Patus-AA, is a project by Biagio Cosenza (TU Berlin) in collaboration with Juan Durillo (LRZ Munich), Stefano Ermon (Stanford University) and Ben Juurlink (TU Berlin).

