package net.daporkchop.pepsimod.module.option;

/**
 * @author DaPorkchop_
 */
public @interface Option {
    /**
     * The option's internal name, used for configuration and stuff
     */
    String value();

    /**
     * A separate display name to use in menus.
     * <p>
     * If left as default (empty string), the internal name will be used.
     */
    String displayName() default "";

    /**
     * List of strings to be used for autocompletion
     */
    String[] completions() default {};

    /**
     * The name of a static function which may be used to serialize this config option. The method must accept a single {@link com.google.gson.JsonObject} field
     * and a single field with whatever the option's type is, and return void.
     * <p>
     * If left as default (empty string), a best-effort attempt will be made to serialize the field automatically, throwing an exception (and crashing the game)
     * if impossible.
     */
    String writer() default "";

    /**
     * The name of a static function which may be used to deserialize this config option. The method must accept a single {@link com.google.gson.JsonObject}
     * field and return a single whatever the option's type is.
     * <p>
     * If left as default (empty string), a best-effort attempt will be made to deserialize the field automatically, throwing an exception (and crashing the
     * game) if impossible.
     */
    String reader() default "";
}
