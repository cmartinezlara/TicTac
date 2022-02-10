import java.math.BigInteger;
import java.util.*;

public class McdFactorizacion {

    private static BigInteger siguiente_primo(BigInteger n) {
        do {
            n = n.add(BigInteger.ONE);
        } while (!es_primo(n));
        return n;
    } 

    private static boolean es_primo(BigInteger n) {
        boolean encontro_divisores = false;
        BigInteger i = new BigInteger("2");
        BigInteger dos = new BigInteger("2");
        while (i.compareTo(n.divide(dos)) <= 0) {
            if (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                encontro_divisores = true;
                break;
            }
            i = i.add(BigInteger.ONE);
        }
        return !encontro_divisores;
    }

    private static Map<BigInteger, BigInteger> getFactores(BigInteger n) {
        Map<BigInteger, BigInteger> primos1 = new HashMap<>();

        BigInteger factor_primo = new BigInteger("2");
        do {
            if (n.mod(factor_primo).compareTo(BigInteger.ZERO) == 0) {
                if(primos1.get(factor_primo) == null) {
                    primos1.put(factor_primo, BigInteger.ZERO);
                }
                primos1.put(factor_primo, primos1.get(factor_primo).add(BigInteger.ONE));
                n = n.divide(factor_primo); 
            } else {
                factor_primo = siguiente_primo(factor_primo);
            }
        } while (n.compareTo(BigInteger.ONE) == 1);
        return primos1;
    }

    public static void main (String[] args) throws Exception {
        BigInteger n1 = new BigInteger("180");
        BigInteger n2 = new BigInteger("324");
        Map<BigInteger, BigInteger> factores1 = getFactores(n1);
        Map<BigInteger, BigInteger> factores2 = getFactores(n2);
        
        BigInteger mcd = BigInteger.ONE;
        for (BigInteger key1 : factores1.keySet()) {
            while (factores1.containsKey(key1) && factores2.containsKey(key1)) {
                mcd = mcd.multiply(key1);
                if (factores1.get(key1).compareTo(BigInteger.ONE) == 0) {
                    break;
                } else {
                    factores1.put(key1, factores1.get(key1).subtract(BigInteger.ONE));
                }
                if (factores2.get(key1).compareTo(BigInteger.ONE) == 0) {
                    break;
                } else {
                    factores2.put(key1, factores2.get(key1).subtract(BigInteger.ONE));
                }
            }
        }
        System.out.println("El MCD de " + n1 + " y " + n2 + " es: " + mcd);
    }
}
