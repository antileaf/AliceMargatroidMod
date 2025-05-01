package rs.antileaf.alice.cards.alice;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.HealDollAction;
import rs.antileaf.alice.action.doll.IncreaseDollMaxHealthAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

public class SuicideSquad extends AbstractAliceCard {
	private static final Logger logger = LogManager.getLogger(SuicideSquad.class.getName());

	public static final String SIMPLE_NAME = SuicideSquad.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int DAMAGE = 4;
	private static final int BLOCK = 4;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_DMG = 1;
	private static final int UPGRADE_PLUS_BLOCK = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public SuicideSquad() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}

	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot != null && !(slot instanceof EmptyDollSlot)) {
			int index = DollManager.get().getIndex(slot);
			AbstractMonster m = AliceHelper.getMonsterByIndex(index);
			if (m != null)
				return new AliceHoveredTargets(){{
					monsters = new AbstractMonster[]{m};
				}};
		}

		return AliceHoveredTargets.NONE;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		if (mo == null && AliceHelper.isInBattle()) {
			AbstractMonster target = null;

			AbstractDoll doll = this.getTargetedDoll();

			if (AbstractDungeon.player.hoveredCard == this) {
				doll = DollManager.get().getHoveredDoll();

				if (doll instanceof EmptyDollSlot)
					doll = null;
			}

			if (doll != null) {
				int index = DollManager.get().getIndex(doll);
				target = AliceHelper.getMonsterByIndex(index);
			}

//			logger.info("doll = {}, target = {}", doll, target);

			super.calculateCardDamage(target);

			this.isDamageModified = this.baseDamage != this.damage;
		}
		else
			super.calculateCardDamage(mo);
	}

	@Override
	public void render(SpriteBatch sb) {
		if (AliceHelper.isInBattle() && AbstractDungeon.player.hoveredCard == this)
			this.calculateCardDamage(null);

		super.render(sb);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();

		if (doll != null) {
			int index = DollManager.get().getIndex(doll);
			AbstractMonster mo = AliceHelper.getMonsterByIndex(index);

			if (mo != null) {
				this.calculateCardDamage(mo);
				this.addToBot(new DamageAction(mo, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.BLUNT_LIGHT));
			}

			this.addToBot(new DollGainBlockAction(doll, this.block));
			this.addToBot(new HealDollAction(doll, this.magicNumber));
			this.addToBot(new IncreaseDollMaxHealthAction(doll, this.magicNumber));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SuicideSquad();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
