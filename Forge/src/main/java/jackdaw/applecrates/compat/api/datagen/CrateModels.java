package jackdaw.applecrates.compat.api.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.compat.api.AppleCrateAPI;
import jackdaw.applecrates.compat.api.exception.WoodException;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateModels extends BlockModelProvider {
    public static final String MINDIR = "$minecraft:$";
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public CrateModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> {
            try {
                ResourceLocation existingTexture = AppleCrateAPI.getPathFromWood().get(woodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(woodType);
                existingFileHelper.trackGenerated(existingTexture, TEXTURE); //trick datagen into thinking that the file is definitly present
                withExistingParent(block.get().getRegistryName().getPath(),
                        modLoc("block/applecrate")).texture("particle", existingTexture).texture("0", existingTexture);
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
