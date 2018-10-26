import de.mpicbg.scicomp.kip.bubbles
import de.mpicbg.scicomp.kip.invert
import de.mpicbg.scicomp.kip.label
import de.mpicbg.scicomp.kip.show
import net.imglib2.img.Img
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.numeric.NumericType

System.setProperty("java.awt.headless", "false")

val image = bubbles(height = 660)


val label = image.invert().label()

label.mapping.labels

image.show()

fun <T : NumericType<T>> Img<T>.showMe() {
    ImageJFunctions.show(this, "")
}
image.showMe()


//private val <T, I> ImgLabeling<T, I>.sizes: Unit
//    get() {
//        return LabelRegions()
//    }
