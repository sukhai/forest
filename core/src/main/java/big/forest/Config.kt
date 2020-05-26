package big.forest

data class Config(
    var level: Forest.Level,
    var preProcessLog: PreProcessLogCallback?,
    internal val trees: MutableList<Tree>
) {
    fun plant(tree: Tree) {
        trees.add(tree)
    }

    fun cut(tree: Tree) {
        trees.remove(tree)
    }
}

