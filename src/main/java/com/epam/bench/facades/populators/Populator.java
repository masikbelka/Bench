package com.epam.bench.facades.populators;

/**
 * Created by Tetiana_Antonenko1.
 */
public interface Populator<SOURCE, TARGET> {

    void populate(SOURCE source, TARGET target);
}
