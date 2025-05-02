package me.antileaf.alice.cards.alice;

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
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.action.doll.HealDollAction;
import me.antileaf.alice.action.doll.IncreaseDollMaxHealthAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		this.isMultiDamage = true;

		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}

	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot != null && !(slot instanceof EmptyDollSlot))
			return new AliceHoveredTargets(){{
				monsters = DollManager.get().getCorrespondingEnemies(slot);
			}};

		return AliceHoveredTargets.NONE;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		if (AliceHelper.isInBattle()) {
			AbstractDoll doll = this.getTargetedDoll();

			if (AbstractDungeon.player.hoveredCard == this) {
				doll = DollManager.get().getHoveredDoll();

				if (doll instanceof EmptyDollSlot)
					doll = null;
			}

			boolean calculated = false;
			if (doll != null) {
				AbstractMonster[] monsters = DollManager.get().getCorrespondingEnemies(doll);

				if (monsters.length > 0) {
					super.calculateCardDamage(mo);
					calculated = true;

					this.isDamageModified = false;

					for (int k = monsters.length - 1; k >= 0; k--) {
						int i = AbstractDungeon.getMonsters().monsters.indexOf(monsters[k]);

						if (i < 0)
							continue;

						this.damage = this.multiDamage[i];
						this.isDamageModified |= this.damage != this.baseDamage;
					}
				}
			}

			if (!calculated) {
				this.isMultiDamage = false;
				super.calculateCardDamage(null);
				this.isMultiDamage = true;
			}
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
			for (AbstractMonster mo : DollManager.get().getCorrespondingEnemies(doll))
				this.addToBot(new DamageAction(mo, new DamageInfo(p,
						this.multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(mo)],
						this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.BLUNT_LIGHT));

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
