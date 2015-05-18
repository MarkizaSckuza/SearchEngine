package com.searchengine.libs;

import java.math.BigInteger;

public class Combinator<T> extends CombinatoricOperator<T> {


        public Combinator(T[] elements, int r) {
                super(elements, r);
                assert r <= elements.length;
        }

        @Override
        protected BigInteger initialiseTotal(int n, int r) {
                BigInteger nFact = factorial(n);
                BigInteger rFact = factorial(r);
                BigInteger nminusrFact = factorial(n - r);
                return nFact.divide(rFact.multiply(nminusrFact));
        }


        @Override
        protected void computeNext() {
                int r = indices.length;
                int i = r - 1;
                int n = elements.length;
                while (indices[i] == n - r + i) {
                        i--;
                }
                indices[i] = indices[i] + 1;
                for (int j = i + 1; j < r; j++) {
                        indices[j] = indices[i] + j - i;
                }
        }

}
