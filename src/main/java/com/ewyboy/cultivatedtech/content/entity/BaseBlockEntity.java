package com.ewyboy.cultivatedtech.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity <T extends BlockEntity> extends Block implements EntityBlock {

    public BaseBlockEntity(Properties properties) {
        super(properties);
    }

    protected abstract BlockEntityType.BlockEntitySupplier<T> getTileSupplier();

    protected T getBlockEntity(Level world, BlockPos pos) {
        return (T) world.getBlockEntity(pos);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> entityType, BlockEntityTicker<? super E> blockEntityTicker) {
        return entityType == blockEntityType ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return getTileSupplier().create(blockPos, blockState);
    }

}
