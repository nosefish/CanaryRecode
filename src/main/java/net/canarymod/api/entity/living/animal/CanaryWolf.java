package net.canarymod.api.entity.living.animal;


import net.canarymod.api.DyeColor;
import net.minecraft.server.EntityWolf;


/**
 * Wolf wrapper implementation
 * 
 * @author Jason (darkdiplomat)
 */
public class CanaryWolf extends CanaryTameable implements Wolf {

    /**
     * Constructs a new wrapper for EntityWolf
     * 
     * @param entity
     *            the EntityWolf to wrap
     */
    public CanaryWolf(EntityWolf entity) {
        super(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGrowingAge() {
        return getHandle().b();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGrowingAge(int age) {
        getHandle().a(age);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().s(color.getColorCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DyeColor getCollarColor() {
        return DyeColor.values()[getHandle().bV()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

}
