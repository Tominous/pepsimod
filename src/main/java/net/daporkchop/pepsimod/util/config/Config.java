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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Tuple;

import java.util.HashMap;
import java.util.Map;

public class Config {
    protected static final Map<String, Tuple<OptionHolder<Object>, Object>> TRANSLATORS = new HashMap<>();
    protected static final int VERSION = 1;

    @SuppressWarnings("unchecked")
    public static void register(String name, OptionHolder holder, Object value) {
        if (holder == null || value == null)    {
            throw new NullPointerException();
        }
        TRANSLATORS.putIfAbsent(name, new Tuple<OptionHolder<Object>, Object>(holder, value));
    }

    public static void loadConfig(String configJson) {
        System.out.println("Loading config!");
        System.out.println(configJson);

        JsonObject object;
        try {
            object = new JsonParser().parse(configJson).getAsJsonObject();
        } catch (IllegalStateException e) {
            //Thrown when the config is an empty string
            System.out.println("Using default config because the file is empty or unreadable");
            return;
        }
        if (!object.has("version") || object.get("version").getAsInt() != VERSION) {
            return;
        }
        TRANSLATORS.forEach((name, tuple) -> {
            if (object.has(name)) {
                tuple.getFirst().read(object.get(name).getAsJsonObject(), tuple.getSecond());
            }
        });
    }

    public static String saveConfig() {
        JsonObject object = new JsonObject();

        TRANSLATORS.forEach((name, tuple) -> {
            JsonObject child = new JsonObject();
            tuple.getFirst().write(child, tuple.getSecond());
            object.add(name, child);
        });

        object.addProperty("version", VERSION);
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }
}
