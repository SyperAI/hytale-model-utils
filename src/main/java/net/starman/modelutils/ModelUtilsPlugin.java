package net.starman.modelutils;

import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.starman.modelutils.commands.ExampleCommand;
import net.starman.modelutils.event.ExampleEvent;

import javax.annotation.Nonnull;

public final class ModelUtilsPlugin extends JavaPlugin {

    private static ModelUtilsPlugin instance;

    public ModelUtilsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static ModelUtilsPlugin get() {
        return instance;
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand());
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
    }

    private void registerCommands() {
        CommandRegistry commandRegistry = this.getCommandRegistry();

        commandRegistry.registerCommand(new ExampleCommand());
    }
}
