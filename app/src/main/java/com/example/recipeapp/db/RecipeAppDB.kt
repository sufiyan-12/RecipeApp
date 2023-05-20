package com.example.recipeapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.recipeapp.models.Ingredients
import com.example.recipeapp.models.RecipeModel

class RecipeAppDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 2) {

    companion object{
        private const val DB_NAME = "recipe_database"
        private const val DB_VERSION = 1

        // User Register or Login
        private const val USER_TABLE = "users"
        private const val ID = "id"
        private const val USER_EMAIL = "user_name"
        private const val USER_PASS = "user_pass"

        // Meals Record
        private const val MEAL_TABLE = "meal_table"
        private const val MEAL_ID = "meal_id"
        private const val MEAL_NAME = "name"
        private const val MEAL_CATEGORY = "category"
        private const val MEAL_INSTRUCTION = "instruction"
        private const val THUMBNAIL = "thumbnail"
        private const val YOUTUBE = "youtube"
        private const val FAVORITE = "favorite"

        // Ingredient Records
        private const val INGREDIENT_TABLE = "ingredient_table"
        private const val INGREDIENT_ID = "ingredient_id"
        private const val INGREDIENT = "ingredient"
        private const val QUANTITY = "quantity"
    }
    override fun onCreate(obj: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE IF NOT EXISTS $USER_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$USER_EMAIL TEXT, $USER_PASS TEXT);"

        val createMealTable = "CREATE TABLE IF NOT EXISTS $MEAL_TABLE ($MEAL_ID INTEGER PRIMARY KEY,"+
                "$ID TEXT, $MEAL_NAME TEXT, $MEAL_CATEGORY TEXT, $MEAL_INSTRUCTION TEXT,"+
                "$THUMBNAIL TEXT, $YOUTUBE TEXT, $FAVORITE INTEGER);"

        val createIngredientTable = "CREATE TABLE IF NOT EXISTS $INGREDIENT_TABLE ($INGREDIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$ID TEXT, $MEAL_ID TEXT,$INGREDIENT TEXT, $QUANTITY INTEGER);"

        obj?.execSQL(createUserTable)
        obj?.execSQL(createMealTable)
        obj?.execSQL(createIngredientTable)
    }

    override fun onUpgrade(obj: SQLiteDatabase?, p1: Int, p2: Int) {
        val userTable = "DROP TABLE IF EXISTS $USER_TABLE"
        val mealTable = "DROP TABLE IF EXISTS $MEAL_TABLE"
        val ingredientTable = "DROP TABLE IF EXISTS $INGREDIENT_TABLE"

        obj?.execSQL(userTable)
        obj?.execSQL(mealTable)
        obj?.execSQL(ingredientTable)
    }

    fun isUserAvailable(email: String, password: String) : Boolean{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $USER_TABLE", null)

        while (cursor.moveToNext()){
            val getEmail = cursor.getString(1)
            val getPassword = cursor.getString(2)

            if(email == getEmail && password == getPassword) {
                db.close()
                return true
            }
        }
        db.close()
        return false
    }

    fun addUser(email: String, password:String){
        val db = writableDatabase
        val values = ContentValues()
        values.put(USER_EMAIL, email)
        values.put(USER_PASS, password)
        db.insert(USER_TABLE, null, values)
        db.close()
    }

    fun addMeal(recipe: RecipeModel, id: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(MEAL_ID, recipe.id)
        values.put(ID, id)
        values.put(MEAL_NAME, recipe.name)
        values.put(MEAL_CATEGORY, recipe.category)
        values.put(MEAL_INSTRUCTION, recipe.instructions)
        values.put(THUMBNAIL, recipe.thumbnail)
        values.put(YOUTUBE, recipe.youtube)
        values.put(FAVORITE, recipe.favorite)
        db.insert(MEAL_TABLE, null, values)
        db.close()
    }

    fun addIngredient(ingredient: Ingredients, id: String, mealId: String){
        val db = writableDatabase
        val values = ContentValues()
        values.put(ID, id)
        values.put(MEAL_ID, mealId)
        values.put(INGREDIENT, ingredient.name)
        values.put(QUANTITY, ingredient.quantity)
        db.insert(INGREDIENT_TABLE, null, values)
        db.close()
    }

    fun getAllMeals(id: String): ArrayList<RecipeModel> {
        if(id.isEmpty()) return ArrayList()
        val recipeList = ArrayList<RecipeModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $MEAL_TABLE WHERE $ID = '${id.toInt()}'", null)

            while (cursor.moveToNext()){
            val itemId = cursor.getInt(0)
            val name = cursor.getString(2)
            val category = cursor.getString(3)
            val instructions = cursor.getString(4)
            val thumbnail = cursor.getString(5)
            val youtube = cursor.getString(6)
            val favorite = cursor.getInt(7)
            val list = ArrayList<Ingredients>()
                val newCursor = db.rawQuery("SELECT * FROM $INGREDIENT_TABLE WHERE $ID = '${id}' AND $MEAL_ID = '${itemId}'", null)
                while (newCursor.moveToNext()){
                    val name = newCursor.getString(3)
                    val quantity = newCursor.getInt(4)
                    list.add(Ingredients(name, quantity.toString()))
                }
            val item = RecipeModel(itemId.toString(), name, category, instructions, thumbnail, youtube, favorite,list)
            recipeList.add(item)
        }
        return recipeList
    }

    fun getFavoriteMeals(id: String): ArrayList<RecipeModel> {
        val recipeList = ArrayList<RecipeModel>()

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $MEAL_TABLE WHERE $ID = $id AND $FAVORITE = 1", null)

        while (cursor.moveToNext()){
            val mealId = cursor.getInt(0)
            val name = cursor.getString(2)
            val category = cursor.getString(3)
            val instructions = cursor.getString(4)
            val thumbnail = cursor.getString(5)
            val youtube = cursor.getString(6)
            val favorite = cursor.getInt(7)

            val item = RecipeModel(mealId.toString(), name, category, instructions, thumbnail, youtube, favorite,ArrayList<Ingredients>())
            recipeList.add(item)
        }
        return recipeList
    }

    fun getShoppingList(id: String): ArrayList<Ingredients>{
        val ingredientList = ArrayList<Ingredients>()

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $INGREDIENT_TABLE WHERE $ID = '$id'", null)

        while (cursor.moveToNext()){
            val name = cursor.getString(3)
            val quantity = cursor.getString(4)

            val item = Ingredients(name, quantity)
            ingredientList.add(item)
        }

        return ingredientList
    }


    fun updateUser(userId: Int, newName: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USER_EMAIL, newName)
            put(USER_PASS, newPassword)
        }

        val rowsAffected = db.update(USER_TABLE, contentValues, "$ID = ?", arrayOf(userId.toString()))
        db.close()

        return rowsAffected > 0
    }

    fun deleteMealById(id: String, mealId: Int): Boolean {
        val db = writableDatabase

        val rowsAffected = db.delete(MEAL_TABLE, "$ID = '$id' AND $MEAL_ID = '$mealId'", null)
        db.close()

        return rowsAffected > 0
    }

    fun deleteIngredientById(id: String, item: Ingredients): Boolean{
        val db = writableDatabase
        val rowAffected = db.delete(INGREDIENT_TABLE, "$ID = '$id' AND $INGREDIENT = '${item.name}' AND $QUANTITY = '${item.quantity}'", null)
        db.close()

        return rowAffected > 0
    }

    fun deleteIngredientsOfMeals(id: String, mealId: String): Boolean{
        val db = writableDatabase
        val rowAffected = db.delete(INGREDIENT_TABLE, "$ID = '$id' AND $MEAL_ID = '$mealId'", null)
        db.close()

        return rowAffected > 0
    }
    fun updateFavoriteById(id: String, mealId: Int, isFavorite: Boolean): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(FAVORITE, if (isFavorite) 1 else 0)
        }

        val rowsAffected = db.update(MEAL_TABLE, contentValues, "$ID = ? AND $MEAL_ID = ?", arrayOf(id, mealId.toString()))
        db.close()

        return rowsAffected > 0
    }

    fun getUserId(username: String, password: String): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $USER_TABLE WHERE $USER_EMAIL = '$username' AND $USER_PASS = '$password'", null)
        var id: String = ""

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0).toString()
        }

        cursor.close()
        db.close()
        return id
    }

}