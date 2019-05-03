package net.daporkchop.pepsimod.module.option;

import net.daporkchop.lib.reflection.PField;

import java.lang.annotation.Annotation;

/**
 * @author DaPorkchop_
 */
public abstract class ImplOption {
    public final String name;
    public final String displayName;
    public final String[] completions;
    public final PField field;

    public ImplOption(PField field) {
        this.field = field;

        if (!field.hasAnnotation(Option.class)) {
            throw new IllegalArgumentException("Field does not have option annotation!");
        }
        Option o = field.getAnnotation(Option.class);
        this.name = o.value();
        this.displayName = o.displayName().isEmpty() ? this.name : o.displayName();
        this.completions = o.completions();
    }
}
