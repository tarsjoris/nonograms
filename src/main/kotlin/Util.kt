public fun <T> Sequence<T>.split(predicate: (T) -> Boolean): Sequence<Sequence<T>> {
    val iterator = this.iterator()
    return sequence {
        while (iterator.hasNext()) {
            yield(sequence {
                while (iterator.hasNext()) {
                    val el = iterator.next()
                    if (predicate(el)) {
                        break
                    }
                    yield(el)
                }
            })
        }
    }
}