@file:MavenRepository("imagej", "http://maven.imagej.net")
//@file:MavenRepository("imagej2", "http://maven.imagej.net/content/repositories/releases/")

@file:DependsOn("de.mpicbg.scicomp.experimental:kip:0.1-SNAPSHOT")


import com.github.holgerbrandl.kravis.spec.Aggregate
import com.github.holgerbrandl.kravis.spec.EncodingChannel
import com.github.holgerbrandl.kravis.spec.plotOf
import de.mpicbg.scicomp.kip.*
import net.imglib2.RandomAccessibleInterval
import net.imglib2.roi.labeling.ImgLabeling

//

/**
 * @author Holger Brandl
 */

//System.setProperty("java.awt.headless", "false")

//val opService = Context(OpService::class.java, ConsoleService::class.java).getService(OpService::class.java)
//val opService = Context().getService(OpService::class.java)

val bubbleImage = bubbles()
bubbleImage.show()


val image = bubbles()
//    .gauss(listOf(10f))
//    .threshold(0.3f)

image.show()
//val blurred = image.gauss()
//blurred.show()
//blurred.median(listOf(3f, 3f))


//val traceCells = image.trackCells(sensitivity = 0.4)
val labeling = image.label()
image.dim()

data class RegionSize(val id: Int, val numPixels: Int)


val ImgLabeling<*, *>.regionSizes: List<RegionSize>
    get() {

        //        return map { it.index.getInteger() to it.toArray().size }

        // with data class
        return map { RegionSize(it.index.getInteger(), it.toArray().size) }
        // convet to expression body

    }
//
plotOf(labeling.regionSizes) {
    encoding(EncodingChannel.x, aggregate = Aggregate.count) { it.numPixels }
}.render()


// misc

val other = bubbles()

// image arithmetics
val imageDiff = image + other
val imageProp = image / other

// number arithmethics

operator fun RandomAccessibleInterval<*>.plus(rhs: Int) = this

val imageDiff = image + 3