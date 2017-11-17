package de.mpicbg.scicomp.kip.samples

import de.mpicbg.scicomp.kip.bubbles
import de.mpicbg.scicomp.kip.gauss
import de.mpicbg.scicomp.kip.show
import de.mpicbg.scicomp.kip.toFloat

/**
 * @author Holger Brandl
 */


fun main(args: Array<String>) {
    //    LegacyInjector.preinit();
    //    System.setProperty("java.awt.headless", "false")
    //        LegacyInjector.preinit();


    val toFloat = bubbles().toFloat()
    val blurred = toFloat.gauss()
    blurred.show()
}
