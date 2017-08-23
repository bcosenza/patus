#ifndef __NEON_MATH_LIB_H__
#define __NEON_MATH_LIB_H__

#include "arm_neon.h"


#ifdef GCC
#define ALIGN(A) __attribute__ ((aligned (A))
#else
#define ALIGN(A)
#endif



static uint32x4_t ZERO_F32x4 = {0, 0, 0, 0};





#define clamp0(x) ((x) > 0) ? 0 : (x)
#define clamp0q_f32(x) vandq_u32(x, vcltq_f32(x, ZERO_F32x4))
/*
#define clamp0_ps(x) _mm_and_ps (x, _mm_cmp_ps (x, ZERO_PS, 1))
#define clamp0_pd(x) _mm_and_pd (x, _mm_cmp_pd (x, ZERO_PD, 1))
*/

#define clamp0if(value, threshold) ((threshold) > 0) ? 0 : (value)
#define clamp0ifq_f32(value, threshold) vandq_u32(x,vcltq_f32(threshold, ZERO_F32x4))
/*
#define clamp0if_ps(value, threshold) _mm_and_ps (value, _mm_cmp_ps (threshold, ZERO_PS, 1))
#define clamp0if_pd(value, threshold) _mm_and_pd (value, _mm_cmp_pd (threshold, ZERO_PD, 1))
*/

/* Porting notes

 1) compare less-than
	uint32x4_t vcltq_f32(float32x4_t a, float32x4_t b);

 2) bitwise and
	inline __m128 _mm_and_ps(__m128& a, __m128& b){
		return reinterpret_cast<__m128>(vandq_u32(reinterpret_cast<uint32x4_t>(a),reinterpret_cast<uint32x4_t>(b)));
	}

 */

#endif
