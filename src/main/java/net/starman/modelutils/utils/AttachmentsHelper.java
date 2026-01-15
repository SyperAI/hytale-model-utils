package net.starman.modelutils.utils;

import com.hypixel.hytale.protocol.PlayerSkin;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import com.hypixel.hytale.server.core.cosmetics.CosmeticRegistry;
import com.hypixel.hytale.server.core.cosmetics.CosmeticType;
import com.hypixel.hytale.server.core.cosmetics.CosmeticsModule;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkinPart;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AttachmentsHelper {
    static CosmeticRegistry reg = CosmeticsModule.get().getRegistry();

    public static void addAttachment(ArrayList<ModelAttachment> attachments, PlayerSkinPart part, @Nullable String gradientId, @Nullable PlayerSkinPart.Variant variant) {
        attachments.add(
                new ModelAttachment(
                        variant != null ? variant.getModel() : part.getModel(),
                        variant != null ? variant.getGreyscaleTexture() : part.getGreyscaleTexture(),
                        part.getGradientSet(),
                        gradientId,
                        1.0
                )
        );
    }

    public static ModelAttachment[] parseSkin(PlayerSkin skin, @Nullable ArrayList<String> ignore, @Nullable String defaultGradientId) {
        ArrayList<ModelAttachment> attachments = new ArrayList<>();

        // We go through all the fields of the class PlayerSkin
        for (Field skinField : skin.getClass().getDeclaredFields()) {
            // We only need string fields so skipping all other
            if (skinField.getType() != String.class) continue;

            // Skip ignored fields
            if (ignore != null && ignore.contains(skinField.getName())) continue;

            // Collecting skin part data
            String skinPartValue;
            try {
                skinPartValue = (String) skinField.get(skin);
            } catch (IllegalAccessException ignored) {
                continue;
            }
            if (skinPartValue == null) continue;

            // Converting field name to enum
            String upperSnakeName = skinField.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
            CosmeticType cosmeticType;
            try {
                cosmeticType = CosmeticType.valueOf(upperSnakeName);
            } catch (IllegalArgumentException e) {
                String enumName = upperSnakeName + "S";
                cosmeticType = CosmeticType.valueOf(enumName);
            }

            // 0 - part id; 1 - gradient id; 2 - variant
            String[] cosmeticParts = skinPartValue.split("\\.");
            PlayerSkinPart skinPart = (PlayerSkinPart) reg.getByType(cosmeticType).get(cosmeticParts[0]);

            System.out.println(skinPart.toString());

            // Collecting gradient id from parsed cosmetic data
            String gradientId;
            if (cosmeticParts.length > 1) {
                gradientId = cosmeticParts[1];
            } else {
                gradientId = defaultGradientId;
            }

            PlayerSkinPart.Variant skinPartVariant = null;
            if (cosmeticParts.length > 2) {
                skinPartVariant = skinPart.getVariants().get(cosmeticParts[2]);
            }

            addAttachment(attachments, skinPart, gradientId, skinPartVariant);
        }

        System.out.println(attachments);
        return attachments.toArray(new ModelAttachment[0]);
    }
}
