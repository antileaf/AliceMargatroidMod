package rs.antileaf.alice.icon;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.utils.AliceHelper;

public class AliceCustomIcon extends AbstractCustomIcon {
	public AliceCustomIcon(String name, String imgName) {
		super(
				AliceMargatroidMod.SIMPLE_NAME.toLowerCase() + ":" + name,
				new Texture(AliceHelper.getImgFilePath("icons", imgName))
		);

//		this.region.offsetY -= 5.0F;
	}

	@Override
	public float getRenderScale() {
		return 27.0F / (float) this.getImgSize();
	}
}
