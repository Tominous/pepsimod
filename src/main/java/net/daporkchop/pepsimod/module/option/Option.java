/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
     * The name of a static function which may be used to serialize this config option. The method must accept both a {@link com.google.gson.JsonObject}
     * and an instance of the type which defines this option, and return void.
     * <p>
     * In other words, a {@code BiConsumer<JsonObject, Holder>}.
     * <p>
     * If left as default (empty string), a best-effort attempt will be made to serialize the field automatically, throwing an exception (and crashing the game)
     * if impossible.
     */
    String writer() default "";

    /**
     * The name of a static function which may be used to deserialize this config option. The method must accept both a {@link com.google.gson.JsonObject}
     * and an instance of the type which defines this option, and return void.
     * <p>
     * In other words, a {@code BiConsumer<JsonObject, Holder>}.
     * <p>
     * If left as default (empty string), a best-effort attempt will be made to deserialize the field automatically, throwing an exception (and crashing the
     * game) if impossible.
     */
    String reader() default "";

    /**
     * An additional comment for this config option. This will be displayed e.g. in the ClickGUI when
     * hovering over the option.
     * <p>
     * Each array value represents a new line, newline characters will be ignored.
     */
    String[] comment() default {};

    /**
     * Whether or not this option will be displayed in the ClickGUI.
     */
    boolean displayInClickGUI() default true;

    @interface Float    {
        float min();

        float max();

        float step();
    }

    @interface Int    {
        int min();

        int max();

        int step();
    }
}
