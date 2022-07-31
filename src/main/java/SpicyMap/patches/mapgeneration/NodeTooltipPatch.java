package SpicyMap.patches.mapgeneration;


import SpicyMap.nodemodifiers.AbstractNodeModifier;
import SpicyMap.patches.NodeModifierField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch2(clz = MapRoomNode.class, method = "update")
public class NodeTooltipPatch {

    @SpirePostfixPatch
    public static void updateModifierTooltips(MapRoomNode __instance) {
        if (__instance.hb.hovered) {
            if (__instance.room != null) {
                ArrayList<AbstractNodeModifier> mods = NodeModifierField.modifiers.get(__instance.room);
                ArrayList<PowerTip> tips = new ArrayList<>();
                for (AbstractNodeModifier mod : mods) {
                    String[] tipStrings = mod.getTooltipStrings();
                    PowerTip tip = new PowerTip(tipStrings[0], tipStrings[1]);
                    tips.add(tip);
                }
                TipHelper.queuePowerTips(InputHelper.mX + 50f, InputHelper.mY - 70f, tips);
            }
        }
    }

}
