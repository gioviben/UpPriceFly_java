package org.example;

import java.util.Iterator;

public interface Aggregate {
    Iterator<Flight> createIterator();
}
