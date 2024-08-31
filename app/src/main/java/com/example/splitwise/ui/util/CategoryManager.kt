package com.example.splitwise.ui.util

class CategoryManager private constructor() {

    companion object {
        @Volatile
        private var instance: CategoryManager? = null

        fun getInstance(): CategoryManager {
            if (instance == null) synchronized(this) {
                instance = CategoryManager()
            }
            return instance!!
        }
    }


    private val categoryMap: HashMap<String, CategoryHolderData> = HashMap()

    data class CategoryHolderData(
        val type: Int,
        val categoryName: String,
        val imageUrl: String = "",
        var id: Long = 0
    )

    fun clearMap() {
        // Clear the existing map
        categoryMap.clear()

        // Add the new data
        categoryMap.putAll(
            hashMapOf(
                "${MOVIE}_movie" to CategoryHolderData(MOVIE, "Entertainment"),
                "${CLOTHING}_clothing" to CategoryHolderData(CLOTHING, "Clothing"),
                "${BEAUTY}_beauty" to CategoryHolderData(BEAUTY, "Beauty"),
                "${FOOD}_food" to CategoryHolderData(FOOD, "Food"),
                "${HEALTH}_health" to CategoryHolderData(HEALTH, "Health"),
                "${RENT}_rent" to CategoryHolderData(RENT, "Rent"),
                "${PETROL_PUMP}_petrolpump" to CategoryHolderData(PETROL_PUMP, "Fuel"),
                "${TRANSPORT}_transport" to CategoryHolderData(TRANSPORT, "Transport"),
                "${BIKE}_bike" to CategoryHolderData(BIKE, "Bike"),
                "${DONATE}_donate" to CategoryHolderData(DONATE, "Donation"),
                "${SPORTS}_sports" to CategoryHolderData(SPORTS, "Sports"),
                "${MOBILE}_mobile" to CategoryHolderData(MOBILE, "Mobile"),
                "${SHOPPING_GENERAL}_other" to CategoryHolderData(SHOPPING_GENERAL, "Other")
            )
        )
    }


    fun createNewCategory(type: String, categoryName: String, imageUrl: String = "",id:String = "") {
        val categoryKey = categoryName + "_$type"
        if (!categoryMap.containsKey(categoryKey)) {
            categoryMap[categoryKey] = CategoryHolderData(Integer.parseInt(type), categoryName, imageUrl)
        }
    }

    fun getTotalCategoryList(): ArrayList<CategoryHolderData> {
        val categoryList = ArrayList<CategoryHolderData>()
        categoryList.addAll(categoryMap.values)
        return categoryList
    }


}