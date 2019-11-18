package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
open class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0

    private class Node<T>(var value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Трудоемкость - O(n)
     * Ресурсоемкость - O(1)
     */
    override fun remove(element: T): Boolean {
        val closest = find(element)

        if (closest == null || closest.value != element) return false

        removeThis(root, element)
        size--

        return true
    }

    private fun minimum(node: Node<T>): Node<T> = if (node.left != null) minimum(node.left!!) else node

    private fun maximum(node: Node<T>): Node<T> = if (node.right != null) maximum(node.right!!) else node

    private fun removeThis(node: Node<T>?, value: T): Node<T>? {
        if (node != null) {
            when {
                value > node.value -> node.right = removeThis(node.right!!, value)
                value < node.value -> node.left = removeThis(node.left!!, value)
                node.left != null -> {
                    node.value = maximum(node.left!!).value
                    node.left = removeThis(node.left!!, node.value)
                }
                node.right != null -> {
                    node.value = minimum(node.right!!).value
                    node.right = removeThis(node.right!!, node.value)
                }
                node == root -> {
                    root = null
                    return node
                }
                else -> return null
            }
        }
        return node
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var stack: Stack<Node<T>> = Stack()
        private var current: Node<T>? = null

        init {
            var node = root

            while (node != null) {
                stack.push(node)
                node = node.left
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         *
         * Трудоемкость - O(1)
         * Ресурсоемкость - O(1)
         */
        override fun hasNext(): Boolean = stack.isNotEmpty()

        /**
         * Поиск следующего элемента
         * Средняя
         *
         * Трудоемкость - O(n)
         * Ресурсоемкость - O(1)
         */
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()

            var node = stack.pop()
            val result = node

            if (node.right != null) {
                node = node.right

                while (node != null) {
                    stack.push(node)
                    node = node.left
                }
            }

            return result!!.value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         *
         * *
         * Трудоемкость - O(n)
         * Ресурсоемкость - O(1)
         */
        override fun remove() {
            if (current != null) remove(current!!.value)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    inner class BinarySubTree<T : Comparable<T>> internal constructor(private val start: T?, private val end: T?, private val tree: KtBinaryTree<T>) : KtBinaryTree<T>() {

        override var size: Int = 0
            get() {
                return findSize(tree.root)
            }

        /**
         * Трудоёмкость: O(n)
         * Ресурсоёмкость: O(1)
         */
        override fun contains(element: T): Boolean = tree.contains(element) && inRange(element)

        /**
         * Трудоёмкость: O(n)
         * Ресурсоёмкость: O(1)
         */

        private fun inRange(element: T): Boolean = (start == null || element >= start) && (end == null || element < end)

        /**
         * Трудоёмкость: O(n)
         * Ресурсоёмкость: O(1)
         */

        override fun add(element: T): Boolean {
            require(inRange(element))

            return tree.add(element)
        }

        /**
         * Трудоёмкость: O(1)
         * Ресурсоёмкость: O(n)
         */

        private fun findSize(node: Node<T>?): Int {
            var size = 0

            return when {
                node != null -> {
                    if (inRange(node.value)) size++

                    size += findSize(node.left)
                    size += findSize(node.right)

                    size
                }
                else -> size
            }
        }
    }

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     *
     * Трудоёмкость: O(1)
     * Ресурсоёмкость: O(1)
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> = BinarySubTree(fromElement, toElement, this)

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     *
     * Трудоёмкость: O(1)
     * Ресурсоёмкость: O(1)
     */
    override fun headSet(toElement: T): SortedSet<T> = BinarySubTree(null, toElement, this)

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     *
     * Трудоёмкость: O(1)
     * Ресурсоёмкость: O(1)
     */
    override fun tailSet(fromElement: T): SortedSet<T> = BinarySubTree(fromElement, null, this)

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

}
