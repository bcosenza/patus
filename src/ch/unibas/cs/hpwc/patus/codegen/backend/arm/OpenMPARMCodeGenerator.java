package ch.unibas.cs.hpwc.patus.codegen.backend.arm;

import cetus.hir.*;
import ch.unibas.cs.hpwc.patus.codegen.CodeGeneratorSharedObjects;
import ch.unibas.cs.hpwc.patus.codegen.backend.openmp.OpenMPCodeGenerator;
import ch.unibas.cs.hpwc.patus.util.ASTUtil;
import ch.unibas.cs.hpwc.patus.util.CodeGeneratorUtil;
import ch.unibas.cs.hpwc.patus.util.StringUtil;

/**
 * ARM + OpenMP backend
 * @author Biagio Cosenza
 *
 */
public class OpenMPARMCodeGenerator extends OpenMPCodeGenerator {

    private static final boolean USE_UNALIGNED_LOADS = true;


    public OpenMPARMCodeGenerator (CodeGeneratorSharedObjects data)
    {
        super (data);
    }

/*
    private Expression _mm256_shuffle_ps (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall(new NameID("_mm256_shuffle_ps"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral(n)));
    }

    private Expression _mm256_permute_ps (Expression expr1, int n)
    {
        return new FunctionCall (new NameID ("_mm256_permute_ps"), CodeGeneratorUtil.expressions (expr1.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_permute2f128_ps (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall (new NameID ("_mm256_permute2f128_ps"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_blend_ps (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall (new NameID ("_mm256_blend_ps"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_shuffle_pd (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall (new NameID ("_mm256_shuffle_pd"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_permute_pd (Expression expr1, int n)
    {
        return new FunctionCall (new NameID ("_mm256_permute_pd"), CodeGeneratorUtil.expressions (expr1.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_permute2f128_pd (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall (new NameID ("_mm256_permute2f128_pd"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (n)));
    }

    private Expression _mm256_blend_pd (Expression expr1, Expression expr2, int n)
    {
        return new FunctionCall (new NameID ("_mm256_blend_pd"), CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (n)));
    }
*/


/*
    @Override
    public Expression shuffle (Expression expr1, Expression expr2, Specifier specDatatype, int nOffset)
    {
        // check out the XOP intrinsic _mm256_cmov_si256
        // [currently doesn't seem to be included as GCC intrinsic!]

        if (nOffset == 0)
            return expr1.clone ();

        if (USE_UNALIGNED_LOADS) // FIXME check correct behaviour on ARM
        {
            String strLoadFnxName = null;
            if (specDatatype.equals (Specifier.FLOAT))
                strLoadFnxName = "vld1q_f32";       // ARMv7, ARMv8
                // strLoadFnxName = "_mm256_loadu_ps";
            else if (specDatatype.equals (Specifier.DOUBLE))
                strLoadFnxName = "vld1q_f64";       // only ARMv8(AArch64)
                // strLoadFnxName = "_mm256_loadu_pd";
            else
                throw new RuntimeException (StringUtil.concat ("Unsupported datatype: ", specDatatype.toString ()));

            return new FunctionCall (
                    new NameID (strLoadFnxName),
                    CodeGeneratorUtil.expressions (new BinaryExpression (
                            ASTUtil.castTo (ASTUtil.getPointerTo (expr1), CodeGeneratorUtil.specifiers (specDatatype, PointerSpecifier.UNQUALIFIED)),
                            BinaryOperator.ADD,
                            new IntegerLiteral (nOffset)
                    ))
            );
        }
        else
        {
            // the code below was generated using AVXSelectGenerator / PermutatorGenetic
            UserSpecifier specVecFloat = new UserSpecifier (new NameID ("float32x4_t"));
            UserSpecifier specVecDouble = new UserSpecifier (new NameID ("float64x2_t"));

            if (specDatatype.equals (Specifier.FLOAT))
            {
                switch (nOffset)
                {
                    case 1:
                        return _mm256_shuffle_ps (_mm256_permute_ps (expr1, 122), _mm256_shuffle_ps (_mm256_permute2f128_ps (expr1, expr2, 33), expr1, 248), 35);
                    case 2:
                        return _mm256_shuffle_pd (new Typecast (CodeGeneratorUtil.specifiers (specVecDouble), expr1), new Typecast (CodeGeneratorUtil.specifiers (specVecDouble), _mm256_permute2f128_ps (expr1, expr2, 33)), 5);
                    //return _mm256_shuffle_ps (_mm256_shuffle_ps (expr1, expr1, 110), _mm256_permute_ps (_mm256_permute2f128_ps (expr2, expr1, 3), 205), 36);
                    case 3:
                        return _mm256_blend_ps (_mm256_permute_ps (_mm256_permute2f128_ps (expr2, expr1, 3), 145), _mm256_permute_ps (expr1, 127), 17);
                    case 4:
                        return _mm256_permute2f128_ps (expr1, expr2, 33);
                    case 5:
                        return _mm256_blend_ps (_mm256_permute_ps (_mm256_permute2f128_ps (expr2, expr1, 3), 185), _mm256_permute_ps (expr2, 32), 136);
                    case 6:
                        return _mm256_shuffle_pd (new Typecast (CodeGeneratorUtil.specifiers (specVecDouble), _mm256_permute2f128_pd (expr2, expr1, 3)), new Typecast (CodeGeneratorUtil.specifiers (specVecDouble), expr2), 5);
                    //return _mm256_shuffle_ps (_mm256_permute_ps (_mm256_permute2f128_ps (expr2, expr1, 3), 190), _mm256_shuffle_ps (expr2, expr2, 64), 199);
                    case 7:
                        return _mm256_blend_ps (_mm256_shuffle_ps (_mm256_permute2f128_ps (expr2, expr1, 3), expr2, 159), _mm256_shuffle_ps (expr2, expr2, 146), 238);
                    default:
                        throw new RuntimeException ("offset must be within the range 0..7");
                }
            }
            else if (specDatatype.equals (Specifier.DOUBLE))
            {
                switch (nOffset)
                {
                    case 1:
                        return _mm256_shuffle_pd (expr1, _mm256_permute2f128_pd (expr1, expr2, 33), 5);
                    case 2:
                        return _mm256_permute2f128_pd (expr1, expr2, 33);
                    case 3:
                        return _mm256_shuffle_pd (_mm256_permute2f128_pd (expr2, expr1, 3), expr2, 5);
                    default:
                        throw new RuntimeException ("offset must be within the range 0..3");
                }
            }
            else
                throw new RuntimeException (StringUtil.concat ("Unsupported datatype: ", specDatatype.toString ()));
        }
    }
*/
    /**
     * Shuffle on ARM can use the VEXT instruction
     */
    public Expression shuffle (Expression expr1, Expression expr2, Specifier specDatatype, int nOffset)
    {
        int nSIMDVectorLength = m_data.getArchitectureDescription ().getSIMDVectorLength (specDatatype);

        // float
        if(nSIMDVectorLength  == 4){
            if(nOffset == 0)
                return expr1.clone ();

            return new FunctionCall (
                    new NameID ("vextq_f32"),                   // ARMv7, ARMv8
                    CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (nOffset)));
        } else
        // double
        if(nSIMDVectorLength  == 2){
            if(nOffset == 0)
                return expr1.clone ();

            return new FunctionCall (
                    new NameID ("vextq_f64"),                   // ARMv8 (AArch64)
                    CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (nOffset)));

        } else
            throw new RuntimeException (StringUtil.concat ("shuffle has not been implemented for SIMD vector length ", nSIMDVectorLength));

        /*
                    case 1:
                        //
                        //  +---------+---------+
                        //  | 0 1 2 3 | 4 5 6 7 |
                        //  +---------+---------+
                        //    0 1 2 3   0 1 2 3
                        //
                        //     \      \   /
                        //     |       \ /
                        //     |
                        //     |      3 3 0 0    <==
                        //     |    +---------+
                        //     |    | 3 3 4 4 |
                        //     |    +---------+
                        //     |      0 1 2 3
                        //     \
                        //      +----\/
                        //
                        //         1 2 0 2   <==
                        //       +---------+
                        //       | 1 2 3 4 |
                        //       +---------+

                        return new FunctionCall (
                                new NameID ("_mm_shuffle_ps"),
                                CodeGeneratorUtil.expressions (
                                        expr1.clone (),
                                        new FunctionCall (
                                                new NameID ("_mm_shuffle_ps"),
                                                CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (),
                                                new IntegerLiteral (0x0f) // _MM_SHUFFLE (0, 0, 3, 3)
                                                )),
                                                new IntegerLiteral (0x89) // _MM_SHUFFLE (2, 0, 2, 1)
                                )
                        );

                    case 2:
                        //
                        //  +---------+---------+
                        //  | 0 1 2 3 | 4 5 6 7 |
                        //  +---------+---------+
                        //    0 1 2 3   0 1 2 3
                        //
                        //          \   /
                        //           \ /
                        //
                        //         2 3 0 1   <==
                        //       +---------+
                        //       | 2 3 4 5 |
                        //       +---------+
                        //

                        return new FunctionCall (
                                new NameID ("_mm_shuffle_ps"),
                                CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (0x4e)
                                // _MM_SHUFFLE (1, 0, 3, 2)
                                ));

                    case 3:
                        //
                        //    0 1 2 3   0 1 2 3
                        //  +---------+---------+
                        //  | 0 1 2 3 | 4 5 6 7 |
                        //  +---------+---------+
                        //
                        //       \   /       /
                        //        \ /        |
                        //                   |
                        //       3 3 0 0 <== |
                        //     +---------+   |
                        //     | 3 3 4 4 |   |
                        //     +---------+   |
                        //       0 1 2 3     |
                        //                   /
                        //             \/----+
                        //
                        //         0 2 1 2  <==
                        //       +---------+
                        //       | 3 4 5 6 |
                        //       +---------+

                        return new FunctionCall (
                                new NameID ("_mm_shuffle_ps"),
                                CodeGeneratorUtil.expressions (
                                        new FunctionCall (
                                                new NameID ("_mm_shuffle_ps"),
                                                CodeGeneratorUtil.expressions (expr1.clone (), expr2.clone (), new IntegerLiteral (0x0f) // _MM_SHUFFLE (0, 0, 3, 3)
                                        )),
                                        expr2.clone (),
                                        new IntegerLiteral (0x98) // _MM_SHUFFLE (2, 1, 2, 0)
                                )
                        );

                    default:
                        throw new RuntimeException ("offset must be within the range 0..3");
                }

            default:
                throw new RuntimeException (StringUtil.concat ("shuffle has not been implemented for SIMD vector length ", nSIMDVectorLength));
        */

    }


    @Override
    protected String getVecLoadFunctionName (Specifier specDatatype)
    {
        String strFunction = null;
        if (Specifier.FLOAT.equals (specDatatype))
            strFunction = "vmovq_n_f32";            // ARMv7, ARMv8
            //strFunction = "_mm256_set1_ps";
        else if (Specifier.DOUBLE.equals (specDatatype))
            strFunction = "vmovq_n_f64";            // ARMv8(AArch64)
            //strFunction = "_mm256_set1_pd";

        return strFunction;
    }


    @Override
    protected boolean hasVecLoadFunctionPointerArg ()
    {
        return false;
    }

}
