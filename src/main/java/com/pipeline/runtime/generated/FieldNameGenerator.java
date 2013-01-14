package com.pipeline.runtime.generated;

import java.util.Iterator;

/**
 * @author Konstantin Tsykulenko
 * @since 1/14/13
 */
public class FieldNameGenerator implements Iterable<String>{

    private static final String pattern = "_f%d";
    private int sequenceNumber = 1;

    @Override
    public Iterator<String> iterator() {
        return new FieldNameGeneratorIterator();
    }

    private class FieldNameGeneratorIterator implements Iterator<String> {

        @Override
        public boolean hasNext() {
            return sequenceNumber < Integer.MAX_VALUE;
        }

        @Override
        public String next() {
            sequenceNumber++;
            return String.format(pattern, sequenceNumber);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}
