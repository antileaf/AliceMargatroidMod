package me.antileaf.alice.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.utils.AliceHelper;

public class ShanghaiDollRelic extends CustomRelic {
	public static final String SIMPLE_NAME = ShanghaiDollRelic.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);

	private static final String IMG = AliceHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	public static final int DAMAGE = 4;

	private boolean activated = false;

	public ShanghaiDollRelic() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.SPECIAL,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);

		this.checkNameAndFlavor();
	}

	public void checkNameAndFlavor() {
		if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null &&
				AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS) {
			ReflectionHacks.setPrivateFinal(this, AbstractRelic.class, "name",
					this.DESCRIPTIONS[1]);
			this.flavorText = this.DESCRIPTIONS[2];

			this.tips.clear();
			this.tips.add(new PowerTip(this.name, this.description));
			this.initializeTips();
		}
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	@Override
	public void atBattleStartPreDraw() {
		this.activated = false;
	}

	@Override
	public void atTurnStart() {
		if (!this.activated) {
			this.activated = true;

			this.flash();
			this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			this.addToBot(new AnonymousAction(() -> {
				AbstractMonster m = AliceHelper.getMonsterWithLeastHP();

				if (m != null)
					this.addToTop(new DamageAction(m,
							new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS),
							AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			}));
		}
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new ShanghaiDollRelic();
	}
}
