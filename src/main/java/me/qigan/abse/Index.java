package me.qigan.abse;

import me.qigan.abse.config.MuConfig;
import me.qigan.abse.config.PositionConfig;
import me.qigan.abse.crp.MainWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@Mod(modid = Index.MODID, version = Index.VERSION)
public class Index
{
    public static MuConfig MAIN_CFG;
    public static PositionConfig POS_CFG;

    public static final String MODID = "abse";
    public static final String VERSION = "1.6";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MainWrapper.initialise(event);
    }

    public static void relog(String username, String token, String pid) {
        Session session = new Session(username, pid, token, "mojang");
        Field sessionField = ReflectionHelper.findField(Minecraft.class, "session", "field_71449_j");
        ReflectionHelper.setPrivateValue(Field.class, sessionField, sessionField.getModifiers() & ~Modifier.FINAL, "modifiers");
        ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), session, "session", "field_71449_j");


        JOptionPane.showMessageDialog(null, "Logged as " + username, "Asolute Evil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean isSafe() {
        return Index.MAIN_CFG.getBoolVal("safe_mod");
    }
}
