# reddit-clone-backend

Simple project showcasing basic REST methods using [![Spring][Spring.js]][Spring-url]

## Rest Api

### User 
Requests
*   ```GET /api/v1/users ``` 
* Request  ```GET /api/v1/users/{id} ``` 
* Request  ```POST /api/v1/users ```
* Request  ```DELETE /api/v1/users/{id} ```
* Request  ```PUT /api/v1/users/{id} ```
* Request  ```PATCH /api/v1/users/{id} ```
* Request  ```GET /api/v1/users/{id}/posts ```
* Request  ```GET /api/v1/users/{id}/posts/{postId} ```
* Request  ```POST /api/v1/users/{id}/posts ```
* Request  ```DELETE /api/v1/users/{id}/posts/{postId} ```
* Request  ```PUT /api/v1/users/{id}/posts/{postId} ```
* Request  ```PATCH /api/v1/users/{id}/posts/{postId} ```
* Request  ```GET /api/v1/users/{id}/comments ```
* Request  ```GET /api/v1/users/{id}/comments/{commentId} ```
* Request  ```DELETE /api/v1/users/{id}/comments/{commentId} ```
* Request  ```PATCH /api/v1/users/{id}/comments/{commentId} ```
* Request  ```GET /api/v1/users/{id}/subreddits ```
* Request  ```GET /api/v1/users/{id}/subreddits/{subRedditId} ```
* Request  ```POST /api/v1/users/{id}/subreddits ```
* Request  ```DELETE /api/v1/users/{id}/posts/{postId} ```
* Request  ```PUT /api/v1/users//{id}/subreddits/{subRedditId} ```
* Request  ```PATCH /api/v1/users//{id}/subreddits/{subRedditId} ```

## Post
* Request  ```GET /api/v1/posts ``` 
* Request  ```GET /api/v1/posts/{id} ``` 
* Request  ```GET /api/v1/posts/{id}/comments ``` 

## Comment
* Request  ```GET /api/v1/comments ``` 
* Request  ```GET /api/v1/comments/{id} ``` 
* Request  ```POST /api/v1/comments ```
* Request  ```DELETE /api/v1/comments/{id} ```
* Request  ```PATCH /api/v1/comments/{commentId}?userId={userId}&postId={postId} ```
* Request  ```PUT /api/v1/comments/{commentId}?userId={userId}&postId={postId} ```
[Spring.js]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/
