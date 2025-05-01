package rs.antileaf.alice.prediction;

import basemod.AutoAdd;
import randomNumberPredictionMaster.helper.PredictorUpdateHelper;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.utils.AliceHelper;

public class PredictionInitializer {
	public void initialize() {
		if (AliceHelper.isRandomPredictionModAvailable()) {
			new AutoAdd("AliceMargatroidMod")
					.packageFilter("rs.antileaf.alice.prediction.predictors")
					.any(AbstractPredictor.class, (info, p) -> {
						PredictorUpdateHelper.Predictors.add(p);
					});
		}
	}
}
