package com.szalainevarganora.minecraftexe.story;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Persisted story state per-world.
 */
public class StoryProgress extends WorldSavedData {
    public static final String DATA_NAME = "minecraftexe_story";

    public boolean started = false;
    public int chapter = 0;
    public boolean theLordSpawned = false;
    public boolean theDarkSpawned = false;
    public double storyX = 0;
    public double storyY = 0;
    public double storyZ = 0;

    public StoryProgress() {
        super(DATA_NAME);
    }

    public StoryProgress(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.started = nbt.getBoolean("started");
        this.chapter = nbt.getInteger("chapter");
        this.theLordSpawned = nbt.getBoolean("theLordSpawned");
        this.theDarkSpawned = nbt.getBoolean("theDarkSpawned");
        this.storyX = nbt.getDouble("storyX");
        this.storyY = nbt.getDouble("storyY");
        this.storyZ = nbt.getDouble("storyZ");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("started", this.started);
        compound.setInteger("chapter", this.chapter);
        compound.setBoolean("theLordSpawned", this.theLordSpawned);
        compound.setBoolean("theDarkSpawned", this.theDarkSpawned);
        compound.setDouble("storyX", this.storyX);
        compound.setDouble("storyY", this.storyY);
        compound.setDouble("storyZ", this.storyZ);
        return compound;
    }
}
