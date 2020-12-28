package com.zolfagharipour.market.network

object NetworkParams {

    const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
    private const val PER_PAGE = "per_page"
    private const val NUMBER_OF_PER_PAGE = "20"
    private const val CONSUMER_KEY_TITLE = "consumer_key"
    private const val CONSUMER_SECRET_TITLE = "consumer_secret"
    private const val CONSUMER_KEY = "ck_f025265e3479f7bee8e93bffe5685517b93ec27d"
    private const val CONSUMER_SECRET = "cs_27b19e572ac9cf1333d4d53f7082a15e9fb6a2b0"

    val QUERY_OPTIONS_PRODUCTS: HashMap<String, String> = object : HashMap<String, String>() {
        init {
            put(CONSUMER_KEY_TITLE, CONSUMER_KEY)
            put(CONSUMER_SECRET_TITLE, CONSUMER_SECRET)
            put(PER_PAGE, NUMBER_OF_PER_PAGE)
        }
    }


}