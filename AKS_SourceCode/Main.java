import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Random;


public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub    
    boolean cert = false;
    
    for(int i = 0; i < args.length; i++)
    {
      if(args[i].trim().equals("-c"))
        cert=true;
      if(args[i].trim().equals("-v"))
        Interface.verbose = true;
      if(args[i].trim().equals("-vs"))
      {
        Interface.verbose = true;
        MillerRabin.verbose = true;
        AKS.verbose = true;
        MillerRabinThread.verbose = true;
      }
    }
    
    try {
      //java.io.BufferedReader stdin = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
      //System.out.println("Type a new number:");
      //String line = stdin.readLine();
      String sFichero = "C:\\Users\\aleja\\OneDrive\\Uni\\TAC\\Pr�ctica 1\\PruebasPrimos.csv";
	  BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));
	  for (int j = 0; j < 50; j++) {
	      Random r = new Random();
	      int l = 2;
	      while(l <= 56/*line != null*/)
	      {
	        //Interface.verbose = false;
	    	BigInteger n = BigInteger.probablePrime(l, r);
	        Interface i = new Interface(n, cert);
	        System.out.print(l + ": ");
	        
	        // System.out.println((i.isPrime() ? "1" : "0"));
	        System.out.println((i.isPrime(bw) ? "1 - PRIME" : "0 - NOT PRIME"));
	        
	        //System.out.println("Type a new number:");
	        //line = stdin.readLine();
	        l++;
	      }
	      bw.close();
	    }
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
    
    /*
    BigInteger totest = new BigInteger("1519380").pow(32768).add(BigInteger.ONE);
    MillerRabin mr = new MillerRabin(totest,10000); 
    if(mr.isPrime())
      System.out.println(totest.toString() + " is prime");
    else
      System.out.println(totest.toString() + " is NOT prime");
    */
    

  }

}

