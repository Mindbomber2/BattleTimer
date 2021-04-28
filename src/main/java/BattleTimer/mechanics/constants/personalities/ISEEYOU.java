package BattleTimer.mechanics.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ISEEYOU extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-7.25f, -8); }
    @Override
    public AbstractPersonality nextPersonality() { return new COMINGFORYOU(); }
}