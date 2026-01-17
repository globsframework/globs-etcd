package org.globsframework.shared.model;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.IntegerField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class PathIndex {
    public static final GlobType TYPE;

    public static final IntegerField index;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = GlobTypeBuilderFactory.create("PathIndex");
        index = typeBuilder.declareIntegerField("index");
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> PathIndex.TYPE.instantiate()
                .set(PathIndex.index, ((PathIndex_) annotation).value()));
        TYPE = typeBuilder.build();
        KEY = KeyBuilder.newEmptyKey(TYPE);
    }

    static public Glob create(int index) {
        return TYPE.instantiate().set(PathIndex.index, index);
    }
}
