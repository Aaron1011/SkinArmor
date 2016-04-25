package com.aaron1011.skinarmor.items;

import com.aaron1011.skinarmor.SkinArmor;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class ItemSkinArmor extends ItemArmor {

    public ItemSkinArmor(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
    }

    private Map<UUID, ModelPlayer> modelMap = Maps.newHashMap();
    private Map<UUID, String> resourceMap = Maps.newHashMap();

    private GameProfile getProfileFromStak(ItemStack stack) {
        GameProfile profile = null;
        if (stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound.hasKey("SkullOwner", 10)) {
                profile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
            } else if (nbttagcompound.hasKey("SkullOwner", 8)) {
                profile = TileEntitySkull.updateGameprofile(new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner")));
                nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), profile));
            }
        }
        return profile;
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        GameProfile profile = this.getProfileFromStak(stack);
        if (profile != null) {

            if (resourceMap.containsKey(profile.getId())) {
                return resourceMap.get(profile.getId());
            }

            ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();

            if (profile != null)
            {
                Minecraft minecraft = Minecraft.getMinecraft();
                Map map = minecraft.getSkinManager().loadSkinFromCache(profile);

                if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
                {
                    resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                }
                else
                {
                    UUID uuid = EntityPlayer.getUUID(profile);
                    resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
                }

                resourceMap.put(profile.getId(), resourcelocation.toString());

                return resourcelocation.toString();
            }

        }
        return null;
    }

    @Override
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        try {
            GameProfile profile = this.getProfileFromStak(itemStack);
            if (profile != null) {
                ModelPlayer modelPlayer = this.modelMap.get(profile.getId());
                if (modelPlayer != null) {
                    return modelPlayer;
                }

                NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(profile.getId());
                if (networkPlayerInfo == null) {
                    networkPlayerInfo = new NetworkPlayerInfo(profile);
                }
                networkPlayerInfo.getLocationSkin();

                RenderPlayer renderPlayer = (RenderPlayer) ((Map) SkinArmor.skinMap.get(Minecraft.getMinecraft().getRenderManager())).get(
                        networkPlayerInfo
                                .getSkinType());

                this.modelMap.put(profile.getId(), renderPlayer.getPlayerModel());
                return this.modelMap.get(profile.getId());

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
