package com.szalainevarganora.minecraftexe.entities;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

/**
 * The Dark Entity — corrupted humanoid that pursues the player once released.
 * Implemented by extending EntityMob and adding basic melee AI and a player target.
 */
public class TheDarkEntity extends EntityMob {
    public TheDarkEntity(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 10;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIAttackMelee(this, 1.2D, true));
        this.tasks.addTask(1, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }
}
