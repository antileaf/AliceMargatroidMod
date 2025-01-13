package rs.antileaf.alice.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.utils.AliceHelper;

public class AbstractAlicePower extends AbstractPower {
	public AbstractAlicePower() {
		super();
	}
	
	protected void initializeImage(String ID) {
		if (ID == null)
			ID = "default";
		
		String img84 = AliceHelper.getPowerImgFilePath(ID + "84");
		String img32 = AliceHelper.getPowerImgFilePath(ID + "32");
		this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img84), 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(img32), 0, 0, 32, 32);
	}
}
