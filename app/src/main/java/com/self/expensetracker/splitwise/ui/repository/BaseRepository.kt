package com.self.expensetracker.splitwise.ui.repository

import com.self.expensetracker.splitwise.ui.util.BEAUTY
import com.self.expensetracker.splitwise.ui.util.BIKE
import com.self.expensetracker.splitwise.ui.util.CLOTHING
import com.self.expensetracker.splitwise.ui.util.DONATE
import com.self.expensetracker.splitwise.ui.util.FOOD
import com.self.expensetracker.splitwise.ui.util.HEALTH
import com.self.expensetracker.splitwise.ui.util.MOBILE
import com.self.expensetracker.splitwise.ui.util.MOVIE
import com.self.expensetracker.splitwise.ui.util.PETROL_PUMP
import com.self.expensetracker.splitwise.ui.util.RENT
import com.self.expensetracker.splitwise.ui.util.SPORTS
import com.self.expensetracker.splitwise.ui.util.TRANSPORT
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
    fun getMonthShortName(month: Int): String {
        when (month) {
            Calendar.JANUARY ->
                return "JanY"

            Calendar.FEBRUARY ->
                return "Feb"

            Calendar.MARCH ->
                return "Mar"

            Calendar.APRIL ->
                return "Apr"

            Calendar.MAY ->
                return "May"

            Calendar.JUNE ->
                return "Jun"

            Calendar.JULY ->
                return "Jul"

            Calendar.AUGUST ->
                return "Aug"

            Calendar.SEPTEMBER ->
                return "Sep"

            Calendar.OCTOBER ->
                return "Oct"

            Calendar.NOVEMBER ->
                return "Nov"

            Calendar.DECEMBER ->
                return "Dec"
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

    fun getCategoryType(key: Int): String {
        when (key) {
            MOVIE ->
                return "MOVIE"

            CLOTHING ->
                return "CLOTHING"

            BEAUTY ->
                return "BEAUTY"

            FOOD ->
                return "FOOD"

            HEALTH ->
                return "HEALTH"

            RENT ->
                return "RENT"

            PETROL_PUMP ->
                return "PETROL_PUMP"

            BIKE ->
                return "BIKE"

            TRANSPORT ->
                return "TRANSPORT"

            DONATE ->
                return "DONATE"

            SPORTS ->
                return "SPORTS"

            MOBILE ->
                return "MOBILE"

            else ->
                return "OTHER"
        }

    }


}