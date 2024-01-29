package rs.antileaf.alice.relics;

import rs.antileaf.alice.AliceMagtroidMod;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;

public class AlicesGrimoire extends CustomRelic {
	public static final String SIMPLE_NAME = AlicesGrimoire.class.getSimpleName();

	public static final String ID = AliceMagtroidMod.SIMPLE_NAME + ":" + SIMPLE_NAME;
	private static final String IMG = "img/relics/" + SIMPLE_NAME + ".png";
	private static final String IMG_OTL = "img/relics/outline/" + SIMPLE_NAME + ".png";

	public AlicesGrimoire() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.RARE,
				LandingSound.MAGICAL
		);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		for (int i = 0; i < 2; i++)
			this.addToBot(new SpawnDollAction(new ShanghaiDoll(), -1));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new AlicesGrimoire();
	}
}