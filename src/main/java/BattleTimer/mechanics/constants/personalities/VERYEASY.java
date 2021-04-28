package BattleTimer.mechanics.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class VERYEASY extends AbstractPersonality {

    @Override
    public float calculateTimeValue() {
        try { return AbstractDungeon.monsterRng.random(0, -1); }
        catch (Exception e){ return 0; }
    }

    @Override
    public AbstractPersonality nextPersonality() { return new EASY(); }
}