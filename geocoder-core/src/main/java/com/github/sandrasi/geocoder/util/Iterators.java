package com.github.sandrasi.geocoder.util;

import java.util.Collections;

public class Iterators {

    /**
     * Returns an iterator that has no elements.
     *
     * @param <T> the type of elements returned by this iterator
     * @return the iterator returned by {@link Collections#emptyIterator()}
     */
    public static <T> java.util.Iterator<T> empty() {
        return Collections.emptyIterator();
    }

    /**
     * Wraps an iterator so that elements can not be removed from the underlying collection by this iterator.
     *
     * @param iterator the iterator which iterates on a collection
     * @param <T> the type of elements returned by this iterator
     * @return a new {@link UnmodifiableIterator}
     */
    public static <T> java.util.Iterator<T> unmodifiable(java.util.Iterator<T> iterator) {
        return new UnmodifiableIterator<>(iterator);
    }

    /**
     * An iterator that does not allow element removal from the underlying collection.
     *
     * @param <T> the type of elements returned by this iterator
     */
    public static final class UnmodifiableIterator<T> implements java.util.Iterator<T> {

        private final java.util.Iterator<T> iterator;

        private UnmodifiableIterator(java.util.Iterator<T> iterator) {
            this.iterator = iterator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next() {
            return iterator.next();
        }

        /**
         * Always throws an {@code UnsupportedOperationException}
         *
         * @throws UnsupportedOperationException
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(UnmodifiableIterator.class.getName() + ".remove()");
        }
    }
}
