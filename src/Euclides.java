import java.io.*;
import java.math.*;
import java.util.Random;

public class Euclides {
	public static void main(String[] args) throws IOException {
		String sFichero = "C:\\Users\\aleja\\OneDrive\\Uni\\TAC\\Pruebas2.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));
	    
		int l = 1;
		while (l < 500) {
			Random r1 = new Random();
			Random r2 = new Random();
			BigInteger mayor = new BigInteger(l, r1);
			BigInteger menor = new BigInteger(l, r2);
			
			if (mayor.compareTo(menor) < 0) {
				BigInteger x = mayor;
				mayor = menor;
				menor = x;
			}
			
			long t1 = System.currentTimeMillis();
			BigInteger mcd = euclides(mayor, menor);
			long t2 = System.currentTimeMillis();
			long tiempo = t2 - t1;
			System.out.println("El MCD de " + mayor + " y " + menor + " es: " + mcd);
			System.out.println("Tiempo = " + tiempo);
			l += 1;
			
			bw.write(mayor+";"+menor+";"+tiempo+"\n");
		}
		bw.close();
	}
	
	public static BigInteger euclides (BigInteger mayor, BigInteger menor) {
		BigInteger zero = new BigInteger("0");
		if (menor.compareTo(zero) == 0) {
			return mayor;
		} else {
			return euclides(menor, mayor.mod(menor));
		}
	}

}