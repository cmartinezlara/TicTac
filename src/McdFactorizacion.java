import java.math.BigInteger;
import java.util.*;

public class McdFactorizacion {

    static Set<BigInteger> primos = initPrimos();
    static BigInteger mayorPrimo = new BigInteger("97");

    private static Set<BigInteger> initPrimos() {
        Set<BigInteger> primos = new HashSet<>();
        primos.add(new BigInteger("2"));
        primos.add(new BigInteger("3"));
        primos.add(new BigInteger("5"));
        primos.add(new BigInteger("7"));
        primos.add(new BigInteger("11"));
        primos.add(new BigInteger("13"));
        primos.add(new BigInteger("17"));
        primos.add(new BigInteger("19"));
        primos.add(new BigInteger("23"));
        primos.add(new BigInteger("29"));
        primos.add(new BigInteger("31"));
        primos.add(new BigInteger("37"));
        primos.add(new BigInteger("41"));
        primos.add(new BigInteger("43"));
        primos.add(new BigInteger("47"));
        primos.add(new BigInteger("53"));
        primos.add(new BigInteger("59"));
        primos.add(new BigInteger("61"));
        primos.add(new BigInteger("67"));
        primos.add(new BigInteger("71"));
        primos.add(new BigInteger("73"));
        primos.add(new BigInteger("79"));
        primos.add(new BigInteger("83"));
        primos.add(new BigInteger("89"));
        primos.add(new BigInteger("97"));
        return primos;
    }

    private static BigInteger siguiente_primo(BigInteger n) { // o(n2)
        do {
            n = n.add(BigInteger.ONE);
        } while (!es_primo(n));

        if (n.compareTo(mayorPrimo) > 0) {
            primos.add(n);
            mayorPrimo = n;
        }
        return n;
    }

    private static boolean es_primo(BigInteger n) { //o(n)?   a = o(n)
        if (n.compareTo(mayorPrimo) <= 0) {
            return primos.contains(n);
        }
        boolean encontro_divisores = false;
        BigInteger i = new BigInteger("2");
        while (i.compareTo(n.sqrt()) <= 0) {
            if (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                encontro_divisores = true;
                break;
            }
            i = siguiente_primo(i);
        }
        return !encontro_divisores;
    }

    private static Map<BigInteger, BigInteger> getFactores(BigInteger n) { //o(n3)
        Map<BigInteger, BigInteger> primos1 = new HashMap<>();

        BigInteger factor_primo = new BigInteger("2");
        do {
            if (n.mod(factor_primo).compareTo(BigInteger.ZERO) == 0) {
                primos1.putIfAbsent(factor_primo, BigInteger.ZERO);
                primos1.put(factor_primo, primos1.get(factor_primo).add(BigInteger.ONE));
                n = n.divide(factor_primo);
            } else {
                factor_primo = siguiente_primo(factor_primo);
            }
        } while (n.compareTo(BigInteger.ONE) > 0);
        return primos1;
    }

    public static void main(String[] args) { //o(n3)
        long t1 = System.nanoTime();

        BigInteger n1 = new BigInteger("78580140");
        BigInteger n2 = new BigInteger("22182054");
        Map<BigInteger, BigInteger> factores1 = getFactores(n1); //o(n3)
        Map<BigInteger, BigInteger> factores2 = getFactores(n2); //o(n3)

        BigInteger mcd = BigInteger.ONE;
        for (BigInteger key1 : factores1.keySet()) {//o(n2)
            while (factores1.containsKey(key1) && factores2.containsKey(key1)) {//o(n)
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
        System.out.println(System.nanoTime() - t1);
        System.out.println("El MCD de " + n1 + " y " + n2 + " es: " + mcd);
    }

}
