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
	

	/* 
	 * Computes the discrete Fourier transform (DFT) of the given complex vector, storing the result back into the vector.
	 * The vector's length must be a power of 2. Uses the Cooley-Tukey decimation-in-time radix-2 algorithm.
	 */
	public static void transformRadix2(double[] real, double[] imag) {
		// Initialization
		if (real.length != imag.length)
			throw new IllegalArgumentException("Mismatched lengths");
		int n = real.length;
		int levels = 31 - Integer.numberOfLeadingZeros(n);  // Equal to floor(log2(n))
		if (1 << levels != n)
			throw new IllegalArgumentException("Length is not a power of 2");
		double[] cosTable = new double[n / 2];
		double[] sinTable = new double[n / 2];
		for (int i = 0; i < n / 2; i++) {
			cosTable[i] = Math.cos(2 * Math.PI * i / n);
			sinTable[i] = Math.sin(2 * Math.PI * i / n);
		}
		
		// Bit-reversed addressing permutation
		for (int i = 0; i < n; i++) {
			int j = Integer.reverse(i) >>> (32 - levels);
			if (j > i) {
				double temp = real[i];
				real[i] = real[j];
				real[j] = temp;
				temp = imag[i];
				imag[i] = imag[j];
				imag[j] = temp;
			}
		}
		
		// Cooley-Tukey decimation-in-time radix-2 FFT
		for (int size = 2; size <= n; size *= 2) {
			int halfsize = size / 2;
			int tablestep = n / size;
			for (int i = 0; i < n; i += size) {
				for (int j = i, k = 0; j < i + halfsize; j++, k += tablestep) {
					double tpre =  real[j+halfsize] * cosTable[k] + imag[j+halfsize] * sinTable[k];
					double tpim = -real[j+halfsize] * sinTable[k] + imag[j+halfsize] * cosTable[k];
					real[j + halfsize] = real[j] - tpre;
					imag[j + halfsize] = imag[j] - tpim;
					real[j] += tpre;
					imag[j] += tpim;
				}
			}
			if (size == n)  // Prevent overflow in 'size *= 2'
				break;
		}
	}

}
