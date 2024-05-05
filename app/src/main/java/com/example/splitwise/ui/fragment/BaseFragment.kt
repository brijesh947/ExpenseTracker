package com.example.splitwise.ui.fragment

import androidx.fragment.app.Fragment
import com.example.splitwise.data.Data
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.BIKE
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.DONATE
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOBILE
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT
import com.example.splitwise.ui.util.SPORTS
import com.example.splitwise.ui.util.TRANSPORT
import java.util.Calendar

open class BaseFragment() : Fragment() {


    val categoryList: ArrayList<Data> = ArrayList()

    fun createCategoryList(): ArrayList<Data> {
        categoryList.clear()
        for (i in 101..113) {
            categoryList.add(ExpenseCategoryData(i, false))
        }
        return categoryList

    }

    fun getFilterType(selectedCategory: Int): String {

        when (selectedCategory) {
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

    fun getDate(date: Int, month: Int): String {
        var today = "$date "
        today += getMonthName(month)
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

    fun getPreviousMonth(month: Int): Int {
        if (month > 0)
            return month - 1;
        return 11;

    }

    fun getNextYear(currMonth: Int, currYear: Int): Int {
        if (currMonth < 11)
            return currYear
        return currYear + 1
    }

    fun getNextMonth(currMonth: Int): Int {
        if (currMonth < 11)
            return currMonth + 1
        return 0;
    }

    fun getCurrentMonth(month: Int, year: Int): String {
        var name = ""
        name += getMonthName(month)
        name += " "
        name += year
        return name
    }


    fun getPrevYear(currMonth: Int, currYear: Int): Int {
        if (currMonth > 0)
            return currYear
        return currYear - 1
    }


}