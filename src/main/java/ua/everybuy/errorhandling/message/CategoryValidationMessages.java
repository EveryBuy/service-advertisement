package ua.everybuy.errorhandling.message;

public class CategoryValidationMessages {
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found with ID %d";
    public static final String LOW_LEVEL_SUBCATEGORY_ERROR_MESSAGE =
            "Low-level subcategory does not belong to the specified top-level subcategory";
    public static final String LOW_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE = "Low-level subcategory not found with ID %d";
    public static final String TOP_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE = "Top-level subcategory not found with ID %d";
    public static final String TOP_LEVEL_SUBCATEGORY_INVALID_CATEGORY_MESSAGE =
            "Top-level subcategory does not belong to the specified category";

}
