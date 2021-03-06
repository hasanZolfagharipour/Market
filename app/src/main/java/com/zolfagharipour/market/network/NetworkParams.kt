package com.zolfagharipour.market.network

object NetworkParams {

    const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"

    private const val CONSUMER_KEY_TITLE = "consumer_key"
    private const val CONSUMER_SECRET_TITLE = "consumer_secret"
    private const val CONSUMER_KEY = "ck_f025265e3479f7bee8e93bffe5685517b93ec27d"
    private const val CONSUMER_SECRET = "cs_27b19e572ac9cf1333d4d53f7082a15e9fb6a2b0"
    private const val PER_PAGE = "per_page"
    private const val PAGE = "page"
    const val NUMBER_OF_PER_PAGE_PRODUCTS_IN_CATEGORY = "10"
    const val NUMBER_OF_PER_PAGE_PRODUCTS_IN_HOME_TAB = "20"
    private const val ORDER_BY = "orderby"

    const val ORDER_BY_POPULAR = "popularity"
    const val ORDER_BY_RATE = "rating"
    const val ORDER_BY_DATE = "date"
    private const val ORDER = "order"
    private const val ORDER_ASC = "asc"

    class CategoryID {
        companion object {
            const val DIGITAL_ID = "52"
            const val FASHION_ID = "62"
            const val ART_AND_BOOK_ID = "76"
            const val SUPER_MARKET_ID = "81"
            const val HEALTHY_ID = "121"
            const val SPECIAL_ID = "119"
            const val SLIDER_ID = "608"
            const val LAST_PRODUCTS_ID = "-1"
            const val POPULAR_PRODUCTS_ID = "-2"
            const val BEST_PRODUCTS_ID = "-3"
        }
    }

    val QUERY_OPTIONS_BASIC: HashMap<String, String> = object : HashMap<String, String>() {
        init {
            put(CONSUMER_KEY_TITLE, CONSUMER_KEY)
            put(CONSUMER_SECRET_TITLE, CONSUMER_SECRET)
        }
    }

    fun queryOptionsProductsByOrder(orderBy: String, perPage: String, page: Int = 1): HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put(ORDER_BY, orderBy)
                put(PER_PAGE, perPage)
                put(PAGE, page.toString())
            }
        }

    fun queryOptionsProductInCategory(categoryId: String, page: Int): HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put(PER_PAGE, NUMBER_OF_PER_PAGE_PRODUCTS_IN_CATEGORY)
                put("category", categoryId)
                put("page", page.toString())
            }
        }

    val QUERY_OPTIONS_DIGITAL_CATEGORY: HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put("parent", CategoryID.DIGITAL_ID)
            }
        }

    val QUERY_OPTIONS_FASHION_AND_MODEL_CATEGORY: HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put("parent", CategoryID.FASHION_ID)
            }
        }

    val QUERY_OPTIONS_ART_AND_BOOK_CATEGORY: HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put("parent", CategoryID.ART_AND_BOOK_ID)
            }
        }

    val QUERY_OPTIONS_SUPER_MARKET_CATEGORY: HashMap<String, String> =
        object : HashMap<String, String>(QUERY_OPTIONS_BASIC) {
            init {
                put("parent", CategoryID.SUPER_MARKET_ID)
            }
        }


}