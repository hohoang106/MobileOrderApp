package com.sh.orderapp.utils;

public class Const {

    public static class FIREBASE_URI {
        public static final String FOOD_REF = "Foods";
        public static final String USERS_REF = "Users";
        public static final String NEWS_REF = "News";
        public static final String COMMENT_FOOD_REF = "CommentFoods";
        public static final String INVOICE_USER_REF = "Invoices_user";
        public static final String INVOICE_ADMIN_REF = "Invoices_admin";
    }

    public static class USER_TYPE {
        public static final String RESTAURANT_MANAGE = "Restaurant";
        public static final String USER = "User";
        public static final String ADMIN = "Admin";
    }

    public static class USER_STATUS {
        public static final String APPROVE = "Approve";
        public static final String REJECT = "Reject";
        public static final String WAIT = "Wait";


    }

    public static final String BILL_SELECT = "BillSelected";
    public static final String FOOD_SELECT = "FoodSelect";
    public static final String USER_LOGIN = "UserLogin";
    public static final String NEW_SELECTED = "NewSelected";
    public static final String RESTAURANT_SELECTED = "RestaurantSelected";
    public static final String ACCOUNT_SELECTED = "AccountSelected";

}
