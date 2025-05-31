package com.ranjan.smartcents.res


private val stringsProvider = StringsProvider("en")
val strings = stringsProvider.strings

interface Strings {
    val nameRequired: String
    val nameTooShort: String
    val emailRequired: String
    val invalidEmail: String
    val passwordRequired: String
    val passwordTooShort : String
    val invalidPassword: String
    val confirmPasswordRequired: String
    val passwordsDoNotMatch: String
    val somethingWentWring: String
    val emailAlreadyInUse: String
}

private class StringsProvider(private val lang: String) {
    val strings: Strings = when (lang) {
        else -> EnglishStrings
    }
}
