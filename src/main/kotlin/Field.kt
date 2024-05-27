data class Span(val color: Int, val length: Int)

typealias Limit = List<Span>
typealias Limits = List<Limit>

class Field(private val rowLimits: Limits, private val columnLimits: Limits) {
    private val width = columnLimits.size
    private val height = rowLimits.size
    private val size = width * height
    private val cells = Array(width * height) { UNKNOWN }
    private val maxColor = rowLimits.maxOf { limit ->
        limit.maxOf { span -> span.color }
    }

    fun solve() {
        println(canComplete(0))
        print()
    }

    private fun print() {
        (0..<height).forEach { y ->
            (0..<width).forEach { x ->
                print(getChar(cells[y * width + x]))
            }
            println()
        }
    }

    private fun getChar(value: Int)
        = if (value > EMPTY) {
            "#" + (value + 'A'.code).toChar()
        } else {
            "  "
        }

    private fun canComplete(index: Int): Boolean {
        if (index >= size) {
            return true
        }
        val correctColor = (EMPTY..maxColor).firstOrNull { color ->
            cells[index] = color
            isValidAfterCellUpdate(index) && canComplete(index + 1)
        }
        cells[index] = correctColor ?: UNKNOWN
        return correctColor != null
    }

    private fun isValidAfterCellUpdate(index: Int): Boolean {
        val x = index % width
        val y = index / width
        return isRowValid(y) && isColumnValid(x)
    }

    private fun isRowValid(y: Int)
        = areLimitsHonored(rowLimits[y], width) { x -> y * width + x }

    private fun isColumnValid(x: Int)
        = areLimitsHonored(columnLimits[x], height) { y -> y * width + x }

    private fun areLimitsHonored(limit: Limit, maxB: Int, coordsToIndex: (Int) -> Int): Boolean {
        var i = 0
        var previousColor = UNKNOWN
        for (span in limit) {
            if (previousColor == span.color) {
                ++i // there should be space between same-color span
            }
            while (i < maxB && cells[coordsToIndex(i)] == EMPTY) {
                ++i
            }
            var counted = 0
            while (i < maxB && counted < span.length && couldBeColor(cells[coordsToIndex(i)], span.color)) {
                ++i
                ++counted
            }
            if (counted < span.length) {
                return false
            }
            if (i < maxB && cells[coordsToIndex(i)] == span.color) {
                return false
            }
            previousColor = span.color
        }
        while (i < maxB) {
            if (cells[coordsToIndex(i)] > EMPTY) {
                return false
            }
            ++i
        }
        return true
    }

    companion object {
        const val EMPTY = -1
        const val UNKNOWN = -2

        private fun couldBeColor(cell: Int, color: Int)
            = cell == color || cell == UNKNOWN
    }
}