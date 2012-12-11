package com.github.sandrasi.geocoder.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IteratorsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void emptyIteratorShouldNotHaveNextElement() {
        thrown.expect(NoSuchElementException.class);
        assertFalse(Iterators.empty().hasNext());
        Iterators.empty().next();
    }

    @Test
    public void emptyIteratorShouldNotSupportRemoval() {
        thrown.expect(IllegalStateException.class);
        Iterators.empty().remove();
    }

    @Test
    public void unmodifiableIteratorShouldHasNextWhenWrappedIteratorHasNext() {
        Iterator<String> subject = Iterators.unmodifiable(Arrays.asList("test").iterator());
        assertTrue(subject.hasNext());
        assertThat(subject.next(), is("test"));
    }

    @Test
    public void unmodifiableIteratorShouldNotSupportRemoval() {
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("UnmodifiableIterator.remove()");
        Iterator<String> subject = Iterators.unmodifiable(Arrays.asList("test").iterator());
        subject.remove();
    }
}
