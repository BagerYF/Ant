package com.example.ant.common.tools

class AppTools {
    fun containsOnlyLetters(chr: Char) : Boolean {
        if (!(chr >= 'a' && chr <= 'z') && !(chr >= 'A' && chr <= 'Z') ) {
            return false
        }
        return true
    }
}