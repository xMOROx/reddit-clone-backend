package com.example.backendclonereddit.configs;

public final class ApiPaths {
    private static final String BASE_PATH = "/api/v1";

    public static final class PostCtrl {
        public static final String CTRL = BASE_PATH + "/posts";
    }

    public static final class CommentCtrl {
        public static final String CTRL = BASE_PATH + "/comments";
    }

    public static final class VoteCtrl {
        public static final String CTRL = BASE_PATH + "/votes";
    }

    public static final class UserCtrl {
        public static final String CTRL = BASE_PATH + "/users";
    }

}
