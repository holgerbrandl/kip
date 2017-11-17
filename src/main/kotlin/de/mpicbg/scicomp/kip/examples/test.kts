import org.scijava.Context
import java.io.File
println(Context::class.java.simpleName)
println(File(".").absolutePath)

enum class BType {
    DECIMAL,
    TIMESTAMP
}

val DECIMAL = BType.DECIMAL
val TIMESTAMP = BType.TIMESTAMP


print(DECIMAL)