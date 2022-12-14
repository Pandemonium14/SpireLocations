package SpireLocations.nodemodifiers.rewards;

import SpireLocations.SpireLocationsMod;
import SpireLocations.nodemodifiers.AbstractNodeModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import java.util.ArrayList;

public class UpgradedRewardsModifier extends AbstractNodeModifier {

    public static final String ID = SpireLocationsMod.makeID("UpgradedRewards");

    public UpgradedRewardsModifier() {
        super(ID,NodeModType.REWARD, iconPath("UpArrowCardReward"));
    }

    @Override
    public ArrayList<Class<? extends AbstractRoom>> getRoomClasses() {
        ArrayList<Class<? extends AbstractRoom>> result = new ArrayList<>();
        result.add(MonsterRoom.class);
        return result;
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        for (RewardItem r : rewards) {
            if (r.cards != null) {
                for (AbstractCard c : r.cards) {
                    if (!c.upgraded && c.canUpgrade()) {
                        c.upgrade();
                    }
                }
            }
        }
    }
}
