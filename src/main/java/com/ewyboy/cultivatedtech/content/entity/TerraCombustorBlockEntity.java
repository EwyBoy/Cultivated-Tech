package com.ewyboy.cultivatedtech.content.entity;

import com.ewyboy.cultivatedtech.register.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TerraCombustorBlockEntity extends BlockEntity implements BlockEntityTicker<TerraCombustorBlockEntity> {

    private CompoundTag energyTag;
    private EnergyStorage energyStorage;

    private final LazyOptional<EnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TerraCombustorBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.BLOCK_ENTITIES.TERRA_COMBUSTOR_BLOCK_ENTITY.get(), pos, state);

        this.energyTag = new CompoundTag();

        this.energyStorage = new EnergyStorage(100000) {
            private void saveEnergy() {
                if (energyTag == null) {
                    energyTag = new CompoundTag();
                }
                energyTag.putInt("energy", energyStorage.getEnergyStored());
                setChanged();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int energyReceived = super.receiveEnergy(maxReceive, simulate);
                if (!simulate) {
                    saveEnergy();
                }
                return energyReceived;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energyExtracted = super.extractEnergy(maxExtract, simulate);
                if (!simulate) {
                    saveEnergy();
                }
                return energyExtracted;
            }

            @Override
            public int getEnergyStored() {
                return super.getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return super.getMaxEnergyStored();
            }

            @Override
            public boolean canExtract() {
                return super.canExtract();
            }

            @Override
            public boolean canReceive() {
                return super.canReceive();
            }

            @Override
            public Tag serializeNBT() {
                return super.serializeNBT();
            }

            @Override
            public void deserializeNBT(Tag nbt) {
                super.deserializeNBT(nbt);
            }
        };

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull TerraCombustorBlockEntity entity) {
        Random random = new Random();
        List<BlockPos> surroundingBlockList;

        final var range  = 30;
        final var delay = 1;
        final var maxAttempts = 100;
        var attempts = 0;

        if (!level.isClientSide && level.getGameTime() % delay == 0) {

            surroundingBlockList = BlockPos.betweenClosedStream(
                    pos.offset(-range, 0, -range),
                    pos.offset(range, 0, range)
            ).map(BlockPos::immutable).collect(Collectors.toList());
            Collections.shuffle(surroundingBlockList);

            for (BlockPos targetPos : surroundingBlockList) {
                if (attempts >= maxAttempts) break;
                attempts++;
                if (level.getBlockState(targetPos).getBlock() == Blocks.GRASS_BLOCK) {
                    level.setBlock(targetPos, Blocks.DIRT.defaultBlockState(), 3);
                    level.sendBlockUpdated(pos, state, state, 3);
                    ServerLevel serverWorld = (ServerLevel) level;
                    serverWorld.playSound(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + random.nextFloat() * 0.4F);
                    energyStorage.receiveEnergy(1000, false);
                    break;
                } else if (level.getBlockState(targetPos).getBlock() instanceof IPlantable || level.getBlockState(targetPos).getBlock() instanceof BonemealableBlock) {
                    level.destroyBlock(targetPos, false);
                    level.sendBlockUpdated(pos, state, state, 3);
                    energyStorage.receiveEnergy(1000, false);
                    break;
                }
            }
        }
    }
}
