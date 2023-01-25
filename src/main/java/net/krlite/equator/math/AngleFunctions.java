package net.krlite.equator.math;

/**
 * <h2>Angle Functions</h2>
 * A class that calculates 2D angles.
 */
public class AngleFunctions {
    /**
     * Solves the included angle between a <b>positive</b> angle and a
     * <b>negative</b> angle.
     *
     * @param srcAngle  The source angle which is <b>positive</b>.
     * @param dstAngle  The destination angle which is <b>negative</b>.
     * @return          The included angle.
     */
    public static double positiveIncludeNegative(double srcAngle, double dstAngle) {
        return positiveIncludePositive(srcAngle, -dstAngle);
    }

    /**
     * Solves the included angle between two <b>positive</b> angles.
     *
     * @param srcAngle  The source angle which is <b>positive</b>.
     * @param dstAngle  The destination angle which is <b>positive</b>.
     * @return          The included angle.
     */
    public static double positiveIncludePositive(double srcAngle, double dstAngle) {
        return toPositive(dstAngle - srcAngle);
    }

    /**
     * Converts a <b>clockwise</b> angle to a <b>negative</b> angle.
     *
     * @param angle The angle which is <b>clockwise</b>.
     * @return      The corresponding <b>negative</b> angle.
     */
    public static double clockwiseToNegative(double angle) {
        return toNegative(clockwiseToPositive(angle));
    }

    /**
     * Converts a <b>clockwise</b> angle to a <b>positive</b> angle.
     *
     * @param angle The angle which is <b>clockwise</b>.
     * @return      The corresponding <b>positive</b> angle.
     */
    public static double clockwiseToPositive(double angle) {
        angle = toClockwise(angle);
        return toPositive(angle + (angle > 180 ? -360 : 0));
    }

    /**
     * Converts a <b>negative</b> angle to a <b>clockwise</b> angle.
     *
     * @param angle The angle which is <b>negative</b>.
     * @return      The corresponding <b>clockwise</b> angle.
     */
    public static double negativeToClockwise(double angle) {
        return toClockwise(toPositive(angle));
    }

    /**
     * Converts a <b>positive</b> angle to a <b>clockwise</b> angle.
     *
     * @param angle The angle which is <b>positive</b>.
     * @return      The corresponding <b>clockwise</b> angle.
     */
    public static double positiveToClockwise(double angle) {
        return toClockwise(toPositive(angle) + 180);
    }

    /**
     * Reverts an angle.
     *
     * @param angle The angle to revert.
     * @return      The reverted angle.
     */
    public static double revert(double angle) {
        angle = mold(angle);
        return angle + (angle > 0 ? -180 : 180);
    }

    /**
     * Reverts an angle by <b>clockwise</b>.
     *
     * @param angle The angle to revert.
     * @return      The reverted <b>clockwise</b> angle.
     */
    public static double revertClockwise(double angle) {
        return toClockwise(360 - toClockwise(angle));
    }
    
    /**
     * Opposites an angle.
     *
     * @param angle The angle to opposite.
     * @return      The opposite <b>positive</b> angle.
     */
    public static double opposite(double angle) {
        return mold(angle + 180);
    }

    /**
     * Opposites an angle by <b>clockwise</b>
     *
     * @param angle The angle to opposite.
     * @return      The opposite <b>clockwise</b> angle.
     */
    public static double oppositeClockwise(double angle) {
        return toClockwise(angle + 180);
    }

    /**
     * Molds the angle by <b>180</b>.
     *
     * @param angle The angle to mold.
     * @return      The modulus.
     */
    public static double mold(double angle) {
        return angle % 180;
    }

    /**
     * Casts an angle to <b>negative: <code>[180, -180)</code></b>.
     *
     * @param angle The angle to cast.
     * @return      The cast <b>negative</b> angle.
     */
    public static double toNegative(double angle) {
        angle = -toPositive(angle);
        if (angle == -180) {
            angle = 180;
        }
        return angle;
    }

    /**
     * Casts an angle to <b>positive: <code>(-180, 180]</code></b>.
     *
     * @param angle The angle to cast.
     * @return      The cast <b>positive</b> angle.
     */
    public static double toPositive(double angle) {
        angle %= 360;
        return angle + (angle > 180 ? -360 : angle <= -180 ? 360 : 0);
    }

    /**
     * Casts an angle to <b>clockwise: <code>[0, 360)</code></b>.
     *
     * @param angle The angle to cast.
     * @return      The cast <b>clockwise</b> angle.
     */
    public static double toClockwise(double angle) {
        return (angle % 360 + 360) % 360;
    }
}
