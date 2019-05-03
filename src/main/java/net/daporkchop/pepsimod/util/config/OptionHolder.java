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

package net.daporkchop.pepsimod.util.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.daporkchop.lib.reflection.PField;
import net.daporkchop.lib.reflection.PReflection;
import net.daporkchop.pepsimod.module.option.ImplOption;
import net.daporkchop.pepsimod.module.option.Option;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author DaPorkchop_
 */
public class OptionHolder<H> {
    public final Map<String, ImplOption<H>> options;

    public OptionHolder(Class<H> clazz) {
        this.options = Arrays.stream(clazz.getDeclaredFields())
                .map(PField::of)
                .filter(f -> f.hasAnnotation(Option.class))
                .map((Function<PField<Object>, ImplOption<H>>) ImplOption::new)
                .collect(Collectors.toMap(o -> o.name, o -> o));
    }

    public void write(JsonObject object, H holder)  {
        this.options.forEach((name, option) -> {
            JsonObject child = new JsonObject();
            option.writer.accept(child, holder);
            object.add(name, child);
        });
    }

    public void read(JsonObject object, H holder)  {
        this.options.forEach((name, option) -> {
            JsonElement child = object.get(name);
            if (child != null)  {
                option.writer.accept(child.getAsJsonObject(), holder);
            }
        });
    }
}
