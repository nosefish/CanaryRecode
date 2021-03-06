package net.minecraft.server;


import net.canarymod.Canary;
import net.canarymod.hook.player.EatHook;


public class ItemBucketMilk extends Item {

    public ItemBucketMilk(int i0) {
        super(i0);
        this.d(1);
        this.a(CreativeTabs.f);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        // CanaryMod: Eat
        EatHook hook = new EatHook(((EntityPlayerMP) entityplayer).getPlayer(), itemstack.getCanaryItem(), 0, 0, null);

        Canary.hooks().callHook(hook);
        if (hook.isCanceled()) {
            return itemstack;
        }
        // For those Lactose intolerant
        entityplayer.cl().a(hook.getLevelGain(), hook.getSaturationGain());
        //

        if (!entityplayer.ce.d) {
            --itemstack.a;
        }

        if (!world.I) {
            entityplayer.bB();
        }

        return itemstack.a <= 0 ? new ItemStack(Item.ax) : itemstack;
    }

    public int c_(ItemStack itemstack) {
        return 32;
    }

    public EnumAction b_(ItemStack itemstack) {
        return EnumAction.c;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.a(itemstack, this.c_(itemstack));
        return itemstack;
    }
}
