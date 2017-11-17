package de.mpicbg.scicomp.kip.misc

import de.mpicbg.scicomp.kip.BoundaryMethod
import net.imglib2.RandomAccessibleInterval
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory
import net.imglib2.outofbounds.OutOfBoundsFactory
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.RealType

/**
 * @author Benoit Lombardot
 * @author Holger Brandl
 */

object Format {

    // format a numeric array describing a parameter per dimension of an image/matrix
    fun perDim(pixelSize: Array<Float>?, nDim: Int): Array<Float> {
        var ps = pixelSize

        if (ps == null) {
            ps = FloatArray(nDim).toTypedArray()
            for (d in 0 until nDim) {
                ps[d] = 1f
            }
        } else if (ps.size == 1) {
            val pixelSize0 = ps[0]
            ps = FloatArray(nDim).toTypedArray()
            for (d in 0 until nDim) {
                ps[d] = pixelSize0
            }
        } else if (ps.size < nDim) {
            ps = null
            TODO("Error, the pixelSize is not consistent with the image dimension ( pixelSize.length vs. nDim )")

        } else if (ps.size > nDim) {
            val pixelSize0 = ps
            ps = FloatArray(nDim).toTypedArray()
            for (d in 0 until nDim) {
                ps[d] = pixelSize0[d]
            }

            //TODO: Warning! to many elements in pixelSize, only the nDim first will be used
        }

        return ps
    }

    fun <T> outOfBoundFactory(method: BoundaryMethod, valueT: T?): OutOfBoundsFactory<T, RandomAccessibleInterval<T>> where T : RealType<T>, T : NativeType<T> =
        when (method) {
            BoundaryMethod.zero, BoundaryMethod.value -> OutOfBoundsConstantValueFactory<T, RandomAccessibleInterval<T>>(valueT)
            BoundaryMethod.same, BoundaryMethod.periodic -> OutOfBoundsConstantValueFactory(valueT)
            BoundaryMethod.mirror -> OutOfBoundsMirrorFactory(OutOfBoundsMirrorFactory.Boundary.SINGLE)
            else -> TODO("boundary method not yet supported")
        }
}
