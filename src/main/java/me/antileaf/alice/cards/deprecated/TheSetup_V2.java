package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class TheSetup_V2 extends AbstractAliceCard {
	public static final String SIMPLE_NAME = TheSetup_V2.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public TheSetup_V2() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("TheSetup"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		ArrayList<AbstractDoll> dolls = new ArrayList<>();
		
		for (int index = 0; index < AbstractDungeon.player.hand.size(); index++) {
			AbstractCard card = AbstractDungeon.player.hand.group.get(index);
			
			int i = DollManager.get().getDolls().size() - index - 1;
			if (i >= 0 && i < DollManager.get().getDolls().size()) {
				int cost = card.cost;
				if (cost == -1)
					cost = EnergyPanel.getCurrentEnergy() - (card.freeToPlay() ? 0 : Math.max(card.costForTurn, 0));
				
				if (cost == 1 || (this.upgraded && cost != 0)) {
					AbstractDoll doll = DollManager.get().getDolls().get(i);
					if (!(doll instanceof EmptyDollSlot))
						dolls.add(doll);
				}
			}
		}
		
		return AliceHoveredTargets.fromDolls(dolls.toArray(new AbstractDoll[0]));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int index = 0; index < AbstractDungeon.player.hand.size(); index++) {
			AbstractCard card = AbstractDungeon.player.hand.group.get(index);
			
			int i = DollManager.get().getDolls().size() - index - 1;
			if (i >= 0 && i < DollManager.get().getDolls().size()) {
				int cost = card.cost;
				if (cost == -1)
					cost = EnergyPanel.getCurrentEnergy();
				
				if (cost == 1 || (this.upgraded && cost != 0)) {
					AbstractDoll doll = DollManager.get().getDolls().get(i);
					if (!(doll instanceof EmptyDollSlot)) {
						for (int k = 0; k < cost; k++)
							this.addToBot(new DollActAction(doll));
					}
				}
			}
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new TheSetup_V2();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
