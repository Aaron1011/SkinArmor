package com.aaron1011.skinarmor;

import com.aaron1011.skinarmor.items.ItemSkinArmor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.EnumHelperClient;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;
import java.util.Map;

@Mod(modid = "skinarmor", name = "SkinArmor", version = "1.0.0")
public class SkinArmor {

    public static ItemArmor.ArmorMaterial SKIN;

    public static ItemSkinArmor SKIN_CHESTPLATE;

    public static Field skinMap;

    static {
        try {
            skinMap = RenderManager.class.getDeclaredField("skinMap");
            skinMap.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onInit(FMLPreInitializationEvent event) {
        SKIN = EnumHelper.addArmorMaterial("SKIN", "skin", 5, new int[]{1, 3, 2, 1}, 15);

        SKIN_CHESTPLATE = (ItemSkinArmor) new ItemSkinArmor(SKIN, 5, 1).setUnlocalizedName("skinChestplate");
        GameRegistry.registerItem(SKIN_CHESTPLATE, "skin_chestplate");
    }


}
