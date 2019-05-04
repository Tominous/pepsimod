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

package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.option.ImplOption;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.config.OptionHolder;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import net.daporkchop.pepsimod.util.event.MoveEvent;
import net.daporkchop.pepsimod.util.misc.ITickListener;
import net.daporkchop.pepsimod.util.render.Renderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * hehe this is actually a command
 * but it's a module
 * gl understanding my overly complicated class heirachy
 */
public abstract class Module extends Command implements ITickListener {
    public static boolean shouldBeEnabled(boolean in, ModuleLaunchState state) {
        if (state == ModuleLaunchState.ENABLED) {
            return true;
        } else if (state == ModuleLaunchState.DISABLED) {
            return false;
        } else {
            return in;
        }
    }

    public KeyBinding keybind;
    public ColorizedText text;
    public OptionHolder<?> options;
    public String nameFull;
    public String[] completionOptions;
    public GeneralTranslator.ModuleState state;

    public Module(String name) {
        this(false, name, -1, false);
    }

    @SuppressWarnings("unchecked")
    public Module(boolean def, String name, int keybind, boolean hide) {
        super(name.toLowerCase());
        this.nameFull = name;
        this.options = new OptionHolder(this.getClass());
        this.registerKeybind(name, keybind);
        this.state = GeneralTranslator.INSTANCE.getState(name, new GeneralTranslator.ModuleState(def, hide));
        if (this.state == null) {
            GeneralTranslator.INSTANCE.states.put(name, this.state = new GeneralTranslator.ModuleState(def, hide));
        }
    }

    /**
     * Toggles a Module
     *
     * @return the new status
     */
    public boolean toggle() {
        this.state.enabled = !this.state.enabled;
        if (this.state.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        return this.state.enabled;
    }

    /**
     * Enables or disables a Module
     *
     * @return the given argument
     */
    public boolean setEnabled(boolean enabled) {
        this.state.enabled = enabled;
        if (this.state.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        return this.state.enabled;
    }

    /**
     * Handles base initialization logic after minecraft is started
     */
    public final void doInit() {
        this.init();
        if (this.hasModeInName()) {
            this.updateName();
        } else {
            this.text = new RainbowText(this.nameFull);
        }
        CommandRegistry.registerCommand(this);

        this.completionOptions = this.options.options.keySet().stream().toArray(String[]::new);;
    }

    public boolean shouldTick() {
        return this.state.enabled;
    }

    /**
     * Called when the Module is enabled
     */
    public abstract void onEnable();

    /**
     * Called when the Module is disabled
     */
    public abstract void onDisable();

    /**
     * Called when minecraft is started
     */
    public abstract void init();

    /**
     * Called directly after a packet is recieved, before it's processed
     *
     * @return if true, the packet will be ignored by vanilla
     */
    public boolean preRecievePacket(Packet<?> packetIn) {
        return false;
    }

    /**
     * Called after a packet is recieved, after it's been processed
     */
    public void postRecievePacket(Packet<?> packetIn) {
    }

    /**
     * Called right before a packet is sent
     *
     * @return if true, the packet will not be sent
     */
    public boolean preSendPacket(Packet<?> packetIn) {
        return false;
    }

    /**
     * Called after a packet is sent
     */
    public void postSendPacket(Packet<?> packetIn) {
    }

    /**
     * Whether or not extra info should show in the name
     * e.g.
     * Criticals [Packet]
     */
    public boolean hasModeInName() {
        return false;
    }

    /**
     * Whether or not extra info should show in the name
     * e.g.
     * Criticals [Packet]
     * <p>
     * This method returned "Packet"
     */
    public String getModeForName() {
        return "";
    }

    /**
     * Updates the module's name
     * Does nothing if the module has no custom name
     */
    public void updateName() {
        if (pepsimod.isInitialized && this.hasModeInName()) {
            if (HUDTranslator.INSTANCE.rainbow) {
                this.text = new RainbowText(this.nameFull + PepsiUtils.COLOR_ESCAPE + "customa8a8a8 [" + this.getModeForName() + "]");
            } else {
                this.text = new RainbowText(this.nameFull + PepsiUtils.COLOR_ESCAPE + "7 [" + this.getModeForName() + "]");
            }
        }
    }

    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                return "." + this.name + " " + (this.completionOptions.length == 0 ? "" : this.completionOptions[0]);
            case 2:
                if (args[1].isEmpty()) {
                    return "." + this.name + " " + (this.completionOptions.length == 0 ? "" : this.completionOptions[0]);
                }
                for (String mode : this.completionOptions) {
                    if (mode.equals(args[1])) {
                        ImplOption option = this.options.options.get(args[1]);
                        if (option == null) {
                            return "";
                        } else {
                            return args[0] + " " + args[1] + " " + option.getFirstCompletion();
                        }
                    } else if (mode.startsWith(args[1])) {
                        return "." + this.name + " " + mode;
                    }
                }
                return "";
            case 3:
                if (args[2].isEmpty()) {
                    ImplOption option = this.options.options.get(args[1].trim());
                    if (option == null) {
                        return "";
                    } else {
                        return args[0] + " " + args[1] + " " + option.getFirstCompletion();
                    }
                }
                ImplOption option = this.options.options.get(args[1]);
                if (option == null) {
                    return "";
                } else {
                        for (String s : option.completions) {
                            if (s.startsWith(args[2])) {
                                return args[0] + " " + args[1] + " " + s;
                            }
                        }
                        return "";
                }
        }

        return "." + this.name;
    }

    public void execute(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                String commands = "";
                for (int i = 0; i < this.completionOptions.length; i++) {
                    commands += PepsiUtils.COLOR_ESCAPE + "o" + this.completionOptions[i] + PepsiUtils.COLOR_ESCAPE + "r" + (i + 1 == this.completionOptions.length ? "" : ", ");
                }
                clientMessage(commands);
                break;
            case 2:
                if (args[1].isEmpty()) {
                    String cmds = "";
                    for (int i = 0; i < this.completionOptions.length; i++) {
                        cmds += PepsiUtils.COLOR_ESCAPE + "o" + this.completionOptions[i] + PepsiUtils.COLOR_ESCAPE + "r" + (i + 1 == this.completionOptions.length ? "" : ", ");
                    }
                    clientMessage(cmds);
                    break;
                }
                ImplOption option = this.options.options.get(args[1]);
                if (option == null) {
                    clientMessage("Unknown option: " + PepsiUtils.COLOR_ESCAPE + "o" + args[1]);
                    break;
                } else {
                    clientMessage(args[1] + ": " + option.getValueAsString(this));
                    break;
                }
            case 3:
                if (args[2].isEmpty()) {
                    option = this.options.options.get(args[1]);
                    if (option == null) {
                        clientMessage("Unknown option: " + PepsiUtils.COLOR_ESCAPE + "o" + args[1]);
                        break;
                    } else {
                        clientMessage(args[1] + ": " + option.getValueAsString(this));
                        break;
                    }
                }
                option = this.options.options.get(args[1]);
                if (option == null) {
                    clientMessage("Unknown option: " + PepsiUtils.COLOR_ESCAPE + "o" + args[1]);
                    break;
                } else {
                    try {
                        switch (option.field.getClassType().getCanonicalName()) {
                            case "java.lang.String":
                                option.field.set(this, args[2]);
                                break;
                            case "java.lang.Boolean":
                                option.field.setBoolean(this, Boolean.parseBoolean(args[2]));
                                break;
                            case "java.lang.Byte":
                                option.field.setByte(this, Byte.parseByte(args[2]));
                                break;
                            case "java.lang.Double":
                                option.field.setDouble(this, Double.parseDouble(args[2]));
                                break;
                            case "java.lang.Float":
                                option.field.setFloat(this, Float.parseFloat(args[2]));
                                break;
                            case "java.lang.Integer":
                                option.field.setInt(this, Integer.parseInt(args[2]));
                                break;
                            case "java.lang.Short":
                                option.field.setShort(this, Short.parseShort(args[2]));
                                break;
                            default:
                                clientMessage("Unknown value type: " + PepsiUtils.COLOR_ESCAPE + "o" + option.field.getClassType().getClass().getCanonicalName() + PepsiUtils.COLOR_ESCAPE + "r. Please report to devs!");
                                return;
                        }
                        clientMessage("Set " + PepsiUtils.COLOR_ESCAPE + "o" + args[1] + PepsiUtils.COLOR_ESCAPE + "r to " + PepsiUtils.COLOR_ESCAPE + "o" + option.getValueAsString(this));
                    } catch (NumberFormatException e) {
                        clientMessage("Invalid number: " + PepsiUtils.COLOR_ESCAPE + "o" + args[2]);
                    }
                }
        }
    }

    /**
     * called every frame
     */
    public void onRender(float partialTicks) {
    }

    public void onRenderGUI(float partialTicks, int width, int height, GuiIngame gui) {
    }

    public void renderOverlay(Renderer renderer)   {
    }

    public void renderWorld(Renderer renderer)   {
    }

    @Deprecated
    public ModuleLaunchState getLaunchState() {
        return ModuleLaunchState.AUTO;
    }

    public void registerKeybind(String name, int key) {
        this.keybind = new KeyBinding(name, key == -1 ? Keyboard.KEY_NONE : key, "key.categories.pepsimod");
        ClientRegistry.registerKeyBinding(this.keybind);
    }

    public abstract ModuleCategory getCategory();

    public void onPlayerMove(MoveEvent e) {

    }

    public boolean shouldRegister() {
        return true;
    }
}