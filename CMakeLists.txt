cmake_minimum_required (VERSION 2.6)
project (PatusBenchmark)


SET(PATUS "patus")

OPTION(BUILD_MODEL
  "Build the model (training code generation, execution, training)"
  ON)

OPTION(BUILD_DOUBLE
  "Enable support for double"
  ON)


OPTION(BACKEND_ARM
  "Use the ARM simple backend"
  OFF)

OPTION(BACKEND_ARM_NEON_CROSS
  "Use the ARM gcc cross-compilation backend"
  ON)

OPTION(BACKEND_ARM_NEON_NATIVE
  "Use the ARM gcc native backend"
  OFF)

OPTION(BACKEND_INTEL_OMP_AVX
  "Use the Intel OpenMP AVX backend"
  OFF)


file(GLOB_RECURSE BENCHMARK "benchmark/*.stc" )


# double compilation of the benchmarks
foreach(bench ${BENCHMARK})
  # Patus compilation
#  execute_process(COMMAND pwd)
  message(${bench})
  execute_process(COMMAND ${PATUS} ${bench})
  # backend compilation
  # TODO
endforeach(bench)
