package me.antileaf.alice.cards.deprecated;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cardmodifier.PhantomCardModifier;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceMiscHelper;

public class WillOWisp extends AbstractAliceCard {
	public static final String SIMPLE_NAME = WillOWisp.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int MAGIC = 2;
	private static final int UPGRADED_COST = 1;
	
	public WillOWisp() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (super.canUse(p, m)) {
			if (AliceHelper.hasDuplicateCards(p.hand.group, null)) {
				this.cantUseMessage = AliceHelper.getDuplicateCardsMessage(p.hand.group, null);
				return false;
			}
			else
				return true;
		}
		return false;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsInHandAction(
				AliceMiscHelper.join(
						cardStrings.EXTENDED_DESCRIPTION[0],
						"" + this.magicNumber,
						cardStrings.EXTENDED_DESCRIPTION[1]
				),
				(card) -> !PhantomCardModifier.check(card),
				(cards) -> {
					if (!cards.isEmpty()) {
						AbstractCard selected = cards.get(0);
						
						this.addToBot(new AnonymousAction(() -> {
//							boolean hasCreatedDialog = false;
							
							for (int i = 0; i < this.magicNumber; i++) {
								if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
									AbstractDungeon.player.createHandIsFullDialog();
									break;
								}
								else {
									AbstractCard copy = selected.makeStatEquivalentCopy();
									if (AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID))
										copy.upgrade();

									CardModifierManager.addModifier(copy, new PhantomCardModifier());
									AliceHelper.addCardToHand(copy);
								}
							}
						}));
					}
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new WillOWisp();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
