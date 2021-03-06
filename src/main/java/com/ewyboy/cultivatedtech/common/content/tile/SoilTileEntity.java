package com.ewyboy.cultivatedtech.common.content.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class SoilTileEntity extends TileEntity {

    private int fertile;
    private int growth;

    public SoilTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public int getFertile() {
        return fertile;
    }

    public void setFertile(int fertile) {
        this.fertile = fertile;
        this.markDirty();
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
        this.markDirty();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("fertile", fertile);
        compound.putInt("growth", growth);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        fertile = compound.getInt("fertile");
        growth = compound.getInt("growth");
    }
}
