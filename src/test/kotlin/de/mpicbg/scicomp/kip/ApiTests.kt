package de.mpicbg.scicomp.kip

import net.imglib2.img.Img
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.real.FloatType
import org.junit.Test


class KipTests {

    //    companion object {
    //
    //        @BeforeClass
    //        @JvmStatic
    //        fun setup() {
    //            opService.info
    //        }
    //    }

    @Test
    fun loadProccesSave() {
        val image = arrayOf(
            0, 0, 0, 0, 0,
            0, 1, 0, 0, 1,
            0, 1, 0, 2, 3,
            0, 0, 0, 4, 0,
            0, 0, 0, 0, 0
        ).asImage(5, 5)


    }

    @Test
    fun loadImageFromFile() {
        val image = openImage<FloatType>("images/mitosis_t1.tif")
        println(image.dim())
    }

    @Test
    fun blurTest() {
        //        LegacyInjector.preinit();
        val blurred = bubbles().toFloat().gauss()
        blurred.show()
    }

    @Test
    fun medianTest() {
        //        LegacyInjector.preinit();
        val avg: Img<BitType> = bubbles().median(listOf(10f, 10f))



        avg.show()
    }
}