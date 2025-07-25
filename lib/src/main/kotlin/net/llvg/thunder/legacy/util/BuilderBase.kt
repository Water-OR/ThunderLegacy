package net.llvg.thunder.legacy.util

interface BuilderBase<out R> {
    fun build(): R
}