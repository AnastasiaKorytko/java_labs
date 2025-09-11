import org.example.HyperbolicCos;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class test {
    @Test
    public void testSeriesWithZero() {
        double res = HyperbolicCos.calculateSeries(0, 6);
        assertEquals(1.0, res, 1e-6);
    }

    @Test
    public void testSeriesWithSmallValue() {
        double x = 1.0;
        int k = 6;
        double res = HyperbolicCos.calculateSeries(x, k);
        double expect = Math.cosh(x);
        assertEquals(expect, res, 1e-6);
    }

    @Test
    public void testSeriesWithNegativeValue() {
        double x = -2.0;
        int k = 6;
        double res = HyperbolicCos.calculateSeries(x, k);
        double expect = Math.cosh(x);
        assertEquals(expect, res, 1e-6);
    }

    @Test
    public void testSeriesWithBigK() {
        double x = 1.5;
        int k = 10;
        double res = HyperbolicCos.calculateSeries(x, k);
        double expect = Math.cosh(x);
        assertEquals(expect, res, 1e-10);
    }

    @Test
    public void testSeriesWithLargeX() {
        double x = 10.0;
        int k = 6;
        double res = HyperbolicCos.calculateSeries(x, k);
        double expect = Math.cosh(x);
        assertEquals(expect, res, 1e-6);
    }

    @Test
    public void testSeriesPrecision() {
        double x = 1.0;
        int k = 3;
        double res = HyperbolicCos.calculateSeries(x, k);
        assertTrue(Math.abs(res - Math.cosh(x)) < 1e-3);
    }

    @Test
    public void testSeries() {
        double[] testValues = {0.1, 0.5, 1.0, 2.0, 3.0};
        int k = 8;
        for (double x : testValues) {
            double res = HyperbolicCos.calculateSeries(x, k);
            double expect = Math.cosh(x);
            assertEquals(expect, res, 1e-8);
        }
    }
}
