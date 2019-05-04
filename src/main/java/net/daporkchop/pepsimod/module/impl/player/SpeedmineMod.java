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

package net.daporkchop.pepsimod.module.impl.player;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.option.Option;
import net.daporkchop.pepsimod.util.ReflectionStuff;

public class SpeedmineMod extends Module {
    public static SpeedmineMod INSTANCE;

    {
        INSTANCE = this;
    }

    @Option("speed")
    @Option.Float(min = 0.0f, max = 1.0f, step = 0.1f)
    public float speed = 0.4f;

    public SpeedmineMod() {
        super("Speedmine");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
        if (mc.world != null) {
            if (ReflectionStuff.getCurBlockDamageMP() < this.speed) {
                ReflectionStuff.setCurBlockDamageMP(this.speed);
            }
            if (ReflectionStuff.getBlockHitDelay() > 1) {
                ReflectionStuff.setBlockHitDelay(1);
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.PLAYER;
    }
}
