package rs.antileaf.alice.cards.alice;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceHelper;

public class TheSetup extends AbstractAliceCard {
	public static final String SIMPLE_NAME = TheSetup.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 5;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	
	public TheSetup() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.fromDolls(DollManager.get().getDolls().stream()
				.filter(doll -> doll != slot)
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.filter(doll -> doll.calcTotalDamageAboutToTake() != -1)
				.toArray(AbstractDoll[]::new));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageCallbackAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
				(amount) -> {
					if (amount > 0) {
						for (AbstractDoll doll : DollManager.get().getDolls())
							if (!(doll instanceof EmptyDollSlot) && doll.calcTotalDamageAboutToTake() != -1)
								AliceHelper.addActionToBuffer(new DollGainBlockAction(doll, amount));
						
						AliceHelper.commitBuffer();
					}
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new TheSetup();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.initializeDescription();
		}
	}
}
