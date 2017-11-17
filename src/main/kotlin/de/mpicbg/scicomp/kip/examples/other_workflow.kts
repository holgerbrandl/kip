import de.mpicbg.scicomp.kip.*
import net.imagej.ops.OpService
import net.imglib2.type.numeric.real.FloatType
import org.scijava.Context
import org.scijava.console.ConsoleService


val image = bubbles()

opsService
pckgFun()


image.show()
image.save("my_image.png")


//val image2 = openImage<FloatType>("images/blobs32.tif")
val image2 = openImage<FloatType>("/Users/brandl/projects/kotlin/kip/images/blobs32.tif")

image2.dim()
image2.gauss()


//javax.swing.JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");


val foo = Context(OpService::class.java, ConsoleService::class.java).getService(OpService::class.java)

var ctx = Context()
var ops = ctx.getService(OpService::class.java)
ops.filter()
//val ij = ImageJ()

