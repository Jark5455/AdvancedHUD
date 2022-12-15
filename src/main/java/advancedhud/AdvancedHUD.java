package advancedhud;

import advancedhud.api.HUDRegistry;
import advancedhud.client.huditems.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = AdvancedHUD.MOD_ID, name = "AdvancedHUD", version = AdvancedHUD.VERSION)
public class AdvancedHUD {

    public final static String MOD_ID = "advancedhud";
    public final static String VERSION = "1.0-SNAPSHOT";

    public final static String MC_VERSION = "1.8.9";
    public static Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new TickHandler());
        FMLCommonHandler.instance().bus().register(new KeyRegister());

        registerHUDItems();
    }

    private void registerHUDItems() {
        HUDRegistry.registerHudItem(new HudItemHotbar());
        HUDRegistry.registerHudItem(new HudItemHealth());
        HUDRegistry.registerHudItem(new HudItemAir());
        HUDRegistry.registerHudItem(new HudItemFood());
        HUDRegistry.registerHudItem(new HudItemArmor());
        HUDRegistry.registerHudItem(new HudItemBossBar());
        HUDRegistry.registerHudItem(new HudItemJumpBar());
        HUDRegistry.registerHudItem(new HudItemHealthMount());
        HUDRegistry.registerHudItem(new HudItemExperienceBar());
        HUDRegistry.registerHudItem(new HudItemTooltips());
        HUDRegistry.setInitialLoadComplete(true);
    }
}
