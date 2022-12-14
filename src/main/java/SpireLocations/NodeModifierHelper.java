package SpireLocations;

import SpireLocations.nodemodifiers.AbstractNodeModifier;
import SpireLocations.patches.NodeModifierField;
import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class NodeModifierHelper {

    public static ArrayList<AbstractNodeModifier> nodeModifiers = new ArrayList<>();

    public static void registerModifier(AbstractNodeModifier mod) {
        if (mod.isEnabled()) {
            nodeModifiers.add(mod);
        }
    }


    public static void addModifier(MapRoomNode node, int floor) {
        AbstractRoom room = node.getRoom();
        if (shouldAddModifier(room.getClass())) {
            AbstractNodeModifier.NodeModType type = rollForType(floor);
            if (type == null) return;
            AbstractNodeModifier mod = getModifier(room.getClass(), type, AbstractDungeon.actNum);
            if (mod != null) {
                NodeModifierField.modifiers.get(room).add(mod);

                if (mod.type == AbstractNodeModifier.NodeModType.CHALLENGE) {
                    AbstractNodeModifier rewardMod = getModifier(room.getClass(), AbstractNodeModifier.NodeModType.REWARD, AbstractDungeon.actNum);
                    if (rewardMod != null) {
                        NodeModifierField.modifiers.get(room).add(rewardMod);
                        BaseMod.logger.log(Level.INFO, "Adding reward to challenge node : " + rewardMod.MODIFIER_ID);
                    }
                }
                for (AbstractNodeModifier m : NodeModifierField.modifiers.get(room)) {
                    m.onGeneration(room);
                    m.onGeneration(room,floor);
                }
            }
        }
    }


    private static boolean shouldAddModifier(Class<? extends AbstractRoom> roomClass) {
        int r = AbstractDungeon.mapRng.random(99);
        r -= SpireLocationsMod.bonusModifierProb;
        if (roomClass.equals(RestRoom.class)) {
            return r < 35;
        } else if (roomClass.equals(MonsterRoomElite.class)) {
            return r < 35;
        } else if (roomClass.equals(MonsterRoom.class)) {
            return r < 25;
        } else if (roomClass.equals(ShopRoom.class)) {
            return r < 60;
        } else if (roomClass.equals(EventRoom.class)) {
            return r < 20;
        } else if (roomClass.equals(TreasureRoom.class)) {
            return r < 35;
        } else {
            return false;
        }
    }

    private static AbstractNodeModifier.NodeModType rollForType(int floor) {
        int r = AbstractDungeon.mapRng.random(99);
        if (r < 25) {
            return AbstractNodeModifier.NodeModType.SPECIAL;
        } else if (r < 60) {
            if (floor > 2) return AbstractNodeModifier.NodeModType.CHALLENGE;
            else return null;
        } else {
            return AbstractNodeModifier.NodeModType.BONUS;
        }
    }


    public static AbstractNodeModifier getModifier(Class<? extends AbstractRoom> roomClass, AbstractNodeModifier.NodeModType type, int actNum) {
        ArrayList<AbstractNodeModifier> list = new ArrayList<>();
        for (AbstractNodeModifier mod : nodeModifiers) {
            if (mod.type == type && mod.roomClasses.contains(roomClass) && mod.enableInAct(actNum)) {
                list.add(mod);
            }
        }
        if (list.size() > 0) {
            int index = AbstractDungeon.mapRng.random(list.size() - 1);
            return list.get(index).makeCopy();
        } else {
            BaseMod.logger.log(Level.INFO,"No suitable " + type.name() + " modifier found for " + roomClass.getName());
            return null;
        }
    }
}
