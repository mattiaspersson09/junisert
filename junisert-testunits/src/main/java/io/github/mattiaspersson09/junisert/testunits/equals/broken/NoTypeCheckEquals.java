package io.github.mattiaspersson09.junisert.testunits.equals.broken;

import java.util.Objects;

public class NoTypeCheckEquals {
    private Object field;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        return true;
    }
}
