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

import com.google.gson.JsonObject;
import net.daporkchop.lib.common.function.io.IOBiConsumer;
import net.daporkchop.lib.common.function.throwing.EBiConsumer;
import net.daporkchop.lib.encoding.basen.Base58;
import net.daporkchop.lib.reflection.PField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/**
 * @param <H> the type of class which holds the option value
 * @author DaPorkchop_
 */
public class ImplOption<H> {
    public final String name;
    public final String displayName;
    public final String[] completions;
    public final String[] comment;
    public final PField field;
    public final BiConsumer<JsonObject, H> writer;
    public final BiConsumer<JsonObject, H> reader;

    public final Number min;
    public final Number max;
    public final Number step;

    @SuppressWarnings("unchecked")
    public ImplOption(PField field) {
        this.field = field;

        if (!field.hasAnnotation(Option.class)) {
            throw new IllegalArgumentException("Field does not have option annotation!");
        }
        Option o = field.getAnnotation(Option.class);
        this.name = o.value();
        this.displayName = o.displayName().isEmpty() ? this.name : o.displayName();
        this.completions = o.completions();
        this.comment = o.comment();

        if (o.writer().isEmpty()) {
            //god damn this is ugly
            if (field.getClassType() == boolean.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getBoolean(holder));
            } else if (field.getClassType() == byte.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getByte(holder));
            } else if (field.getClassType() == short.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getShort(holder));
            } else if (field.getClassType() == int.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getInt(holder));
            } else if (field.getClassType() == long.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getLong(holder));
            } else if (field.getClassType() == float.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getFloat(holder));
            } else if (field.getClassType() == double.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getDouble(holder));
            } else if (field.getClassType() == String.class) {
                this.writer = (object, holder) -> object.addProperty("value", (String) field.get(holder));
            } else if (field.getClassType() == char.class) {
                this.writer = (object, holder) -> object.addProperty("value", field.getChar(holder));
            } else if (field.getClassType().isArray() || Serializable.class.isAssignableFrom(field.getClassType())) {
                this.writer = (IOBiConsumer<JsonObject, H>) (object, holder) -> {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                        out.writeObject(field.get(holder));
                    }
                    object.addProperty("value", Base58.encodeBase58(baos.toByteArray()));
                };
            } else {
                throw new IllegalStateException(String.format("Unable to serialize type: %s", field.getClassType()));
            }
        } else {
            Method method;
            try {
                method = field.getParentClass().getMethod(o.writer(), JsonObject.class, field.getClassType());
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            this.writer = (EBiConsumer<JsonObject, H>) (object, holder) -> method.invoke(null, object, holder);
        }
        if (o.reader().isEmpty()) {
            if (field.getClassType() == boolean.class) {
                this.reader = (object, holder) -> field.setBoolean(holder, object.get("value").getAsBoolean());
            } else if (field.getClassType() == byte.class) {
                this.reader = (object, holder) -> field.setByte(holder, object.get("value").getAsByte());
            } else if (field.getClassType() == short.class) {
                this.reader = (object, holder) -> field.setShort(holder, object.get("value").getAsShort());
            } else if (field.getClassType() == int.class) {
                this.reader = (object, holder) -> field.setInt(holder, object.get("value").getAsInt());
            } else if (field.getClassType() == long.class) {
                this.reader = (object, holder) -> field.setLong(holder, object.get("value").getAsLong());
            } else if (field.getClassType() == float.class) {
                this.reader = (object, holder) -> field.setFloat(holder, object.get("value").getAsFloat());
            } else if (field.getClassType() == double.class) {
                this.reader = (object, holder) -> field.setDouble(holder, object.get("value").getAsDouble());
            } else if (field.getClassType() == String.class) {
                this.reader = (object, holder) -> field.set(holder, object.get("value").getAsString());
            } else if (field.getClassType() == char.class) {
                this.reader = (object, holder) -> field.setChar(holder, object.get("value").getAsCharacter());
            } else if (field.getClassType().isArray() || Serializable.class.isAssignableFrom(field.getClassType())) {
                this.reader = (IOBiConsumer<JsonObject, H>) (object, holder) -> {
                    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base58.decodeBase58(object.get("value").getAsString())))) {
                        field.set(holder, in.readObject());
                    } catch (ClassNotFoundException e) {
                        throw new IOException(e);
                    }
                };
            } else {
                throw new IllegalStateException(String.format("Unable to deserialize type: %s", field.getClassType()));
            }
        } else {
            Method method;
            try {
                method = field.getParentClass().getMethod(o.reader(), JsonObject.class, field.getClassType());
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            this.reader = (EBiConsumer<JsonObject, H>) (object, holder) -> method.invoke(null, object, holder);
        }

        Option.Int i = field.getAnnotation(Option.Int.class);
        Option.Float f = field.getAnnotation(Option.Float.class);
        if (i != null && f != null) {
            throw new IllegalStateException("cannot be both a float and an int!");
        } else if (i != null)   {
            this.min = i.min();
            this.max = i.max();
            this.step = i.step();
        } else if (f != null)   {
            this.min = f.min();
            this.max = f.max();
            this.step = f.step();
        } else {
            this.min = this.max = this.step = null;
        }
    }

    public String getFirstCompletion()  {
        return this.completions.length == 0 ? "" : this.completions[0];
    }

    @SuppressWarnings("unchecked")
    public String getValueAsString(Object holder)   {
        JsonObject object = new JsonObject();
        this.writer.accept(object, (H) holder);
        return object.has("value") ? object.get("value").getAsString() : "unknown";
    }
}
