/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 20.05.2005
 */
package net.finmath.marketdata.model.curves;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.time.TimeDiscretization;

/**
 * Implementation of a discount factor curve based on {@link net.finmath.marketdata.model.curves.CurveFromInterpolationPoints}. The discount curve is based on the {@link net.finmath.marketdata.model.curves.CurveFromInterpolationPoints} class.
 *
 * It thus features all interpolation and extrapolation methods and interpolation entities
 * as {@link net.finmath.marketdata.model.curves.CurveFromInterpolationPoints} and implements the {@link net.finmath.marketdata.model.curves.DiscountCurve}.
 *
 * Note that this version of the DiscountCurveInterpolation will no longer make the
 * assumption that at t=0 its value is 1.0. Such a norming is not
 * necessary since valuation will always divide by the corresponding
 * discount factor at evaluation time. See the implementation of {@link net.finmath.marketdata.products.SwapLeg}
 * for an example.
 *
 * @author Christian Fries
 * @see net.finmath.marketdata.products.SwapLeg
 * @see net.finmath.marketdata.model.curves.CurveFromInterpolationPoints
 * @version 1.0
 */
public class DiscountCurveInterpolation extends CurveFromInterpolationPoints implements Serializable, DiscountCurve {

	private static final long serialVersionUID = -4126228588123963885L;

	/**
	 * Create an empty discount curve using default interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 */
	private DiscountCurveInterpolation(String name) {
		super(name, null, InterpolationMethod.LINEAR, ExtrapolationMethod.CONSTANT, InterpolationEntity.LOG_OF_VALUE_PER_TIME);
	}

	/**
	 * Create an empty discount curve using given interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 */
	private DiscountCurveInterpolation(String name, InterpolationMethod interpolationMethod,
			ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity){

		super(name, null, interpolationMethod, extrapolationMethod, interpolationEntity);
	}


	/**
	 * Create an empty discount curve using given interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 */
	private DiscountCurveInterpolation(String name, LocalDate referenceDate, InterpolationMethod interpolationMethod,
			ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity){

		super(name, referenceDate, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given discount factors using given interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenDiscountFactors Array of corresponding discount factors.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromDiscountFactors(
			String name, LocalDate referenceDate,
			double[] times, double[] givenDiscountFactors, boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		DiscountCurveInterpolation discountFactors = new DiscountCurveInterpolation(name, referenceDate, interpolationMethod, extrapolationMethod, interpolationEntity);

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			discountFactors.addDiscountFactor(times[timeIndex], givenDiscountFactors[timeIndex], isParameter != null && isParameter[timeIndex]);
		}

		return discountFactors;
	}

	/**
	 * Create a discount curve from given times and given discount factors using given interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param times Array of times as doubles.
	 * @param givenDiscountFactors Array of corresponding discount factors.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromDiscountFactors(
			String name,
			double[] times,
			double[] givenDiscountFactors,
			boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {
		return createDiscountCurveFromDiscountFactors(name, null, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given discount factors using given interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param times Array of times as doubles.
	 * @param givenDiscountFactors Array of corresponding discount factors.
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromDiscountFactors(
			String name,
			double[] times,
			double[] givenDiscountFactors,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {
		boolean[] isParameter = new boolean[times.length];
		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			isParameter[timeIndex] = times[timeIndex] > 0;
		}

		return createDiscountCurveFromDiscountFactors(name, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given discount factors using default interpolation and extrapolation methods.
	 *
	 * @param name The name of this discount curve.
	 * @param times Array of times as doubles.
	 * @param givenDiscountFactors Array of corresponding discount factors.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromDiscountFactors(String name, double[] times, double[] givenDiscountFactors) {
		DiscountCurveInterpolation discountFactors = new DiscountCurveInterpolation(name);

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			discountFactors.addDiscountFactor(times[timeIndex], givenDiscountFactors[timeIndex], times[timeIndex] > 0);
		}

		return discountFactors;
	}

	/**
	 * Create a discount curve from given times and given zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenZeroRates Array of corresponding zero rates.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromZeroRates(
			String name, LocalDate referenceDate,
			double[] times, double[] givenZeroRates, boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		double[] givenDiscountFactors = new double[givenZeroRates.length];

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
		}

		return createDiscountCurveFromDiscountFactors(name, referenceDate, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenZeroRates Array of corresponding zero rates.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromZeroRates(
			String name, Date referenceDate,
			double[] times, double[] givenZeroRates, boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		return createDiscountCurveFromZeroRates(name, referenceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), times, givenZeroRates, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param times Array of times as doubles.
	 * @param givenZeroRates Array of corresponding zero rates.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 * @deprecated Initializing a curve without reference date is deprecated.
	 */
	@Deprecated
	public static DiscountCurveInterpolation createDiscountCurveFromZeroRates(
			String name,
			double[] times, double[] givenZeroRates, boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		return createDiscountCurveFromZeroRates(name, (LocalDate)null, times, givenZeroRates, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenZeroRates Array of corresponding zero rates.
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromZeroRates(
			String name, LocalDate referenceDate,
			double[] times, double[] givenZeroRates,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		double[] givenDiscountFactors = new double[givenZeroRates.length];
		boolean[] isParameter = new boolean[givenZeroRates.length];

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
			isParameter[timeIndex] = false;
		}

		return createDiscountCurveFromDiscountFactors(name, referenceDate, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given zero rates using default interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param times Array of times as doubles.
	 * @param givenZeroRates Array of corresponding zero rates.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromZeroRates(String name, double[] times, double[] givenZeroRates) {
		double[] givenDiscountFactors = new double[givenZeroRates.length];

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			givenDiscountFactors[timeIndex] = Math.exp(- givenZeroRates[timeIndex] * times[timeIndex]);
		}

		return createDiscountCurveFromDiscountFactors(name, times, givenDiscountFactors);
	}

	/**
	 * Create a discount curve from given times and given annualized zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.pow(1.0 + givenAnnualizedZeroRates[timeIndex], -times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenAnnualizedZeroRates Array of corresponding zero rates.
	 * @param isParameter Array of booleans specifying whether this point is served "as as parameter", e.g., whether it is calibrates (e.g. using CalibratedCurves).
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromAnnualizedZeroRates(
			String name, LocalDate referenceDate,
			double[] times, double[] givenAnnualizedZeroRates, boolean[] isParameter,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		double[] givenDiscountFactors = new double[givenAnnualizedZeroRates.length];

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			givenDiscountFactors[timeIndex] = Math.pow(1.0 + givenAnnualizedZeroRates[timeIndex], -times[timeIndex]);
		}

		return createDiscountCurveFromDiscountFactors(name, referenceDate, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given times and given annualized zero rates using given interpolation and extrapolation methods.
	 * The discount factor is determined by
	 * <code>
	 * 		givenDiscountFactors[timeIndex] = Math.pow(1.0 + givenAnnualizedZeroRates[timeIndex], -times[timeIndex]);
	 * </code>
	 *
	 * @param name The name of this discount curve.
	 * @param referenceDate The reference date for this curve, i.e., the date which defined t=0.
	 * @param times Array of times as doubles.
	 * @param givenAnnualizedZeroRates Array of corresponding zero rates.
	 * @param interpolationMethod The interpolation method used for the curve.
	 * @param extrapolationMethod The extrapolation method used for the curve.
	 * @param interpolationEntity The entity interpolated/extrapolated.
	 * @return A new discount factor object.
	 */
	public static DiscountCurveInterpolation createDiscountCurveFromAnnualizedZeroRates(
			String name, LocalDate referenceDate,
			double[] times, double[] givenAnnualizedZeroRates,
			InterpolationMethod interpolationMethod, ExtrapolationMethod extrapolationMethod, InterpolationEntity interpolationEntity) {

		double[] givenDiscountFactors = new double[givenAnnualizedZeroRates.length];
		boolean[] isParameter = new boolean[givenAnnualizedZeroRates.length];

		for(int timeIndex=0; timeIndex<times.length;timeIndex++) {
			givenDiscountFactors[timeIndex] = Math.pow(1.0 + givenAnnualizedZeroRates[timeIndex], -times[timeIndex]);
			isParameter[timeIndex] = false;
		}

		return createDiscountCurveFromDiscountFactors(name, referenceDate, times, givenDiscountFactors, isParameter, interpolationMethod, extrapolationMethod, interpolationEntity);
	}

	/**
	 * Create a discount curve from given time discretization and forward rates.
	 * This function is provided for "single interest rate curve" frameworks.
	 *
	 * @param name The name of this discount curve.
	 * @param tenor Time discretization for the forward rates
	 * @param forwardRates Array of forward rates.
	 * @return A new discount curve object.
	 */
	public static DiscountCurve createDiscountFactorsFromForwardRates(String name, TimeDiscretization tenor, double[] forwardRates) {
		DiscountCurveInterpolation discountFactors = new DiscountCurveInterpolation(name);

		double df = 1.0;
		for(int timeIndex=0; timeIndex<tenor.getNumberOfTimeSteps();timeIndex++) {
			df /= 1.0 + forwardRates[timeIndex] * tenor.getTimeStep(timeIndex);
			discountFactors.addDiscountFactor(tenor.getTime(timeIndex+1), df, tenor.getTime(timeIndex+1) > 0);
		}

		return discountFactors;
	}

	/* (non-Javadoc)
	 * @see net.finmath.marketdata.model.curves.DiscountCurveInterface#getDiscountFactor(double)
	 */
	@Override
	public double getDiscountFactor(double maturity)
	{
		return getDiscountFactor(null, maturity);
	}

	/* (non-Javadoc)
	 * @see net.finmath.marketdata.model.curves.DiscountCurveInterface#getDiscountFactor(double)
	 */
	@Override
	public double getDiscountFactor(AnalyticModel model, double maturity)
	{
		return getValue(model, maturity);
	}


	/**
	 * Returns the zero rate for a given maturity, i.e., -ln(df(T)) / T where T is the given maturity and df(T) is
	 * the discount factor at time $T$.
	 *
	 * @param maturity The given maturity.
	 * @return The zero rate.
	 */
	public double getZeroRate(double maturity)
	{
		if(maturity == 0) {
			return this.getZeroRate(1.0E-14);
		}

		return -Math.log(getDiscountFactor(null, maturity))/maturity;
	}

	/**
	 * Returns the zero rates for a given vector maturities.
	 *
	 * @param maturities The given maturities.
	 * @return The zero rates.
	 */
	public double[] getZeroRates(double[] maturities)
	{
		double[] values = new double[maturities.length];

		for(int i=0; i<maturities.length; i++) {
			values[i] = getZeroRate(maturities[i]);
		}

		return values;
	}

	protected void addDiscountFactor(double maturity, double discountFactor, boolean isParameter) {
		this.addPoint(maturity, discountFactor, isParameter);
	}

	@Override
	public String toString() {
		return "DiscountCurveInterpolation [" + super.toString() + "]";
	}
}
