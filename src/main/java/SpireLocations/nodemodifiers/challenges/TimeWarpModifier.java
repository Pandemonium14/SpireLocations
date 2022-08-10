package SpireLocations.nodemodifiers.challenges;

import SpireLocations.NodeModifierHelper;
import SpireLocations.SpireLocationsMod;
import SpireLocations.nodemodifiers.AbstractNodeModifier;
import SpireLocations.patches.NodeModifierField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import java.util.ArrayList;

public class TimeWarpModifier extends AbstractNodeModifier {

    public static String ID = SpireLocationsMod.makeID("TimeWarp");

    public TimeWarpModifier() {
        super(ID, NodeModType.CHALLENGE);
    }

    @Override
    public ArrayList<Class<? extends AbstractRoom>> getRoomClasses() {
        return new ArrayList<Class<? extends AbstractRoom>>() {{
            add(MonsterRoom.class);
        }};
    }

    @Override
    public void atBattleStart() {
        AbstractMonster m = m = AbstractDungeon.getRandomMonster();;
        while (m.hasPower(MinionPower.POWER_ID)) {
            m = AbstractDungeon.getRandomMonster();
        }
        addToBot(new ApplyPowerAction(m, m, new TimeWarpPower(m)));
    }

    @Override
    public void onGeneration(AbstractRoom room) {
        NodeModifierField.modifiers.get(room).add(NodeModifierHelper.getModifier(room.getClass(), NodeModType.REWARD));
    }
}