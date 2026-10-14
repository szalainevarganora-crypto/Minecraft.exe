package com.szalainevarganora.minecraftexe.entities;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;

/**
 * The Lord entity — a humanoid antagonist that blocks the player in early scenes.
 * Implemented as a lightly customized zombie to reuse the humanoid model.
 */
public class TheLordEntity extends EntityZombie {
    public TheLordEntity(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = false;
        this.setHealth(40.0F);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        // Make The Lord harder to damage during scripted scenes (but not invulnerable)
        if (!this.world.isRemote && amount > 0.0F) {
            amount = Math.min(amount, 4.0F);
        }
        return super.attackEntityFrom(source, amount);
    }
}
