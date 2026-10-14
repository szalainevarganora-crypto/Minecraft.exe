package com.szalainevarganora.minecraftexe.story;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class StorySavedData extends WorldSavedData {
    private static final String NAME = "minecraftexe_story";

    public boolean active = false;
    public int chapter = 0;
    public long startTime = 0L;
    public long noelActivateTime = 0L;
    public boolean superTNTPlaced = false;
    public double gateX = 0.0D;
    public double gateY = 0.0D;
    public double gateZ = 0.0D;

    public StorySavedData() {
        super(NAME);
    }

    public StorySavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.active = nbt.getBoolean("active");
        this.chapter = nbt.getInteger("chapter");
        this.startTime = nbt.getLong("startTime");
        this.noelActivateTime = nbt.getLong("noelActivateTime");
        this.superTNTPlaced = nbt.getBoolean("superTNTPlaced");
        this.gateX = nbt.getDouble("gateX");
        this.gateY = nbt.getDouble("gateY");
        this.gateZ = nbt.getDouble("gateZ");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("active", this.active);
        compound.setInteger("chapter", this.chapter);
        compound.setLong("startTime", this.startTime);
        compound.setLong("noelActivateTime", this.noelActivateTime);
        compound.setBoolean("superTNTPlaced", this.superTNTPlaced);
        compound.setDouble("gateX", this.gateX);
        compound.setDouble("gateY", this.gateY);
        compound.setDouble("gateZ", this.gateZ);
        return compound;
    }

    public static StorySavedData get(World world) {
        StorySavedData data = (StorySavedData) world.getMapStorage().getOrLoadData(StorySavedData.class, NAME);
        if (data == null) {
            data = new StorySavedData();
            world.getMapStorage().setData(NAME, data);
        }
        return data;
    }
}
