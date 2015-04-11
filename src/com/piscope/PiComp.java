package com.piscope;

public class PiComp {
	
	/* 
	 * Computes the discrete Fourier transform (DFT) of the given complex vector, storing the result back into the vector.
	 * The vector can have any length. This is a wrapper function.
	 */
	public static void transform(double[] real, double[] imag) {
		if (real.length != imag.length)
			throw new IllegalArgumentException("Mismatched lengths");
		
		int n = real.length;
		if (n == 0)
			return;
		else if ((n & (n - 1)) == 0)  // Is power of 2
			transformRadix2(real, imag);
		else  // More complicated algorithm for arbitrary sizes
			transformBluestein(real, imag);
	}
	
	/* 
	 * Computes the inverse discrete Fourier transform (IDFT) of the given complex vector, storing the result back into the vector.
	 * The vector can have any length. This is a wrapper function. This transform does not perform scaling, so the inverse is not a true inverse.
	 */
	public static void inverseTransform(double[] real, double[] imag) {
		transform(imag, real);
	}

}
