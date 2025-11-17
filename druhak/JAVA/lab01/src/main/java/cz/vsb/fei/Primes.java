package cz.vsb.fei;

public class Primes {
    public static void main(String[] args) {
        boolean[] primes = new boolean[100];

        for (int i = 0; i < 100; i++) {
            primes[i] = true;
        }

        for (int i = 2; i < primes.length; i++) {
            for (int j = i * i; j < primes.length; j += i) {
                primes[j] = false;
            }
        }

        for (int i = 0; i < primes.length; i++) {
            if (primes[i]) {
                System.out.print(i + " ");
            }
        }

    }
}
