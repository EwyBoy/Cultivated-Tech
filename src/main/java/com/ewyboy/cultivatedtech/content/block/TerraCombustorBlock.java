package com.ewyboy.cultivatedtech.content.block;

import com.ewyboy.cultivatedtech.client.IHasRenderType;
import com.ewyboy.cultivatedtech.content.entity.BaseBlockEntity;
import com.ewyboy.cultivatedtech.content.entity.TerraCombustorBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TerraCombustorBlock extends BaseBlockEntity<TerraCombustorBlockEntity> {

    public TerraCombustorBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.ENABLED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ENABLED);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        double x = (double) pos.getX() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;

        //if (world.getBlockState(pos).equals(BlockStateProperties.ENABLED)) {}

        for (int i = 0; i < 30; i++) {
            level.addParticle(ParticleTypes.SMOKE, x, (double) pos.getY() + 0.8D, z, 0.0D, 0.0D + ((double) i / 100), 0.0D);

            level.addParticle(ParticleTypes.FLAME, Mth.randomBetween(random, -0.25f, 0.25f) + x, pos.getY() + 0.45D, Mth.randomBetween(random, -0.25f, 0.25f) + z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, @NotNull List<Component> componentList, @NotNull TooltipFlag tooltipFlag) {
        componentList.add(Component.literal("Generates energy by combusting nearby vegetation & grass"));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof TerraCombustorBlockEntity computer) {
                computer.tick(world, pos, state, computer);
            }
        };
    }

    @Override
    protected BlockEntityType.BlockEntitySupplier<TerraCombustorBlockEntity> getTileSupplier() {
        return TerraCombustorBlockEntity::new;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return this.getTileSupplier().create(blockPos, blockState);
    }

}
