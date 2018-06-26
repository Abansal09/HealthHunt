package in.healthhunt.model.articles;

/**
 * Created by abhishekkumar on 5/10/18.
 */

public interface ArticleParams {



    int TAGS_VIEW_ALL_TYPE = 0;
    int LATEST_VIEW_ALL_TYPE = 1;
    int LATEST_PRODUCT_VIEW_ALL_TYPE = 2;

    int BASED_ON_TAGS = 1001;
    int CONTINUE_ARTICLES = 1002;
    int TRENDING_ARTICLES = 1003;
    int SPONSORED_ARTICLES = 1004;
    int TOP_PRODUCTS = 1005;
    int LATEST_ARTICLES = 1006;
    int WEBINARS_ARTICLES = 1007;
    int LATEST_PRODUCTS = 1008;
    int RELATED_PRODUCTS = 1009;
    int RELATED_ARTICLES = 1010;

    String ARTICLE_TYPE = "article_type";

    String TEXT_BASED_ON_TAGS = "Based on tags";
    String TEXT_CONTINUE_ARTICLES = "Continue reading";
    String TEXT_TRENDING_ARTICLES = "Trending";
    String TEXT_SPONSORED_ARTICLES = "Sponsored";
    String TEXT_TOP_PRODUCTS_ARTICLES = "Top products";
    String TEXT_LATEST_ARTICLES = "Latest articles";
    String TEXT_WEBINARS_ARTICLES = "Webinars";
    String TEXT_LATEST_PRODUCTS_ARTICLES = "Latest products";
    String TEXT_RELATED_ARTICLES = "Related articles";
    String TEXT_RELATED_PRODUCTS = "Related products";


    String TAGS = "tags";
    String OFFSET = "offset";
    String LIMIT = "limit";
    String ID = "id";
    String ARTICLE_URL = "article_url";
    String CATEGORY_NAME = "categoryName";
    String AUTHOR_URL = "authorUrl";
    String AUTHOR_NAME = "authorName";
    String ARTICLE_TITLE = "article_title";
    String ARTICLE_TAGS_NAME_LIST = "article_related_tags_list";
    String ARTICLE_READ_TIME = "article_read_time";
    String ARTICLE_DATE = "article_date";
    String SECTION = "section";
    String LATEST_BY_RECENT = "recent";
    String LATEST_BY_WEEK = "week";
    String LATEST_BY_MONTH = "month";
    String SPONSORED = "sponsored";
    String POST_FORMAT_IMAGE = "post-format-image";
    String POST_FORMAT_VIDEO = "post-format-video";
    String TYPE = "type";
    String MARKET = "market";
    String MARKT_TYPE = "markt_type";
    String PRODUCT_NAME = "product_name";
    String PRODUCT_TYPE = "product_type";
    String PRODUCT_PRICE = "product_price";
    String PRODUCT_QUANTITY = "product_quantity";
    String PRODUCT_UNIT = "product_unit";
    String BRAND_NAME = "brand_name";
    String IS_BOOKMARK = "is_bookmark";
    String FORMAT = "format";
    int PRODUCT_SERVICES = 1;
    int PRODUCT_SERVICE_REQUEST = 2;
    String FILTER_TYPE = "filter_type";
    String BRANDS = "brands";
    String RELATED = "related";
    String FILTER = "filter";
    String COLLECTION = "collection";
    String AUTHOR = "author";
    String COLLECTION_SAVED = "saved";
    String COLLECTION_SAVED_MERKET = "saved_market";
    String COLLECTION_CREATED = "created";
    String COLLECTION_RECEIVED = "received";
    String FETCH = "fetch";
    String NOTI_ALL = "all";
    String NOTI_MARK_AS_COMPLETED = "mark_as_completed";
    String SEARCH = "search";
    String APP = "app";
    String CATEGORIES = "categories";
    String QTRANSLANG = "qtranslang";
    String ENGLISH_LAN = "en";
    String TAGS_FILTER = "tags_filter[]";
    String ORDER_BY = "orderby";
    String ORDER = "order";
    String DESC = "desc";
    String DATE = "date";
    String POST = "post";
    String INCLUDE = "include";

    String PRODUCT_TYPE_ID = "productType";
    String BRAND = "brand";
    String LOCATION = "location";

    String LIKE_TYPE = "like_type";

    String IS_LAST_PAGE = "is_last_page";

    String TRENDING = "trending";

    String POSITION = "position";

    int ARTICLE = 100;
    int PRODUCT = 200;
    int VIDEO = 300;

    String POST_TYPE = "post_type";

    int SAVED = 1000;
    int APPROVED = 2000;
    int RECEIVED = 3000;
    int DOWNLOADED = 4000;


    String POST_ID = "post_id";
}
