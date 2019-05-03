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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.impl.EntitySpeedTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.util.MovementInput;

public class EntitySpeedMod extends Module {
    public static EntitySpeedMod INSTANCE;

    {
        INSTANCE = this;
    }

    public EntitySpeedMod() {
        super("EntitySpeed");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
        Entity ridingEntity = ReflectionStuff.getRidingEntity(mc.player);
        if (ridingEntity != null) {
            MovementInput movementInput = mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                ridingEntity.motionX = 0.0D;
                ridingEntity.motionZ = 0.0D;
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                ridingEntity.motionX = (forward * EntitySpeedTranslator.INSTANCE.speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * EntitySpeedTranslator.INSTANCE.speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                ridingEntity.motionZ = (forward * EntitySpeedTranslator.INSTANCE.speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * EntitySpeedTranslator.INSTANCE.speed * Math.cos(Math.toRadians(yaw + 90.0F)));
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(EntitySpeedTranslator.INSTANCE.speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            EntitySpeedTranslator.INSTANCE.speed = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return EntitySpeedTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 4f, 0.1f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }

    @Override
    public boolean preRecievePacket(Packet<?> packetIn) {
        if (mc.player != null && mc.player.getRidingEntity() != null && mc.player.getRidingEntity() instanceof EntityPig) { //prevent pigs from getting pushed around while riding them (maybe)
            if (packetIn instanceof SPacketEntityTeleport && ((SPacketEntityTeleport) packetIn).getEntityId() == mc.player.getRidingEntity().getEntityId()) {
                return true;
            } else if (packetIn instanceof SPacketEntityVelocity && ((SPacketEntityVelocity) packetIn).getEntityID() == mc.player.getRidingEntity().getEntityId()) {
                return true;
            } else if (packetIn instanceof SPacketEntity && ((SPacketEntity) packetIn).getEntity(mc.player.world) == mc.player.getRidingEntity()) {
                return true;
            } else {
                return packetIn instanceof SPacketMoveVehicle;
            }
        } else {
            return false;
        }
    }
}
