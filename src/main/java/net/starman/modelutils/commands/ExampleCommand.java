package net.starman.modelutils.commands;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.PlayerSkin;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.starman.modelutils.utils.ModelHelper;

import javax.annotation.Nonnull;

public class ExampleCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> modelArg;

    public ExampleCommand() {
        super("test", "Super test command!");

        modelArg = withRequiredArg("model", "Model", ArgTypes.STRING);
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;  // Anyone can use this command
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset(modelArg.get(commandContext));
        if (modelAsset == null) {
            commandContext.sender().sendMessage(Message.raw("Error: Model not found"));
            return;
        }

        PlayerSkinComponent playerSkinComponent = store.getComponent(ref, PlayerSkinComponent.getComponentType());

        ModelHelper.applySkin(Model.createUnitScaleModel(modelAsset), playerSkinComponent.getPlayerSkin(), ref);
    }

}
