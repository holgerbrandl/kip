package de.mpicbg.scicomp.kip.examples

import com.github.holgerbrandl.kravis.histogram
import de.mpicbg.scicomp.kip.*
import net.imglib2.roi.labeling.LabelRegions
import net.imglib2.type.logic.BitType

/**
 * @author Holger Brandl
 */


data class RegionSize(val id: Int, val numPixels: Long)

val net.imglib2.roi.labeling.ImgLabeling<Int, BitType>.regionSizes: List<RegionSize>
    get() = with(LabelRegions(this)) {
        existingLabels.map { RegionSize(it, getLabelRegion(it).size()) }
    }


fun main(args: Array<String>) {

    bubbles().invert().label().indexImg.show("labeled bubbles")
    val image = bubbles().invert()

    //    val image = bubbles().gauss(10).threshold(0.3f).invert()

    //val traceCells = image.trackCells(sensitivity = 0.4)
    val labeling = image.label()
    labeling.indexImg.show()
    image.dim()

    val sizes = with(LabelRegions(labeling)) {
        existingLabels.map { getLabelRegion(it).size() }
    }


    //    plotOf(labeling.regionSizes) {
    //        encoding(EncodingChannel.x, label= "region size", bin=true) { it.numPixels }
    //        encoding(EncodingChannel.y, aggregate = Aggregate.count)
    //
    //    }.render()


    labeling.regionSizes.histogram { numPixels }.render()

}
