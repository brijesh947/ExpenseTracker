package com.example.splitwise.ui.repository

import java.util.Calendar

open class BaseRepository {

    fun getDate(date: Int, month: Int): String {
        var today = "$date "
        today+=getMonthName(month)
        return today
    }
    fun getMonthName(month: Int): String {
        when (month) {
            Calendar.JANUARY ->
                return "JANUARY"

            Calendar.FEBRUARY ->
                return "FEBRUARY"

            Calendar.MARCH ->
                return "MARCH"

            Calendar.APRIL ->
                return "APRIL"

            Calendar.MAY ->
                return "MAY"

            Calendar.JUNE ->
                return "JUNE"

            Calendar.JULY ->
                return "JULY"

            Calendar.AUGUST ->
                return "AUGUST"

            Calendar.SEPTEMBER ->
                return "SEPTEMBER"

            Calendar.OCTOBER ->
                return "OCTOBER"

            Calendar.NOVEMBER ->
                return "NOVEMBER"

            Calendar.DECEMBER ->
                return "DECEMBER"
        }
        return ""
    }

    fun needToAddDMYData(calendar: Calendar, month: Int, year: Int, date: Int, filterType: Int): Boolean {
        when (filterType) {
            Calendar.YEAR -> {
                if (calendar.get(Calendar.YEAR) != year)
                    return true
            }

            Calendar.MONTH -> {
                if (calendar.get(Calendar.MONTH) != month)
                    return true
            }

            Calendar.DATE -> {
                if (calendar.get(Calendar.DATE) != date)
                    return true
            }
        }
        return false
    }


}