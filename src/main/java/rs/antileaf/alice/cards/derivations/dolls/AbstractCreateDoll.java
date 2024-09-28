package rs.antileaf.alice.cards.derivations.dolls;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.*;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public abstract class AbstractCreateDoll extends AbstractAliceCard {
	
	public AbstractCreateDoll(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription
	) {
		super(
				id,
				name,
				img,
				cost,
				rawDescription,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
		
		this.exhaust = true; // This card should not be gettable in any way.
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}
	
	public abstract AbstractDoll getDoll();
	
	@Override
	public boolean canUpgrade() {
		return false;
	}
	
	@Override
	public void upgrade() {
		// No upgrade.
	}
	
	public static AbstractCreateDoll get(String id) {
		if (id == null) {
			AliceSpireKit.log(AbstractCreateDoll.class, "Null doll id");
			return null;
		}
		
		else if (id.equals(ShanghaiDoll.ID))
			return new CreateShanghaiDoll();
		else if (id.equals(NetherlandsDoll.ID))
			return new CreateNetherlandsDoll();
		else if (id.equals(HouraiDoll.ID))
			return new CreateHouraiDoll();
		else if (id.equals(FranceDoll.ID))
			return new CreateFranceDoll();
		else if (id.equals(LondonDoll.ID))
			return new CreateLondonDoll();
		else if (id.equals(KyotoDoll.ID))
			return new CreateKyotoDoll();
		else if (id.equals(OrleansDoll.ID))
			return new CreateOrleansDoll();
		
		else {
			AliceSpireKit.log(AbstractCreateDoll.class, "Unknown doll id: " + id);
			return null;
		}
	}
}
