package dev.fernando.dscatalog.projections;

import dev.fernando.dscatalog.util.HasId;

public interface ProductProjection extends HasId<Long> {
    String getName();
}
