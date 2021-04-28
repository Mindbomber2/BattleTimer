package BattleTimer.mechanics;

import BattleTimer.core.BattleTimerCore;
import BattleTimer.mechanics.constants.personalities.AbstractPersonality;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

import java.util.ArrayList;

import static BattleTimer.mechanics.constants.EnemyTimers.*;

public class AbstractMonsterPatch {

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class patchIntoTimer {
        public static SpireField<Float> currentMonsterTimer = new SpireField<>(() -> 10f);
        public static SpireField<Float> currentMaxMonsterTimer = new SpireField<>(() -> 10f);

        public static float calculateTime(AbstractMonster __instance) {
            float f = 0;
            float timer_lb = 0;
            float timer_ub = 0;
            switch (__instance.type){
                case BOSS:
                    f = TURN_TIMER_BOSS;
                    timer_ub = 2;
                    timer_lb = -5;
                    break;
                case ELITE:
                    f = TURN_TIMER_ELITE;
                    timer_ub = 3;
                    timer_lb = - -4;
                    break;
                case NORMAL:
                    f = TURN_TIMER_NORMAL;
                    timer_ub = 3;
                    timer_lb = - -3;
                    break;
            }
            f += AbstractDungeon.monsterRng.random(timer_lb, timer_ub);
            if (!(AbstractDungeon.ascensionLevel == 20)) { f /= 0.976; }
            return f;
        }

    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    String.class,
                    String.class,
                    int.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    String.class,
                    float.class,
                    float.class
            }
    )

    public static class constructorTimer {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
            System.out.println("Patching ctor of " + __instance.name);
            float calculatedTime = patchIntoTimer.calculateTime(__instance);
            patchIntoTimer.currentMonsterTimer.set(__instance,calculatedTime);
            patchIntoTimer.currentMaxMonsterTimer.set(__instance, calculatedTime);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class timerRenderPatch {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, SpriteBatch sb) {
            System.out.println("render bullshit " + __instance.name);
            if(BattleTimerCore.hasMinions && __instance instanceof AbstractFriendlyMonster){
                System.out.println("hi wtf");
                return;
            }
            DrawMonsterTimer.drawMonsterTimer(sb, __instance, patchIntoTimer.currentMonsterTimer.get(__instance),
                    patchIntoTimer.currentMaxMonsterTimer.get(__instance));
            if(!AbstractDungeon.isScreenUp) {
                patchIntoTimer.currentMonsterTimer.set(__instance,
                        patchIntoTimer.currentMonsterTimer.get(__instance) - Gdx.graphics.getDeltaTime());
                if (patchIntoTimer.currentMonsterTimer.get(__instance) <= 0f) {
                    AbstractDungeon.actionManager.addToBottom(new monsterTakeTurnAction(__instance));
                    TurnbasedPowerStuff.triggerMonsterTurnPowers(__instance);
                    float calculatedTime = patchIntoTimer.calculateTime(__instance);
                    patchIntoTimer.currentMonsterTimer.set(__instance, calculatedTime);
                    patchIntoTimer.currentMaxMonsterTimer.set(__instance, calculatedTime);
                }
            }
        }
    }
}