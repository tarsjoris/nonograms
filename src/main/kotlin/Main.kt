import java.io.File

fun main() {
    //readField("real.txt").solve()
    //readField("simple.txt").solve()
    readField("simple-color.txt").solve()
}

private fun readField(filename: String)
    = File("src/main/resources/$filename").useLines { lines ->
        val rowColumnLimits = lines.split(CharSequence::isEmpty)
            .map { it.map(::parseLine).toList() }
            .toList()
    val rowLimits = rowColumnLimits[0]
    val columnLimits = rowColumnLimits[1]
    Field(rowLimits, columnLimits)
}

private fun parseLine(line: String): Limit = line.split(" ")
    .map(::parseSpan)

private fun parseSpan(span: String) = Span(span[0] - 'A', span.substring(1).toInt())