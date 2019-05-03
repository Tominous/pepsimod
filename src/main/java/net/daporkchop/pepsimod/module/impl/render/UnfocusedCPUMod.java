/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.config.impl.CpuLimitTranslator;

public class UnfocusedCPUMod extends Module {
    public static UnfocusedCPUMod INSTANCE;

    {
        INSTANCE = this;
    }

    public UnfocusedCPUMod() {
        super("UnfocusedCPU");
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        INSTANCE = this;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(CpuLimitTranslator.INSTANCE.limit, "limit", OptionCompletions.INTEGER,
                        (value) -> {
                            CpuLimitTranslator.INSTANCE.limit = Math.max(1, value);
                            return true;
                        },
                        () -> {
                            return CpuLimitTranslator.INSTANCE.limit;
                        }, "Limit", new ExtensionSlider(ExtensionType.VALUE_INT, 1, 60, 1))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return String.valueOf(CpuLimitTranslator.INSTANCE.limit);

    }
}
