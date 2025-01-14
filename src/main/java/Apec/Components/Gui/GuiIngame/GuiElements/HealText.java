package Apec.Components.Gui.GuiIngame.GuiElements;

import Apec.ApecMain;
import Apec.Utils.ApecUtils;
import Apec.Components.Gui.GuiIngame.GUIComponentID;
import Apec.Components.Gui.GuiIngame.TextComponent;
import Apec.DataInterpretation.DataExtractor;
import Apec.Settings.SettingID;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.util.vector.Vector2f;

public class HealText extends TextComponent {

    public HealText () {
        super(GUIComponentID.HEAL_TEXT);
    }

    private int stringWidth = 0;

    @Override
    public void draw(DataExtractor.PlayerStats ps, DataExtractor.ScoreBoardData sd,DataExtractor.OtherData od, DataExtractor.TabStats ts, ScaledResolution sr,boolean editingMode) {
        super.draw(ps,sd,od,ts,sr,editingMode);
        GlStateManager.pushMatrix();
        if (ApecMain.Instance.settingsManager.getSettingState(SettingID.HEAL_TEXT)) {
            GlStateManager.scale(scale, scale, scale);

            Vector2f StatBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(),oneOverScale);

            String healString = (ps.HealDuration != 0 ? " +" + ps.HealDuration +"/s " + ps.HealDurationTicker : "");

            if(editingMode){
                healString = " +" + "170" +"/s " + "\u2585";
            }
            stringWidth = mc.fontRendererObj.getStringWidth(healString);
            ApecUtils.drawStylizedString(healString, (int) (StatBar.x - stringWidth), (int) (StatBar.y - 10), 0xd10808);
            
        }
        GlStateManager.popMatrix();
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return guiModifier.applyGlobalChanges(this,new Vector2f(g_sr.getScaledWidth() - 84, 15));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-stringWidth*scale,-11*scale);
    }

}
