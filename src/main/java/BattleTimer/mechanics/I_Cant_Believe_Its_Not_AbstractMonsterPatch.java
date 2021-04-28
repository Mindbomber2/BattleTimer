package BattleTimer.mechanics;

import BattleTimer.mechanics.constants.personalities.*;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static BattleTimer.mechanics.constants.EnemyTimers.*;

public class I_Cant_Believe_Its_Not_AbstractMonsterPatch {

    @SpirePatch(clz = CustomMonster.class, method = SpirePatch.CLASS)
    public static class patchIntoTimer {
        public static SpireField<Float> currentMonsterTimer = new SpireField<>(() -> 10f);
        public static SpireField<Float> currentMaxMonsterTimer = new SpireField<>(() -> 10f);

        public static float calculateTime(CustomMonster __instance) {
            float f = 0;
            if(AbstractDungeon.isPlayerInDungeon()) {
                AbstractPersonality currentPersonality;
                switch (__instance.type) {
                    case BOSS:
                        f = TURN_TIMER_BOSS;
                        break;
                    case ELITE:
                        f = TURN_TIMER_ELITE;
                        break;
                    case NORMAL:
                        f = TURN_TIMER_NORMAL;
                        break;
                    default:
                        f = TURN_TIMER_NORMAL;
                        break;
                }
                if (AbstractDungeon.ascensionLevel <= 5) { currentPersonality = new VERYEASY();
                } else if (AbstractDungeon.ascensionLevel <= 10) { currentPersonality = new EASY();
                } else if (AbstractDungeon.ascensionLevel <= 15) { currentPersonality = new MEDIUM();
                } else { currentPersonality = new VERYHARD(); }
                for (int i = 1; i <= GameActionManager.turn; i += 1) {
                    if (i % 2 == 0) { currentPersonality = currentPersonality.nextPersonality(); }
                }
                System.out.println(currentPersonality.toString());
                f += currentPersonality.calculateTimeValue();
            }
            return f;
        }

    }

    @SpirePatch(clz = CustomMonster.class, method = SpirePatch.CONSTRUCTOR,
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
        public static void timerCtorPatch(CustomMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
            System.out.println("Patching ctor of " + __instance.name);
            float calculatedTime = patchIntoTimer.calculateTime(__instance);
            patchIntoTimer.currentMonsterTimer.set(__instance,calculatedTime);
            patchIntoTimer.currentMaxMonsterTimer.set(__instance, calculatedTime);
        }
    }

    @SpirePatch(clz = CustomMonster.class, method = "render")
    public static class timerRenderPatch {
        @SpirePostfixPatch
        public static void timerCtorPatch(CustomMonster __instance, SpriteBatch sb) {
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