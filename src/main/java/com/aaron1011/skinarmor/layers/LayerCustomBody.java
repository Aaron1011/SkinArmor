package com.aaron1011.skinarmor.layers;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

import java.util.UUID;

public class LayerCustomBody implements LayerRenderer {

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getCurrentArmor(3);

        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem() == Items.skull)
        {
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();

            if (entitylivingbaseIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            boolean flag = entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityZombie && ((EntityZombie)entitylivingbaseIn).isVillager();
            float f7;
            f7 = 1.1875F;
            GlStateManager.scale(f7, -f7, -f7);

            if (flag)
            {
                GlStateManager.translate(0.0F, 0.0625F, 0.0F);
            }

            GameProfile gameprofile = null;

            if (itemstack.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound.hasKey("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8))
                {
                    gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner")));
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }

            TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

}
