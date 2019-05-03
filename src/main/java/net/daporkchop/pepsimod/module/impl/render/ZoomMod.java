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
import net.daporkchop.pepsimod.module.api.ModuleLaunchState;

public class ZoomMod extends Module {
    public static ZoomMod INSTANCE;
    public float fov = -1f;

    {
        INSTANCE = this;
    }

    public ZoomMod(int key) {
        super(false, "Zoom", key, true);
    }

    @Override
    public void onEnable() {
        if (this.fov == -1f || mc.gameSettings.fovSetting == this.fov) {
            this.fov = mc.gameSettings.fovSetting;
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (this.state.enabled) {
            if (mc.gameSettings.fovSetting > 12f) {
                for (int i = 0; i < 100; i++) {
                    if (mc.gameSettings.fovSetting > 12f) {
                        mc.gameSettings.fovSetting -= 0.1f;
                    }
                }
            }
        } else if (mc.gameSettings.fovSetting < this.fov) {
            for (int i = 0; i < 100; i++) {
                mc.gameSettings.fovSetting += 0.1F;
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }

    @Override
    public ModuleLaunchState getLaunchState() {
        return ModuleLaunchState.DISABLED;
    }
}
