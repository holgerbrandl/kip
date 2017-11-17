package de.mpicbg.scicomp.kip.samples

import de.mpicbg.scicomp.kip.bubbles
import net.imglib2.img.Img
import net.imglib2.type.logic.BitType


/**
 * @author Holger Brandl
 */


fun imageGeneraorExample() {

    val bubbles: Img<BitType> = bubbles(500, 500, cutoff = 130)

    //    bubbles.toFloat().gauss()

}

