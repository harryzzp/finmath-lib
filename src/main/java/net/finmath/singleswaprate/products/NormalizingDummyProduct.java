package net.finmath.singleswaprate.products;

import net.finmath.singleswaprate.annuitymapping.AnnuityMapping;
import net.finmath.singleswaprate.annuitymapping.NormalizingFunction;
import net.finmath.singleswaprate.model.VolatilityCubeModel;
import net.finmath.time.Schedule;

/**
 * A dummy product that only evaluates the value of a {@link NormalizingFunction}. This can be used to calibrate the function.
 *
 * @author Christian Fries
 * @author Roland Bachl
 *
 */
public class NormalizingDummyProduct extends AbstractSingleSwapRateProduct {

	private final NormalizingFunction normalizer;

	/**
	 * Create the dummy product for a normalizer.
	 *
	 * @param fixSchedule The fix schedule of the product that is going to use this normalizer.
	 * @param floatSchedule The float schedule of the product that is going to use this normalizer.
	 * @param discountCurveName The name of the discount curve.
	 * @param forwardCurveName The name of the forward curve.
	 * @param volatilityCubeName The name of the volatility cube.
	 * @param normalizer The normalizer of this dummy.
	 */
	public NormalizingDummyProduct(Schedule fixSchedule, Schedule floatSchedule, String discountCurveName, String forwardCurveName,
			String volatilityCubeName, NormalizingFunction normalizer) {
		super(fixSchedule, floatSchedule, discountCurveName, forwardCurveName, volatilityCubeName);
		this.normalizer = normalizer;
	}

	@Override
	protected double payoffFunction(double swapRate, AnnuityMapping annuityMapping,
			VolatilityCubeModel model) {
		return normalizer.getValue(swapRate);
	}

	@Override
	protected double hedgeWeight(double swapRate, AnnuityMapping annuityMapping,
			VolatilityCubeModel model) {
		return normalizer.getSecondDerivative(swapRate);
	}

	@Override
	protected double singularAddon(double swapRate, AnnuityMapping annuityMapping,
			VolatilityCubeModel model) {
		return 0;
	}

	@Override
	protected AnnuityMapping buildAnnuityMapping(VolatilityCubeModel model) {
		return null;
	}

}
