package rs.antileaf.alice.monsters;

import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.relics.ShanghaiDollRelic;
import rs.antileaf.alice.strings.AliceLanguageStrings;
import rs.antileaf.alice.utils.AliceSpireKit;
import rs.antileaf.alice.utils.AliceSpriterAnimation;

import java.util.stream.Stream;

public class ShanghaiDollAsMonster extends CustomMonster {
	public static final String SIMPLE_NAME = ShanghaiDollAsMonster.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

	private boolean hasStrengthened = false;
	private boolean hasWeakened = false;
	private int attackedCount = 0;
	private int bonusDamage = 0;

	public ShanghaiDollAsMonster(float x, float y) {
		super(
				monsterStrings.NAME,
				ID,
				7,
				0.0F,
				-80.0F,
				150.0F,
				166.0F,
				AliceSpireKit.getImgFilePath("monsters", SIMPLE_NAME + "/Idle/objectAa000"),
				x,
				y
		);

		this.type = EnemyType.NORMAL;
		this.animation = new AliceSpriterAnimation("AliceMargatroidMod/img/monsters/ShanghaiDollAsMonster/Shanghai.scml");
		this.dialogX = (this.hb_x - 50.0F) * Settings.scale;
		this.dialogY -= (this.hb_y - 30.0F) * Settings.scale;

		if (AbstractDungeon.ascensionLevel >= 7)
			this.setHp(20, 24);
		else
			this.setHp(23, 27);

		((AliceSpriterAnimation) this.animation).myPlayer.addListener(new ShanghaiListener(this));
		this.setAnim("Idle");
		this.animation.setFlip(true, false);
	}

	public ShanghaiDollAsMonster() {
		this(0.0F, 0.0F);
	}

	private void setAnim(String key) {
		((AliceSpriterAnimation) this.animation).myPlayer.setAnimation(key);
	}

	private int getStrength() {
		return 3;
	}

	private int getBlock() {
		return AbstractDungeon.ascensionLevel >= 7 ? 8 : 6;
	}

	private int getFrail() {
		return AbstractDungeon.ascensionLevel >= 17 ? 2 : 1;
	}

	private int getDamage() {
		return AbstractDungeon.ascensionLevel >= 2 ? 6 : 5; // + 0~2
	}

	@Override
	public void usePreBattleAction() {
		super.usePreBattleAction();

		if (AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS)
			this.addToBot(new TalkAction(this, monsterStrings.DIALOG[0], 0.5F, 2.0F));
		else {
			this.name = Stream.generate(() -> AliceLanguageStrings.QUESTION_MARK)
					.limit(3)
					.reduce("", String::concat);
			this.addToBot(new TalkAction(this, monsterStrings.DIALOG[1], 0.5F, 2.0F));
		}

		if (AbstractDungeon.ascensionLevel >= 17)
			this.addToBot(new ApplyPowerAction(this, this,
					new ArtifactPower(this, 1), 1));

		this.addToBot(new GainBlockAction(this, this, this.getBlock()));
	}

	@Override
	public void getMove(int num) {
		this.bonusDamage = num % 3;

		if (!this.hasStrengthened)
			this.setMove(monsterStrings.MOVES[0], (byte) 0, Intent.DEFEND_BUFF);
		else if (!this.hasWeakened)
			this.setMove(monsterStrings.MOVES[1], (byte) 1, Intent.DEBUFF);
		else {
			if (this.attackedCount + (num % 2) < 3)
				this.setMove(monsterStrings.MOVES[2], (byte) 2, Intent.ATTACK,
						this.getDamage() + this.bonusDamage);
			else
				this.setMove(monsterStrings.MOVES[3], (byte) 3, Intent.ESCAPE);
		}
	}

	@Override
	public void takeTurn() {
		if (this.nextMove == 0) {
			this.setAnim("Attack");

			this.addToBot(new ApplyPowerAction(this, this,
					new StrengthPower(this, this.getStrength()), this.getStrength()));
			this.addToBot(new GainBlockAction(this, this, this.getBlock()));
			this.hasStrengthened = true;
		}
		else if (this.nextMove == 1) {
			this.setAnim("Attack");

			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this,
					new FrailPower(AbstractDungeon.player, this.getFrail(), true), this.getFrail()));
			this.hasWeakened = true;
		}
		else if (this.nextMove == 2) {
			this.addToBot(new DamageAction(AbstractDungeon.player,
					new DamageInfo(this, this.getDamage() + this.bonusDamage,
							DamageInfo.DamageType.NORMAL),
					AbstractGameAction.AttackEffect.FIRE));
			this.attackedCount++;

			this.setAnim("Attack");
		}
		else {
			this.setAnim("Escape");
			this.animation.setFlip(false, false);

			this.addToBot(new EscapeAction(this));
		}

		this.addToBot(new RollMoveAction(this));
	}

	@Override
	public void die() {
		super.die();
		this.setAnim("Fall");

		AbstractDungeon.getCurrRoom().addRelicToRewards(new ShanghaiDollRelic());
	}

	public static class ShanghaiListener implements Player.PlayerListener {
		private final ShanghaiDollAsMonster owner;

		public ShanghaiListener(ShanghaiDollAsMonster owner) {
			this.owner = owner;
		}

		@Override
		public void animationFinished(Animation animation) {
			if (!animation.name.equals("Escape") && !animation.name.equals("Idle") && !animation.name.equals("Fall")) {
				owner.setAnim("Idle");
				AliceSpireKit.logger.info("ShanghaiDollAsMonster ended anim {}", animation.name);
			}
		}

		@Override
		public void animationChanged(Animation a, Animation b) {
			AliceSpireKit.logger.info("ShanghaiDollAsMonster changed to anim {}", b.name);
		}

		@Override
		public void preProcess(Player player) {}

		@Override
		public void postProcess(Player player) {}

		@Override
		public void mainlineKeyChanged(Mainline.Key a, Mainline.Key b) {}
	}
}
