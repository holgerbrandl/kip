package de.mpicbg.scicomp.kip.scratch

import de.mpicbg.scicomp.kip.bubbles
import de.mpicbg.scicomp.kip.median
import de.mpicbg.scicomp.kip.show
import net.imglib2.img.Img
import net.imglib2.type.logic.BitType


/**
 * @author Holger Brandl
 */
fun main(args: Array<String>) {
    val bubbles = bubbles()

    bubbles.show()

    val avg: Img<BitType> = bubbles.median(listOf(10f, 10f))

    avg.show()
}