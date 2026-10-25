package dev.arakiel.twilightdelight.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CookingPotBlock;

public final class FieryCookingPotBlock extends CookingPotBlock {
    public static final MapCodec<FieryCookingPotBlock> CODEC =
            simpleCodec(FieryCookingPotBlock::new);

    public FieryCookingPotBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public ItemStack getCloneItemStack(
            LevelReader level,
            BlockPos pos,
            BlockState state
    ) {
        ItemStack source = super.getCloneItemStack(level, pos, state);
        ItemStack result = new ItemStack(this);
        result.applyComponents(source.getComponentsPatch());
        return result;
    }
}
