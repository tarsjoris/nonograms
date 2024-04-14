fun main() {
    //solveSimple()
    solveReal()
}

private fun solveSimple() {
    Field(
        arrayOf(
            arrayOf(1, 1),
            arrayOf(1),
            arrayOf(1, 1)
        ),
        arrayOf(
            arrayOf(1, 1),
            arrayOf(1),
            arrayOf(1, 1)
        )
    ).solve()
}

private fun solveReal() {
    Field(
        arrayOf(
            arrayOf(3, 7),
            arrayOf(1, 2, 5, 1, 1),
            arrayOf(5, 7, 1, 5),
            arrayOf(1, 3, 7, 5),
            arrayOf(1, 1, 2, 9),
            arrayOf(1, 1, 4, 2, 3, 2),
            arrayOf(1, 1, 4, 2, 1, 1, 1, 2),
            arrayOf(3, 18),
            arrayOf(1, 9, 3),
            arrayOf(1, 1, 4, 1, 1, 1, 2, 3),
            arrayOf(5, 3, 1, 2, 1),
            arrayOf(1, 1, 2, 1, 2, 5),
            arrayOf(1, 1, 1, 13, 2),
            arrayOf(1, 1, 2, 1, 1, 1),
            arrayOf(1, 1, 1, 3, 1, 1, 2),
            arrayOf(11, 5, 3),
            arrayOf(1, 6, 5),
            arrayOf(1, 1, 1, 7, 7),
            arrayOf(1, 18),
            arrayOf(1, 1, 1, 1, 14, 2),
            arrayOf(1, 1, 1, 1, 8, 5, 2),
            arrayOf(16, 6, 1),
            arrayOf(1, 1, 4, 4, 3),
            arrayOf(1, 1, 1, 3, 1, 8),
            arrayOf(1, 1, 1, 3, 1),
            arrayOf(1, 1, 1, 4, 4, 1),
            arrayOf(1, 1, 1, 1, 1, 7, 2, 1),
            arrayOf(1, 1, 1, 3, 1, 1, 3, 1),
            arrayOf(1, 1, 1, 1, 5, 1, 1, 1),
            arrayOf(1, 1, 1, 1, 1, 5, 4)
        ),
        arrayOf(
            arrayOf(2, 9),
            arrayOf(2, 1, 7),
            arrayOf(1, 1, 1, 6, 1, 1, 5),
            arrayOf(3, 1, 5, 1, 1, 3),
            arrayOf(1, 2, 1, 1, 1, 2, 1, 1, 5),
            arrayOf(11, 1, 1, 3),
            arrayOf(8, 6, 1, 1),
            arrayOf(5, 13),
            arrayOf(5, 4, 1, 1, 1, 3),
            arrayOf(5, 5, 1, 1, 1, 1),
            arrayOf(12, 3, 2),
            arrayOf(2, 13),
            arrayOf(8, 7, 2),
            arrayOf(5, 1, 6, 1),
            arrayOf(3, 1, 3, 4, 1, 1),
            arrayOf(4, 2, 1, 1, 1, 4, 1, 2),
            arrayOf(5, 1, 1, 1, 3, 1, 2),
            arrayOf(7, 1, 1, 3, 2, 1),
            arrayOf(5, 1, 1, 1, 3, 1, 2),
            arrayOf(4, 2, 1, 1, 1, 3, 1, 1),
            arrayOf(3, 1, 3, 7, 1),
            arrayOf(1, 5, 1, 10),
            arrayOf(1, 1, 22),
            arrayOf(1, 2, 4, 16),
            arrayOf(2, 1, 6, 1, 1, 1, 1),
            arrayOf(1, 2, 2, 3, 1, 1, 1),
            arrayOf(1, 2, 1, 2, 2, 1),
            arrayOf(1, 1, 1, 1, 5, 1),
            arrayOf(1, 1, 2, 5),
            arrayOf(2, 1, 2)
        )
    ).solve()
}

enum class EValue {
    Unknown,
    Marked,
    Empty
}

class Field(private val rowLimits: Array<Array<Int>>, private val columnLimits: Array<Array<Int>>) {
    private val width = columnLimits.size
    private val height = rowLimits.size
    private val size = width * height
    private val cells = Array(width * height) { EValue.Unknown }

    fun solve() {
        canComplete(0)
        print()
        println((0..<height).all { y -> isRowValid(y) })
        println((0..<width).all { x -> isColumnValid(x) })
    }

    private fun print() {
        (0..<height).forEach { y ->
            (0..<width).forEach { x ->
                print(getChar(cells[y * width + x]))
            }
            println()
        }
    }

    private fun getChar(value: EValue) = when (value) {
        EValue.Marked -> '#'
        EValue.Empty -> '.'
        EValue.Unknown -> ' '
    }

    private fun canComplete(index: Int): Boolean {
        if (index >= size) {
            return true
        }
        cells[index] = EValue.Marked
        return if (isValidAfterCellUpdate(index) && canComplete(index + 1)) {
            true
        } else {
            cells[index] = EValue.Empty
            if (isValidAfterCellUpdate(index) && canComplete(index + 1)) {
                true
            } else {
                cells[index] = EValue.Unknown
                false
            }
        }
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

    private fun areLimitsHonored(limits: Array<Int>, maxB: Int, coordsToIndex: (Int) -> Int): Boolean {
        var i = 0
        for (chunkSize in limits) {
            while (i < maxB && cells[coordsToIndex(i)] == EValue.Empty) {
                ++i
            }
            var counted = 0
            while (counted < chunkSize && i < maxB && cells[coordsToIndex(i)] != EValue.Empty) {
                ++i
                ++counted
            }
            if (counted < chunkSize) {
                return false
            }
            if (i < maxB && cells[coordsToIndex(i)] == EValue.Marked) {
                return false
            }
            ++i
        }
        while (i < maxB) {
            if (cells[coordsToIndex(i)] == EValue.Marked) {
                return false
            }
            ++i
        }
        return true
    }
}