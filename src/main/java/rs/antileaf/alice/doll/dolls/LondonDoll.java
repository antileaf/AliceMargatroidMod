package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.utils.AliceSpireKit;

public class LondonDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = LondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	
	public LondonDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath("purple"),
				RenderTextMode.NONE
		);
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
//		this.highlightActValue();
	}
	
	@Override
	public void onRemoved() {
		this.addToBot(new AnonymousAction(() -> {
			int index = -1;
			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
				AbstractDoll doll = DollManager.get().getDolls().get(i);
				
				if ((doll instanceof EmptyDollSlot) || doll == this) {
					index = i;
					break;
				}
			}
			
			if (index == -1)
				AliceSpireKit.log(this.getClass(), "No empty doll slot found");
			
			this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.ID), index));
		}));
		
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.DESCRIPTION[0];
		this.actDescription = dollStrings.DESCRIPTION[1];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new LondonDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new LondonDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
