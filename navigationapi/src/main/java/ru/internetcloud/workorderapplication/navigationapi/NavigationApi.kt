package ru.internetcloud.workorderapplication.navigationapi

interface NavigationApi <DIRECTION> {

    fun navigate(direction: DIRECTION)
}
