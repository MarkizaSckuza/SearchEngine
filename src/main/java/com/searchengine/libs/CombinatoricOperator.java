package com.searchengine.libs;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Iterator;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;


public abstract class CombinatoricOperator<T> implements Iterator<T[]>,
        Iterable<T[]> {

        protected CombinatoricOperator(T[] elements, int r) {
                assert 0 <= r;
                indices = new int[r];
                this.elements = elements.clone();
                total = initialiseTotal(elements.length, r);
                reset();
        }

        protected T[] elements;


        protected int[] indices;


        protected void initialiseIndices() {
                for (int i = 0; i < indices.length; i++) {
                        indices[i] = i;
                }
        }


        private BigInteger numLeft;

        private BigInteger total;


        protected abstract BigInteger initialiseTotal(int n, int r);


        public void reset() {
                initialiseIndices();
                numLeft = total;
        }


        public BigInteger getNumLeft() {
                return numLeft;
        }


        public BigInteger getTotal() {
                return total;
        }


        public boolean hasNext() {
                return numLeft.compareTo(ZERO) == 1;
        }


        public T[] next() {
                if (!numLeft.equals(total)) {
                        computeNext();
                }
                numLeft = numLeft.subtract(ONE);
                return getResult(indices);
        }


        protected abstract void computeNext();


        @SuppressWarnings("unchecked")
        private T[] getResult(int[] indexes) {
                T[] result = (T[]) Array.newInstance(elements.getClass()
                        .getComponentType(), indexes.length);
                for (int i = 0; i < result.length; i++) {
                        result[i] = elements[indexes[i]];
                }
                return result;
        }

        public void remove() {
                throw new UnsupportedOperationException();
        }


        public Iterator<T[]> iterator() {
                return this;
        }


        public static BigInteger factorial(int n) {
                BigInteger fact = ONE;
                for (int i = n; i > 1; i--) {
                        fact = fact.multiply(BigInteger.valueOf(i));
                }
                return fact;
        }
}
