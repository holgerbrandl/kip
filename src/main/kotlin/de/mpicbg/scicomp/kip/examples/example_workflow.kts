import de.mpicbg.scicomp.kip.bubbles
import de.mpicbg.scicomp.kip.openImage
import de.mpicbg.scicomp.kip.save
import de.mpicbg.scicomp.kip.show
import net.imglib2.type.numeric.real.FloatType

val image = bubbles()


image.show()
image.save("my_image.png")


val image2 = openImage<FloatType>("my_image.png")


//javax.swing.JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");


