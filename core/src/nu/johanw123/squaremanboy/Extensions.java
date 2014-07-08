package nu.johanw123.squaremanboy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by johanw123 on 2014-06-14.
 */
public class Extensions {


    public static float FormatFloatPrecision(float floatValue, int decimalCount) {
        BigDecimal value = new BigDecimal(floatValue).setScale(decimalCount, RoundingMode.HALF_EVEN);
        return value.floatValue();
    }

}
