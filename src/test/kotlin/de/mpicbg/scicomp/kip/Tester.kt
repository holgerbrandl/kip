package de.mpicbg.scicomp.kip

import net.imglib2.type.numeric.real.FloatType
import org.junit.Test

/**
 * @author Holger Brandl
 */


class KipTests {

    @Test
    fun loadProccesSave() {
        val image = arrayOf(
            0, 0, 0, 0, 0,
            0, 1, 0, 0, 1,
            0, 1, 0, 2, 3,
            0, 0, 0, 4, 0,
            0, 0, 0, 0, 0
        ).asImage(5, 5)

        //        image.

    }

    @Test
    fun loadImageFromFile() {
        val image = openImage<FloatType>("images/mitosis_t1.tif")
        image.gauss()
    }

    @Test
    fun blurTest() {
        //                System.setProperty("java.awt.headless", "false")
        //        LegacyInjector.preinit();


        val blurred = bubbles().toFloat().gauss()
        blurred.show()

    }

}