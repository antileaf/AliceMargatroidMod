package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.powers.unique.SwordOfLightPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SwordOfLight_Supernova extends CustomRelic {
	public static final String SIMPLE_NAME = SwordOfLight_Supernova.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceSpireKit.getRelicLargeImgFilePath(SIMPLE_NAME);

	public SwordOfLight_Supernova() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.UNCOMMON,
				LandingSound.HEAVY
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atTurnStart() {
		this.beginPulse();
		this.pulse = true; // Equivalent to this.beginLongPulse();
		
		this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SwordOfLightPower()));
	}
	
	@Override
	public void onPlayerEndTurn() {
		this.pulse = false;
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (this.pulse) {
			if (card.type == AbstractCard.CardType.ATTACK) {
				this.flash();
				this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
				this.pulse = false;
			}
		}
	}
	
	@Override
	public void onVictory() {
		this.pulse = false;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new SwordOfLight_Supernova();
	}
}
