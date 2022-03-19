import java.math.BigInteger;
import java.lang.Thread;
import java.io.*;


/**
 * 
 */

/**
 * @author Vincent
 *
 */
public class AKS extends Thread
{

	static boolean verbose = false;
	
	BigInteger n;
	boolean n_isprime;
	BigInteger factor;
	double timeelapsed;

	/***
	 * Constructor--just save the number
	 * @param n
	 */
	public AKS(BigInteger n)
	{
		this.n = n;
	}
	
	/***
	 * Run AKS.isprime as a thread
	 */
	/*
	public void run()
	{
	  try {
		this.isPrime();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	/***
	 * Run the AKS primality test and time it
	 * 
	 * @return true if n is prime
	 * @throws IOException 
	 */
	/*
	public boolean isPrimeTimed() throws IOException
  {
    double start = System.currentTimeMillis();
    boolean rtn = isPrime();
    timeelapsed = System.currentTimeMillis() - start;
    return rtn;
  }
	
	/***
	 * Run the AKS primality test
	 * 
	 * @return true if n is prime
	 * @throws IOException 
	 */
	public boolean isPrime(BufferedWriter bw) throws IOException 
	{
		System.out.println(n);
		bw.write(n+";");
		//boolean salir = true;
		//-----PASO 1-----
		// TODO: Do this in linear time http://www.ams.org/journals/mcom/1998-67-223/S0025-5718-98-00952-1/S0025-5718-98-00952-1.pdf
		// If ( n = a^b for a in natural numbers and b > 1), output COMPOSITE
		long t1 = System.nanoTime();
		BigInteger base = BigInteger.valueOf(2); // 1
		BigInteger aSquared;// 1
		
		do
		{
			BigInteger result; // 1 

			int power = Math.max((int) (log()/log(base) - 2),1); // 2 + 13 + 8
			int comparison; // 1
			
			do
			{
				power++; // 1
				result = base.pow(power); // 2 
				comparison = n.compareTo(result); // 2 
			}
			while( comparison > 0 && power < Integer.MAX_VALUE ); //3 veces + 1 --> 4 * (2 + 5) = 28 
			
			if( comparison == 0 ) // 1 + "lo de dentro" = 1 + 5 = 6
			{
				if (verbose) System.out.println(n + " is a perfect power of " + base); // 2 
				factor = base; // 1
				n_isprime = false; // 1
				return n_isprime; // 1
			}
			
			if (verbose) System.out.println(n + " is not a perfect power of " + base); // 2

			base = base.add(BigInteger.ONE); // 2
			aSquared = base.pow(2); // 2
		}
		while (aSquared.compareTo(this.n) <= 0); // floor(raiz(n)) - 1 + 1 --> floor(raiz(n)) * (2 + 65) = floor(raiz(n))*67
		if (verbose) System.out.println(n + " is not a perfect power of any integer less than its square root"); // 2
        
		//--- Total P1: floor(raiz(n))*67 + 4; Potencial


		//-----PASO 2-----
		// Find the smallest r such that o_r(n) > log^2 n
		// o_r(n) is the multiplicative order of n modulo r
		// the multiplicative order of n modulo r is the 
		// smallest positive integer k with	n^k = 1 (mod r).
		double log = this.log(); // 1 + 13
		double logSquared = log*log; // 2 
		BigInteger k = BigInteger.ONE; // 1
		BigInteger r = BigInteger.ONE; // 2
		do
		{
			r = r.add(BigInteger.ONE); // 2
			if (verbose) System.out.println("trying r = " + r); // 1+1
			k = multiplicativeOrder(r); // 8r+8
		}
		while( k.doubleValue() < logSquared ); // r * (2 + 8r+12) = 8r^2 + 14r
		if (verbose) System.out.println("r is " + r); // 2
		
		//---- Total P2: 8r^2 + 14r +21 donde r = max(3, log(n)^5)
		//-----PASO 3-----
		// If 1 < gcd(a,n) < n for some a <= r, output COMPOSITE
		for( BigInteger i = BigInteger.valueOf(2); i.compareTo(r) <= 0; i = i.add(BigInteger.ONE) ) //5 +  (r-1)*(5 + 4)
		{
			BigInteger gcd = n.gcd(i);// 1 
			if (verbose) System.out.println("gcd(" + n + "," + i + ") = " + gcd); // 2 
			if ( gcd.compareTo(BigInteger.ONE) > 0 && gcd.compareTo(n) < 0 ) // 1  
			{
				factor = i; // 1
				n_isprime = false; // 1
				return false; // 1
			}
		}
		 // ---Total P3: (r-1)*9 + 5
		//if (salir) return true; //CORTE PARA LAS PRUEBAS
		
		//-----PASO 4-----
		// If n <= r, output PRIME
		if( n.compareTo(r) <= 0 )
		{
			n_isprime = true;
			return true;
		}

		//-----PASO 5-----
		// For i = 1 to sqrt(totient)log(n) do
		// if (X+i)^n <>�X^n + i (mod X^r - 1,n), output composite;
		long t2 = System.nanoTime();
		// sqrt(totient)log(n)
		int limit = (int) (Math.sqrt(totient(r).doubleValue()) * this.log());
		// X^r - 1
		Poly modPoly = new Poly(BigInteger.ONE, r.intValue()).minus(new Poly(BigInteger.ONE,0));
		// X^n (mod X^r - 1, n)
		Poly partialOutcome = new Poly(BigInteger.ONE, 1).modPow(n, modPoly, n);
		for( int i = 1; i <= limit; i++ )
		{
			Poly polyI = new Poly(BigInteger.valueOf(i),0);
			// X^n + i (mod X^r - 1, n)
			Poly outcome = partialOutcome.plus(polyI);
			Poly p = new Poly(BigInteger.ONE,1).plus(polyI).modPow(n, modPoly, n);
			if( !outcome.equals(p) )
			{
				if (verbose) System.out.println( "(x+" + i + ")^" + n + " mod (x^" + r + " - 1, " + n + ") = " + outcome);
				if (verbose) System.out.println( "x^" + n + " + " + i + " mod (x^" + r + " - 1, " + n + ") = " + p);
				// if (verbose) System.out.println("(x+i)^" + n + " = x^" + n + " + " + i + " (mod x^" + r + " - 1, " + n + ") failed");
				factor = BigInteger.valueOf(i);
				n_isprime = false;
				return n_isprime;
			}
			else
				if (verbose) System.out.println("(x+" + i + ")^" + n + " = x^" + n + " + " + i + " mod (x^" + r + " - 1, " + n + ") true");
		}
		long t_5 = System.nanoTime() - t2;
        bw.write(t_5+";");

		long tf = System.nanoTime() - t1;
        bw.write(tf+"\n");
		n_isprime = true;
	    return n_isprime;
	}

	
	/***
	 * Calculate the totient of a BigInteger r
	 * Based on this algorithm:
	 * 
	 * http://community.topcoder.com/tc?module=Static&d1=tutorials&d2=primeNumbers
	 * 
	 * @param r BigInteger to calculate the totient of
	 * @return phi(r)--number of integers less than r that are coprime
	 */
    BigInteger totient(BigInteger n) // 9 + (floor(raiz(n)) - 1) * (11 + 5n/2 )
    { 
    	BigInteger result = n; //1
      
    	for( BigInteger i = BigInteger.valueOf(2); n.compareTo(i.multiply(i)) > 0; i = i.add(BigInteger.ONE) ) //2 + (floor(raiz(n)) - 1) * (11 + 5n/2 )
    	{ 
    		if (n.mod(i).compareTo(BigInteger.ZERO) == 0) // 3 + 3 = 6
    			result = result.subtract(result.divide(i));// 3
    		
    		while (n.mod(i).compareTo(BigInteger.ZERO) == 0) //3 + (n/2)*(5)
    			n = n.divide(i); //2 
    	}
    	
    	if (n.compareTo(BigInteger.ONE) > 0) // 2 + 3 = 5
    		result = result.subtract(result.divide(n));// 3
    	
    	return result; // 1
    	
    } 

	/***
	 * Calculate the multiplicative order of n modulo r
	 * This is defined as the smallest positive integer k 
	 * for which n^k = 1 (mod r).
	 * 
	 * @param r modulus for mutliplicative order
	 * @return multiplicative order or -1 if none exists
	 */
	BigInteger multiplicativeOrder(BigInteger r)// 8r + 7
	{
		// TODO Consider implementing an alternative algorithm http://rosettacode.org/wiki/Multiplicative_order
		BigInteger k = BigInteger.ZERO; // 1
		BigInteger result; // 1
		
		do
		{
			k = k.add(BigInteger.ONE); // 2 
			result = this.n.modPow(k,r); // 2
			//System.out.println("k: " + k + " r: " + r);
		}
		while( result.compareTo(BigInteger.ONE) != 0 && r.compareTo(k) > 0); // r * (4 + 4)
		
		if (r.compareTo(k) <= 0) // 2 + 3
			return BigInteger.ONE.negate(); // 1
		else 
		{
			if (verbose) System.out.println(n + "^" + k + " mod " + r + " = " + result); // 1 + 1
			return k; // 1
		}
	}
	

	// Save log n here
	double logSave = -1;

	/***
	 * 
	 * @return log base 2 of n
	 */
	double log() // 13
	{
		if ( logSave != -1 ) // 1 
			return logSave;
		
		// from http://world.std.com/~reinhold/BigNumCalcSource/BigNumCalc.java
		BigInteger b; //  1
		
	    int temp = n.bitLength() - 1000; //3
	    if (temp > 0) // 1 + 6
	    {
	    	b=n.shiftRight(temp);  // 2
	        logSave = (Math.log(b.doubleValue()) + temp)*Math.log(2); // 4
	    }
	    else 
	    	logSave = (Math.log(n.doubleValue()))*Math.log(2); // 4

	    return logSave; // 1
	}

	
	/**
	 * log base 2 method that takes a parameter
	 * @param x
	 * @return
	 */
	double log(BigInteger x)// 8
	{
		// from http://world.std.com/~reinhold/BigNumCalcSource/BigNumCalc.java
		BigInteger b; // 1
		
	    int temp = x.bitLength() - 1000; // 3
	    if (temp > 0) // 1 + 5
	    {
	    	b=x.shiftRight(temp); // 2
	        return (Math.log(b.doubleValue()) + temp)*Math.log(2);  // 3
	    }
	    else 
	    	return (Math.log(x.doubleValue())*Math.log(2)); // 3
	}
	
	public BigInteger getFactor()
	{
		return factor;
	}

  public double GetElapsedTime() {
    return timeelapsed;
  }
	
}
