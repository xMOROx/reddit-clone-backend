# reddit-clone-backend

Simple project showcasing basic REST methods using [![Spring][Spring.js]][Spring-url]

## Rest Api

### User

Requests

* ```GET /api/v1/users```
* ```GET /api/v1/users/{id}```
* ```POST /api/v1/users```
* ```DELETE /api/v1/users/{id}```
* ```PUT /api/v1/users/{id}```
* ```PATCH /api/v1/users/{id}```
* ```GET /api/v1/users/{id}/posts```
* ```GET /api/v1/users/{id}/posts/{postId}```
* ```POST /api/v1/users/{id}/posts```
* ```DELETE /api/v1/users/{id}/posts/{postId}```
* ```PUT /api/v1/users/{id}/posts/{postId}```
* ```PATCH /api/v1/users/{id}/posts/{postId}```
* ```GET /api/v1/users/{id}/comments```
* ```GET /api/v1/users/{id}/comments/{commentId}```
* ```DELETE /api/v1/users/{id}/comments/{commentId}```
* ```PATCH /api/v1/users/{id}/comments/{commentId}```
* ```GET /api/v1/users/{id}/subreddits```
* ```GET /api/v1/users/{id}/subreddits/{subRedditId}```
* ```POST /api/v1/users/{id}/subreddits```
* ```DELETE /api/v1/users/{id}/posts/{postId}```
* ```PUT /api/v1/users//{id}/subreddits/{subRedditId}```
* ```PATCH /api/v1/users//{id}/subreddits/{subRedditId}```

## Post

* ```GET /api/v1/posts```
* ```GET /api/v1/posts/{id}```
* ```GET /api/v1/posts/{id}/comments```

## Comment

* ```GET /api/v1/comments```
* ```GET /api/v1/comments/{id}```
* ```POST /api/v1/comments```
* ```DELETE /api/v1/comments/{id}```
* ```PATCH /api/v1/comments/{commentId}?userId={userId}&postId={postId}```
* ```PUT /api/v1/comments/{commentId}?userId={userId}&postId={postId}```
[Spring.js]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/
