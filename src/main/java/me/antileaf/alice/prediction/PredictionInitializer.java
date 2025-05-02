package me.antileaf.alice.prediction;

import basemod.AutoAdd;
import me.antileaf.alice.utils.AliceHelper;
import randomNumberPredictionMaster.helper.PredictorUpdateHelper;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

public class PredictionInitializer {
	public void initialize() {
		if (AliceHelper.isRandomPredictionModAvailable()) {
			new AutoAdd("AliceMargatroidMod")
					.packageFilter("me.antileaf.alice.prediction.predictors")
					.any(AbstractPredictor.class, (info, p) -> {
						PredictorUpdateHelper.Predictors.add(p);
					});
		}
	}
}
