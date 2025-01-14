package Apec.Components.Gui.GuiIngame.GuiElements;

import Apec.ApecMain;
import Apec.ComponentId;
import Apec.Components.Gui.GuiIngame.GUIComponent;
import Apec.Components.Gui.GuiIngame.GUIComponentID;
import Apec.Components.Gui.GuiIngame.GUIModifier;
import Apec.DataInterpretation.DataExtractor;
import Apec.Settings.SettingID;
import Apec.Utils.ApecUtils;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class DrillFuelBar extends GUIComponent {

    public DrillFuelBar() {
        super(GUIComponentID.DRILL_FUEL);
    }

    private float fuelAmount = -1f;

    /**
     * Updates fuel values.
     * @param event
    */

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.thePlayer != null && ApecMain.Instance.settingsManager.getSettingState(SettingID.DRILL_FUEL_BAR)){
            try{
                ItemStack heldItem = mc.thePlayer.getHeldItem();
                if(heldItem != null){
                    String internalName = ApecUtils.getInternalItemName(heldItem);
                    if(internalName.contains("_DRILL")){
                        List<String> lore = ApecUtils.getItemLore(heldItem);
                        fuelAmount = -1.0f;
                        for (String s : lore){
                            if(s.contains("Fuel:")){
                                String[] values = ApecUtils.removeAllCodes(s).replace("Fuel:", "").replace(" ", "").split("/");
                                float currentFuel = ApecUtils.hypixelShortValueFormattingToFloat(values[0]);
                                float maxFuel = ApecUtils.hypixelShortValueFormattingToFloat(values[1]);

                                fuelAmount =  currentFuel / maxFuel;

                                return;
                            }
                        }
                    }
                }
            }catch(Exception e){}
        }
        fuelAmount = -1f;
    }

    @Override
    public void drawTex(DataExtractor.PlayerStats ps, DataExtractor.ScoreBoardData sd, DataExtractor.OtherData od, DataExtractor.TabStats ts, ScaledResolution sr, boolean editingMode) {
        super.drawTex(ps, sd, od, ts, sr, editingMode);
        if(ApecMain.Instance.settingsManager.getSettingState(SettingID.DRILL_FUEL_BAR)){
            if (fuelAmount > -1 || editingMode) {
                GuiIngame gui = mc.ingameGUI;

                Vector2f pos = ApecUtils.scalarMultiply(getCurrentAnchorPoint(),this.oneOverScale);
                mc.renderEngine.bindTexture(new ResourceLocation(ApecMain.modId,"gui/statBars.png"));

                GlStateManager.scale(scale,scale,1);
                gui.drawTexturedModalRect(pos.x, pos.y, 246, 0, 5, 71);
                int height = (int)(71 * (editingMode ? 1.0f : fuelAmount));

                gui.drawTexturedModalRect(pos.x, pos.y + (71 - height), 246, 142 - height, 5, height);
            }
        }
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return guiModifier.applyGlobalChanges(this,new Vector2f(g_sr.getScaledWidth() - 14,g_sr.getScaledHeight() - 124 + 20*(1 - ((GUIModifier)ApecMain.Instance.getComponent(ComponentId.GUI_MODIFIER)).getGuiComponent(GUIComponentID.INFO_BOX).getScale())));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(5 * scale, 71 * scale);
    }
}
