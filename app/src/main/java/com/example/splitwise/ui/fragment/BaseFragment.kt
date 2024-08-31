package com.example.splitwise.ui.fragment

import androidx.fragment.app.Fragment
import com.example.splitwise.data.Data
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.BIKE
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.CategoryManager
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
    private val categoryManager : CategoryManager = CategoryManager.getInstance()


    fun createCategoryList(): ArrayList<Data> {
        categoryList.clear()
        var i = 0
        categoryManager.getTotalCategoryList().forEach {
            categoryList.add(i++, ExpenseCategoryData(it.categoryName, it.type, false))
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
        var today = "$date"
        //today += getMonthName(month)
        return today
    }

    fun getMonthName(month: Int): String {
        when (month) {
            Calendar.JANUARY ->
                return "Jan"

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
        name += ", "
        name += year
        return name
    }


    fun getPrevYear(currMonth: Int, currYear: Int): Int {
        if (currMonth > 0)
            return currYear
        return currYear - 1
    }


}