CreateProductTest:
createProductInFoodCategoryTest - создание нового продукта
tearDown - удалить продукт по идентификатору

GetCategoryTest:
getCategoryByIdPositiveTest -  возвращает информацию о категории по существующему идектификатору категории (1)
getCategoryByIdNotExisNegativeTest - при указании не существующего идентификатора (999) категории, возвращает 404

GetProductIdTest:
getProductByIdInFoodTest - при указании существующего идентификатора (1) продуката, возвращает продукт по указанному номеру
getProductByIdZeroInFoodTest - при указании не существующего идентификатора (0) продуката, возвращает 404

GetProductsTest:
getAllProductsTest - без указания параметров, получить данные о всех продуктах

PutProductTest:
putProductByAllInFoodPositiveTest - при указании всех параметров продукта по указанному идентификатору, обновить характеристики продукта
putProductByTitleNullInFoodPositiveTest - если не указан параметр продукта Title, то значение у продукта обновляется на Null
putProductByPriceNullInFoodPositiveTest - если не указан параметр продукта Price, то значение у продукта обновляется на 0
