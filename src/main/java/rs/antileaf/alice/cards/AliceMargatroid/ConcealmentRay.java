package rs.antileaf.alice.cards.AliceMargatroid;

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
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ConcealmentRay extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ConcealmentRay.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 5;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	
	public ConcealmentRay() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.tags.add(CardTagEnum.ALICE_RAY);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageCallbackAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
				(amount) -> {
					if (amount > 0) {
						for (int i = 0; i < DollManager.MAX_DOLL_SLOTS; i++) {
							AbstractMonster mon = AliceSpireKit.getMonsterByIndex(i);
							if (mon == null)
								break;
							
							if (mon.intent == AbstractMonster.Intent.ATTACK ||
									mon.intent == AbstractMonster.Intent.ATTACK_BUFF ||
									mon.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
									mon.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
								AbstractDoll doll = DollManager.get().getDolls().get(i);
								if (!(doll instanceof EmptyDollSlot))
									this.addToBot(new DollGainBlockAction(doll, amount));
							}
						}
					}
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ConcealmentRay();
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
