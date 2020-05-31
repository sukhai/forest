package big.forest

/**
 * A handler to all the logs that sent to [Forest].
 *
 * The implementation class will just have to implement the only method in this interface,
 * which is [log].
 *
 * Whenever [Forest.log]-type method is called, it will construct a [LogEntry] and delegate
 * the actual handling to the [log] method in this class.
 */
interface Tree {
    /**
     * A method to handle the given log [entry] that was sent from [Forest] or caller of
     * this class.
     *
     * @param entry The [LogEntry] that contains the information of the log.
     */
    fun log(entry: LogEntry)
}