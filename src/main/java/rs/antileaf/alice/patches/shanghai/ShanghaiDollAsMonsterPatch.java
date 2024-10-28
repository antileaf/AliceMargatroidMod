package rs.antileaf.alice.patches.shanghai;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.monsters.ShanghaiDollAsMonster;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ShanghaiDollAsMonsterPatch {
	private static final float CHANCE = 0.5F;

	@SpirePatch(
			clz = AbstractDungeon.class,
			method = "getMonsterForRoomCreation"
	)
	public static class ReplaceLouseWithShanghaiDoll {
		@SpirePostfixPatch
		public static MonsterGroup Postfix(MonsterGroup group, AbstractDungeon _inst) {
			if ((AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS ||
					AliceConfigHelper.enableShanghaiDollEventForOtherCharacters()) &&
					!AliceSpireKit.getSaveData().getHasTriggeredShanghaiDollEvent()) {
				ArrayList<Integer> indices = new ArrayList<>();
				for (int i = 0; i < group.monsters.size(); i++) {
					AbstractMonster m = group.monsters.get(i);
					if (m instanceof LouseNormal || m instanceof LouseDefensive)
						indices.add(i);
				}

				if (!indices.isEmpty() && AbstractDungeon.monsterRng.randomBoolean(CHANCE)) {
					int index = indices.get(AbstractDungeon.monsterRng.random(indices.size() - 1));
					AbstractMonster louse = group.monsters.get(index);

					float x = (louse.drawX - Settings.WIDTH * 0.75F) / Settings.xScale;
					float y = (louse.drawY - AbstractDungeon.floorY) / Settings.yScale;
					y += 60.0F;

					group.monsters.set(index, new ShanghaiDollAsMonster(x, y));
					louse.dispose();

					AliceSpireKit.logger.info("Successfully replaced Louse with Shanghai Doll at index {}", index);
				}
			}

			return group;
		}
	}

	@SpirePatch(
			clz = AbstractRoom.class,
			method = "update"
	)
	public static class UpdateSaveDataAfterVictoryPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.NewExprMatcher(SaveFile.class));
				if (tmp.length != 1)
					throw new PatchingException("Failed to find the only SaveFile constructor call in AbstractRoom.update");
				return new int[]{tmp[0]};
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractRoom _inst) {
			AliceSpireKit.logger.info("Victory! monsters: {}", _inst.monsters.monsters);

			if (_inst.monsters.monsters.stream().anyMatch(m -> m instanceof ShanghaiDollAsMonster)) {
				AliceSpireKit.getSaveData().setHasTriggeredShanghaiDollEvent(true);
				AliceSpireKit.logger.info("Shanghai Doll event triggered!");
			}
		}
	}
}
