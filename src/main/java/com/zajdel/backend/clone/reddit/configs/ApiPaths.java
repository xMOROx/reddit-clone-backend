package com.zajdel.backend.clone.reddit.configs;

public final class ApiPaths {
    private static final String BASE_PATH = "/api/v1";

    public static final class SubRedditCtrl {
        public static final String CTRL = BASE_PATH + "/subreddits";
    }
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

    public static final class SubredditCtrl {
        public static final String CTRL = BASE_PATH + "/subreddits";
    }
    public static final class ReplyCtrl {
        public static final String CTRL = BASE_PATH + "/replies";
    }

    public static final class AuthCtrl {
        public static final String CTRL = BASE_PATH + "/auth";
    }

    public static final class RefreshTokenCtrl {
        public static final String CTRL = BASE_PATH + "/refresh-token";
    }

    public static final class VerificationTokenCtrl {
        public static final String CTRL = BASE_PATH + "/verification-token";
    }

    public static final class PasswordResetTokenCtrl {
        public static final String CTRL = BASE_PATH + "/password-reset-token";
    }

    public static final class CorsOriginLink {
        public static final String LINK =  "http://localhost:8080";
    }

}
