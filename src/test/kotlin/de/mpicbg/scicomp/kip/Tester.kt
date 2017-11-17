package de.mpicbg.scicomp.kip

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
    fun blurTest() {
        val blurred = bubbles().toFloat().gauss()
        blurred.show()

    }
}
