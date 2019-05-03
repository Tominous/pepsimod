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

package net.daporkchop.pepsimod.module.impl.combat;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * #totallyNotSkidded
 */
public class AutoTotemMod extends Module {
    public static AutoTotemMod INSTANCE;
    private int timer;

    {
        INSTANCE = this;
    }

    public AutoTotemMod() {
        super("AutoTotem");
    }

    @Override
    public void onEnable() {
        this.timer = 0;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        EntityPlayerSP player = mc.player;

        if (this.timer > 0) {
            this.timer--;
            return;
        }

        NonNullList<ItemStack> inv;
        ItemStack offhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        int inventoryIndex;

        inv = player.inventory.mainInventory;

        if ((offhand == null) || (offhand.getItem() != Items.TOTEM_OF_UNDYING)) {
            for (inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
                if (inv.get(inventoryIndex) != ItemStack.EMPTY) {
                    if (inv.get(inventoryIndex).getItem() == Items.TOTEM_OF_UNDYING) {
                        this.replaceTotem(inventoryIndex);
                        break;
                    }
                }
            }
            this.timer = 3;
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }

    public void replaceTotem(int inventoryIndex) {
        if (mc.player.openContainer instanceof ContainerPlayer) {
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
        }
    }
}
